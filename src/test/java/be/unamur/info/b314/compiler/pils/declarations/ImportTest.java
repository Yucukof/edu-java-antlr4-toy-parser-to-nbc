package be.unamur.info.b314.compiler.pils.declarations;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.PilsToNbcConverter;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.pils.imports.Import;
import be.unamur.info.b314.compiler.pils.program.Program;
import be.unamur.info.b314.compiler.pils.program.Strategy;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.PATH;

/**
 * @author Hadrien BAILLY
 */
public class ImportTest extends UnitTestTemplate {

    @Test
    public void given_valid_import_when_toString_then_expect_correct_String() {
        final Import i = Import.builder()
              .filename("test.wld")
              .build();

        assertThat(i.isValid()).isTrue();
        assertThat(i.toString()).isEqualTo("import test.wld");
    }

    @Test
    public void given_invalid_import_when_isValid_then_expect_false() {

        final Import i = Import.builder().build();
        assertThat(i.isValid()).isFalse();

        final Import i2 = i.toBuilder().filename("").build();
        assertThat(i2.isValid()).isFalse();
    }

    @Test
    public void given_simple_import_when_parse_then_expect_context() {
        final GrammarParser parser = getParser("declare and retain import i1.wld when your turn by default do skip done");

        final GrammarParser.RootContext ctx = parser.root();
        assertThat(ctx.strategy().impDecl().exception).isNull();

        final PilsToNbcConverter converter = new PilsToNbcConverter(getWld());
        final ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(converter,ctx);

        final Program program = converter.getProgram();
        assertThat(program).isNotNull().isInstanceOf(Strategy.class);

        final Strategy strategy = (Strategy) program;
        final Import world = strategy.getWorld();
        assertThat(world).isNotNull();
        assertThat(world.getFilename()).isEqualTo("i1.wld");
    }

    @Test
    public void given_duplicate_imports_when_parse_then_expect_exception() {
        final GrammarParser parser = getParser("declare and retain import i1.wld import i2.wld when your turn by default do skip done");

        assertThat(parser.strategy().exception).isNotNull();
    }

    @Test
    public void given_no_import_when_parse_then_expect_no_exception() {
        final GrammarParser parser = getParser("declare and retain when your turn by default do skip done");

        assertThat(parser.strategy().impDecl()).isNull();
    }


}