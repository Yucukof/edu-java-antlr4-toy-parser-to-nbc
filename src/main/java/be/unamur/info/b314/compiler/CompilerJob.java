package be.unamur.info.b314.compiler;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * @author Hadrien BAILLY
 */
@Data
@Builder
public class CompilerJob {
    private static final Logger log = LoggerFactory.getLogger(CompilerJob.class);

    private static final String NAME = "b314-compiler";
    private static final String HELP = "h";
    private static final String INPUT = "i";
    private static final String OUTPUT = "o";

    private Path directory;

    /**
     * The input B314 file.
     */
    private File inputFile;
    /**
     * The output PCode file.
     */
    private File outputFile;

    public CommandLine getCommandLine() {
        try {
            CommandLineParser parser = new DefaultParser();
            final Options options = getCommandLineOptions();
            final String[] arguments = getCommandLineArguments();
            return parser.parse(options, arguments);
        } catch (ParseException e) {
            log.error("Error while parsing command line!", e);
            displayCommandLine();
            throw new IllegalArgumentException("Could not parse compiler into command line!", e);
        }
    }

    public static Options getCommandLineOptions() {
        final Options options;
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
        return options;
    }

    private String[] getCommandLineArguments() {
        final String inputArg = "-i " + inputFile;
        final String outputArg = "-o " + outputFile;
        final String[] args = {inputArg, outputArg};
        log.trace("Arguments: [{}]", Arrays.toString(args));
        return args;
    }

    /**
     * Prints help message with this options.
     */
    private void displayCommandLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(128);
        formatter.printHelp(String.format("java -jar %s.jar -%s | %s %s",
              NAME, HELP, INPUT, OUTPUT), getCommandLineOptions());
    }

    public boolean createOutputFile() {
        if (outputFile != null) {
            if (!outputFile.exists()) {
                try {
                    return outputFile.createNewFile();
                } catch (IOException e) {
                    log.error("Failed to create output file!");
                }
            } else {
                log.warn("Output file already exists!");
                return false;
            }
        }
        log.error("Cannot create output file (file is null)!");
        return false;
    }

    public CompilerResult compile() throws IOException {
        if (isReady()) {
            return new CompilerResult(inputFile);
        } else {
            validate();
            throw new IllegalStateException("Parser is not ready!");
        }
    }

    public boolean isReady() {
        return inputIsReady()
              && outputIsReady();
    }

    public void validate() {
        if (inputFile == null) {
            log.error("Input file is null!");
        } else {
            log.debug(inputFile.getAbsolutePath());
            if (!inputFile.exists()) {
                log.error("Input file does not exist!");
            } else if (!inputFile.isFile()) {
                log.error("Input file is not a valid file!");
            } else if (!inputFile.canRead()) {
                log.error("Input file is not readable!");
            }
        }

        if (outputFile == null) {
            log.error("Output file is null!");
        } else {
            log.debug(outputFile.getAbsolutePath());
            if (!outputFile.exists()) {
                log.error("Output file does not exist!");
            } else if (!outputFile.isFile()) {
                log.error("Output file is not a valid file!");
            } else if (!outputFile.canWrite()) {
                log.error("Output file is not writable!");
            }
        }
    }

    public boolean inputIsReady() {
        return inputFile != null
              && inputFile.exists()
              && inputFile.isFile()
              && inputFile.canRead();
    }

    public boolean outputIsReady() {
        return outputFile != null
              && outputFile.exists()
              && outputFile.isFile()
              && outputFile.canWrite();
    }
}
