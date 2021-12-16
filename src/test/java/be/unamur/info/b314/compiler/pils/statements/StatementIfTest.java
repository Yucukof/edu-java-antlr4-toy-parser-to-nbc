package be.unamur.info.b314.compiler.pils.statements;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.listeners.Context;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class StatementIfTest extends UnitTestTemplate {

    private Context symbols;

    @Before
    public void before() {
        symbols = new Context();
    }

    @Test
    public void given_simple_if_when_from_context_then_expect_object() {
        final GrammarParser parser = getParser("if 1=1 then compute 1 else compute 0 done");

        final GrammarParser.InstructionContext ctx = parser.instruction();
        final StatementIf statementIf = StatementIf.from((GrammarParser.IfInstrContext) ctx, symbols);

        assertThat(statementIf.getGuard().isBoolean()).isTrue();
        assertThat(statementIf.getBranchTrue()).isNotNull();
        assertThat(statementIf.getBranchTrue().size()).isEqualTo(1);
        assertThat(statementIf.getBranchFalse()).isNotNull();
        assertThat(statementIf.getBranchFalse().size()).isEqualTo(1);
        assertThat(statementIf.isValid()).isTrue();
    }

    @Test
    public void given_if_with_several_instructions_when_from_context_then_expect_object() {
        final GrammarParser parser = getParser("if 1=1 then compute 1 compute 2 else next use fruits next move north done");

        final GrammarParser.InstructionContext ctx = parser.instruction();
        final StatementIf statementIf = StatementIf.from((GrammarParser.IfInstrContext) ctx, symbols);

        assertThat(statementIf.getGuard().isBoolean()).isTrue();
        assertThat(statementIf.getBranchTrue()).isNotNull();
        assertThat(statementIf.getBranchTrue().size()).isEqualTo(2);
        assertThat(statementIf.getBranchFalse()).isNotNull();
        assertThat(statementIf.getBranchFalse().size()).isEqualTo(2);
        assertThat(statementIf.isValid()).isTrue();
    }

    @Test
    public void given_simple_if_without_else_when_from_context_then_expect_object() {
        final GrammarParser parser = getParser("if 1=1 then compute 1 done");

        final GrammarParser.InstructionContext ctx = parser.instruction();
        final StatementIf statementIf = StatementIf.from((GrammarParser.IfInstrContext) ctx, symbols);

        assertThat(statementIf.getGuard().isBoolean()).isTrue();
        assertThat(statementIf.getBranchTrue()).isNotNull();
        assertThat(statementIf.getBranchTrue().size()).isEqualTo(1);
        assertThat(statementIf.getBranchFalse()).isNotNull();
        assertThat(statementIf.getBranchFalse().size()).isEqualTo(0);
        assertThat(statementIf.isValid()).isTrue();
    }

    @Test
    public void given_if_with_several_instructions_without_else_when_from_context_then_expect_object() {
        final GrammarParser parser = getParser("if 1=1 then compute 1 compute 2 done");

        final GrammarParser.InstructionContext ctx = parser.instruction();
        final StatementIf statementIf = StatementIf.from((GrammarParser.IfInstrContext) ctx, symbols);

        assertThat(statementIf.getGuard().isBoolean()).isTrue();
        assertThat(statementIf.getBranchTrue()).isNotNull();
        assertThat(statementIf.getBranchTrue().size()).isEqualTo(2);
        assertThat(statementIf.getBranchFalse()).isNotNull();
        assertThat(statementIf.getBranchFalse().size()).isEqualTo(0);
        assertThat(statementIf.isValid()).isTrue();
    }

}