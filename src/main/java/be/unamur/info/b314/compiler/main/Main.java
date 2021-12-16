package be.unamur.info.b314.compiler.main;

import be.unamur.info.b314.compiler.GrammarLexer;
import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.PilsToNbcConverter;
import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.program.ProgramMapper;
import be.unamur.info.b314.compiler.pils.program.Program;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author James Ortiz - james.ortizvega@unamur.be
 */
@Slf4j
public class Main {

    private static final String NAME = "b314-compiler";
    private static final String HELP = "h";
    private static final String INPUT = "i";
    private static final String OUTPUT = "o";
    /**
     * The command line options.
     */
    private final Options options;
    /**
     * The input B314 file.
     */
    private File inputFile;
    /**
     * The output PCode file.
     */
    private File outputFile;

    private Main() {
        // Create command line options
        options = new Options();
        options.addOption(Option.builder(HELP)
              .desc("Prints this help message.")
              .build());

        options.addOption(Option.builder(INPUT)
              .desc("The B314 input file.")
              .hasArg()
              .build());

        options.addOption(Option.builder(OUTPUT)
              .desc("The PCOde output file.")
              .hasArg()
              .build());
    }

    /**
     * Main method launched when starting compiler jar file.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Main main = new Main();
        CommandLineParser parser = new DefaultParser();
        CommandLine line = null;
        try {
            log.info("Arguments:\n\t" + String.join("\n\t", args));
            line = parser.parse(main.options, args);
        } catch (ParseException ex) {
            log.error("Error while parsing command line!", ex);
            main.printHelpMessage();
        }
        // If help is requested, print help message and exit.
        if (line != null) {
            if (line.hasOption(HELP)) {
                main.printHelpMessage();
            } else {
                // Else start compilation
                try {
                    main.initialise(line);
                    main.compile(); // Call compile method (to be completed)
                    System.err.println("OK"); // Print OK on stderr
                } catch (Exception e) {
                    log.error("Exception occured during compilation!", e);
                    System.err.println("KO"); // Print KO on stderr if a problem occured
                }
            }
        }
    }

    /**
     * Prints help message with this options.
     */
    private void printHelpMessage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(128);
        formatter.printHelp(String.format("java -jar %s.jar -%s | %s %s",
              NAME, HELP, INPUT, OUTPUT), options);
    }

    /**
     * Initialise the input compiler using the given input line.
     *
     * @throws Exception If one of the three required arguments is not provided.
     */
    private void initialise(CommandLine line) throws Exception {
        log.info("Initialisation");
        // Check that the arguments are there
        if (!line.hasOption(INPUT)) {
            throw new ParseException(String.format("Option %s is mandatory!", INPUT));
        } else if (!line.hasOption(OUTPUT)) {
            throw new ParseException(String.format("Option %s is mandatory!", OUTPUT));
        }
        // Get given files and check they exist
        inputFile = new File(line.getOptionValue(INPUT));
        checkArgument(inputFile.exists() && inputFile.isFile(), "File %s not found!", inputFile.getName());
        log.info("Input file set to {}", inputFile.getPath());

        outputFile = new File(line.getOptionValue(OUTPUT));
        if (!outputFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            outputFile.createNewFile();
        }
        checkArgument(outputFile.exists() && outputFile.isFile(), "File %s not created!", outputFile.getName());
        log.info("Output file set to {}", outputFile.getPath());

        log.info("Initialisation: done");
    }

    /**
     * Compiler Methods, this is where the MAGIC happens !!! \o/
     */
    private void compile() throws IOException {
        // Get abstract syntax tree
        log.info("Parsing input...");
        // Create the token stream
        final CharStream stream = CharStreams.fromFileName(inputFile.toString());
        final CommonTokenStream tokens = new CommonTokenStream(new GrammarLexer(stream));
        // Process source text into tree
        GrammarParser.RootContext tree = parse(tokens);
        log.info("Parsing input: done");
        // Process tree into program
        final Program program = parseProgram(tree);
        // Print NBC Code
        printNBCCode(program);

    }

    /**
     * Builds the abstract syntax tree from input.
     */
    private GrammarParser.RootContext parse(final CommonTokenStream tokens) throws ParseCancellationException {
        // Intialise parser
        final GrammarParser parser = new GrammarParser(tokens);
        // Set error listener to adoc implementation
        parser.removeErrorListeners();
        MyConsoleErrorListener errorListener = new MyConsoleErrorListener();
        parser.addErrorListener(errorListener);
        // Launch parsing
        GrammarParser.RootContext tree;
        try {
            tree = parser.root();
        } catch (RecognitionException e) {
            throw new IllegalArgumentException("Error while retrieving parsing tree!", e);
        }
        if (errorListener.errorHasBeenReported()) {
            throw new IllegalArgumentException("Error while parsing input!");
        }
        return tree;
    }

    private Program parseProgram(final GrammarParser.RootContext tree) {
        log.info("Building Symbol Table...");
        final PilsToNbcConverter converter = new PilsToNbcConverter(inputFile.getParentFile().toPath());
        final ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(converter, tree);
        log.info("Building symbol table: done");
        return converter.getProgram();
    }

    private void printNBCCode(final Program pilsProgram) throws FileNotFoundException {
        log.info("Generating NBC Code...");
        final Context context = new Context();
        final ProgramMapper mapper = context.mappers.getProgramMapper();

        final be.unamur.info.b314.compiler.nbc.program.Program nbcProgram = mapper.apply(pilsProgram);
        log.info("NBC Program:\n{}", nbcProgram);

        log.info("Printing program to file...");
        log.info("File path: [{}]", outputFile.getAbsolutePath());
        try (final PrintWriter writer = new PrintWriter(outputFile)) {
            writer.print(nbcProgram);
            writer.flush();
        }
        log.info("Printing NBC Code: done");
    }

    /**
     * @author James Ortiz - james.ortizvega@unamur.be
     */
    public static class MyConsoleErrorListener extends ConsoleErrorListener {

        private boolean errorReported;

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            errorReported = true;
            super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);
        }

        public boolean errorHasBeenReported() {
            return errorReported;
        }
    }
}