package be.unamur.info.b314.compiler.listeners;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Hadrien BAILLY
 */
public class PilsToNbcConverterTest extends UnitTestTemplate {

    @Test
    public void given_clause_when_with_redefinition_when_walking_then_expect_clause() {
        //@formatter:off
        final String input =
              "declare and retain " +
                    "x as boolean; " +
                    "when your turn " +
                    "when x " +
                    "declare local x as integer; " +
                    "do " +
                    "set x to 1 " +
                    "done " +
                    "by default do " +
                    "set x to true "+
                    "done";
        //@formatter:on

        final GrammarParser parser = getParser(input);
        final GrammarParser.StrategyContext ctx = parser.strategy();

        final ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new PilsToNbcConverter(getWld()), ctx);
    }

}