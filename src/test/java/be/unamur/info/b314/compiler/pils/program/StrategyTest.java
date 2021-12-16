package be.unamur.info.b314.compiler.pils.program;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.PilsToNbcConverter;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.pils.imports.Import;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class StrategyTest extends UnitTestTemplate {

    @Test
    public void given_strategy_with_import_when_parsing_then_expect_program() {
        final GrammarParser parser = getParser("declare and retain import i1.wld when your turn by default do skip done");

        final GrammarParser.StrategyContext ctx = parser.strategy();
        assertThat(ctx.impDecl().exception).isNull();

        final PilsToNbcConverter converter = new PilsToNbcConverter(getWld());
        final ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(converter, ctx);

        final Program program = converter.getProgram();
        assertThat(program).isNotNull().isInstanceOf(Strategy.class);

        final Strategy strategy = (Strategy) program;
        final Import world = strategy.getWorld();
        assertThat(world).isNotNull();
        assertThat(world.getFilename()).isEqualTo("i1.wld");
    }

    @Test
    public void given_strategy_without_import_when_parsing_then_expect_program() {
        final GrammarParser parser = getParser("declare and retain when your turn by default do skip done");

        final GrammarParser.StrategyContext ctx = parser.strategy();
        assertThat(ctx.impDecl()).isNull();

        final PilsToNbcConverter converter = new PilsToNbcConverter(getWld());
        final ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(converter, ctx);

        final Program program = converter.getProgram();
        assertThat(program).isNotNull().isInstanceOf(Strategy.class);

        final Strategy strategy = (Strategy) program;
        final Import world = strategy.getWorld();
        assertThat(world).isNull();
    }

}