package be.unamur.info.b314.compiler;

import be.unamur.info.b314.compiler.listeners.PilsToNbcConverter;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Hadrien BAILLY
 */
@Data
@Builder
@AllArgsConstructor
public class CompilerResult {
    private static final Logger log = LoggerFactory.getLogger(CompilerResult.class);

    private final String path;
    private final GrammarParser parser;
    private final PilsToNbcConverter listener;

    public CompilerResult(final File inputFile) throws IOException {
        this(getGrammarParser(inputFile), inputFile);
    }

    public CompilerResult(final GrammarParser parser, final File file) {
        this.path = file.getParent();
        this.parser = parser;
        this.parser.removeErrorListeners();
        this.parser.addErrorListener(new CompilerErrorListener());
        this.listener = new PilsToNbcConverter(file.getParentFile().toPath());
    }

    private static GrammarParser getGrammarParser(@NonNull final File inputFile) throws IOException {
        if (inputIsValid(inputFile)) {
            final CharStream stream = CharStreams.fromFileName(inputFile.toString());
            final CommonTokenStream tokens = new CommonTokenStream(new GrammarLexer(stream));
            return new GrammarParser(tokens);
        } else {
            throw new IllegalArgumentException("Input file is not valid!");
        }
    }

    private static boolean inputIsValid(final File inputFile) {
        return inputFile != null
              && inputFile.exists()
              && inputFile.isFile()
              && inputFile.canRead();
    }

    public void parseRoot() {
        final GrammarParser.RootContext rootContext = getRoot();
        if (!isFaulty()) {
            parse(rootContext);
        }
    }

    public GrammarParser.RootContext getRoot() {
        return parser.root();
    }

    public boolean isFaulty() {
        return ((CompilerResult.CompilerErrorListener) parser.getErrorListeners().get(0)).isInError();
    }

    public void parse(final ParserRuleContext context) {
        if (!isFaulty()) {
            parse(context, listener);
        }
    }

    public static void parse(final ParserRuleContext context, final PilsToNbcConverter listener) {
        new ParseTreeWalker().walk(listener, context);
    }

    public Map<String, Variable> getSymbols() {
        return listener.getContext().getSymbols().getVariables();
    }

    public void parseStrategy() {
        final GrammarParser.StrategyContext ctx = getStrategy();
        if (!isFaulty()) {
            parse(ctx);
        }
    }

    public GrammarParser.StrategyContext getStrategy() {
        return parser.strategy();
    }

    public void parseWorld(final PilsToNbcConverter listener) {
        final GrammarParser.WorldContext ctx = getWorld();
        if (!isFaulty()) {
            parse(ctx, listener);
        }
    }

    public GrammarParser.WorldContext getWorld() {
        return parser.world();
    }

    public void parseFctDecl() {
        final GrammarParser.FctDeclContext ctx = getFctDecl();
        if (!isFaulty()) {
            parse(ctx);
        }
    }

    public GrammarParser.FctDeclContext getFctDecl() {
        return parser.fctDecl();
    }

    public static class CompilerErrorListener extends ConsoleErrorListener {

        private boolean inError;

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            inError = true;
            log.error("{}", msg);
        }

        public boolean isInError() {
            return inError;
        }
    }
}
