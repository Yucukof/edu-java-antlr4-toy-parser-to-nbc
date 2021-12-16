package be.unamur.info.b314.compiler.pils.statements;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.PilsToNbcConverter;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidNumberOfIndexesException;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Hadrien BAILLY
 */
public class StatementSetTest extends UnitTestTemplate {

    @Test
    public void given_simple_set_statement_with_simple_integer_variable_when_parsing_then_expect_tree() {
        final GrammarParser parser = getParser("set y to 1");

        final PilsToNbcConverter converter = new PilsToNbcConverter(getWld());
        final Variable variable = Variable.builder()
              .name("y")
              .type(Type.INTEGER)
              .build();
        assertThat(variable.isScalar()).isTrue();
        converter.getContext().put(variable);


        final GrammarParser.InstructionContext ctx = parser.instruction();
        assertThat(ctx.exception).isNull();
        assertThat(ctx).isInstanceOf(GrammarParser.SetInstrContext.class);
        final GrammarParser.SetInstrContext set = (GrammarParser.SetInstrContext) ctx;
        converter.enterSetInstr(set);

    }

    @Test
    public void given_simple_set_statement_with_array_variable_without_index_when_parsing_then_expect_exception() {
        final GrammarParser parser = getParser("set y to 1");

        final PilsToNbcConverter converter = new PilsToNbcConverter(getWld());
        final Variable variable = Variable.builder()
              .name("y")
              .type(Type.INTEGER)
              .dimensions(Arrays.asList(2, 2))
              .build();
        assertThat(variable.isArray()).isTrue();
        converter.getContext().put(variable);

        final GrammarParser.InstructionContext ctx = parser.instruction();
        assertThat(ctx.exception).isNull();
        assertThat(ctx).isInstanceOf(GrammarParser.SetInstrContext.class);
        final GrammarParser.SetInstrContext set = (GrammarParser.SetInstrContext) ctx;
        assertThatThrownBy(() -> converter.enterSetInstr(set)).isInstanceOf(InvalidNumberOfIndexesException.class);

    }

    @Test
    public void given_operation_set_statement_with_simple_integer_variable_when_parsing_then_expect_tree() {
        final GrammarParser parser = getParser("set y to 1+1");

        final PilsToNbcConverter converter = new PilsToNbcConverter(getWld());
        final Variable variable = Variable.builder()
              .name("y")
              .type(Type.INTEGER)
              .build();
        assertThat(variable.isScalar()).isTrue();
        converter.getContext().put(variable);


        final GrammarParser.InstructionContext ctx = parser.instruction();
        assertThat(ctx.exception).isNull();
        assertThat(ctx).isInstanceOf(GrammarParser.SetInstrContext.class);
        final GrammarParser.SetInstrContext set = (GrammarParser.SetInstrContext) ctx;
        converter.enterSetInstr(set);

    }

    @Test
    public void given_simple_set_statement_with_arrat_integer_variable_when_parsing_then_expect_tree() {
        final GrammarParser parser = getParser("set y[1,1] to 1");

        final PilsToNbcConverter converter = new PilsToNbcConverter(getWld());
        final Variable variable = Variable.builder()
              .name("y")
              .type(Type.INTEGER)
              .dimensions(Arrays.asList(2, 2))
              .build();
        assertThat(variable.isArray()).isTrue();
        converter.getContext().put(variable);


        final GrammarParser.InstructionContext ctx = parser.instruction();
        assertThat(ctx.exception).isNull();
        assertThat(ctx).isInstanceOf(GrammarParser.SetInstrContext.class);
        final GrammarParser.SetInstrContext set = (GrammarParser.SetInstrContext) ctx;
        converter.enterSetInstr(set);

    }

}