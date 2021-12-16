package be.unamur.info.b314.compiler.local.semantics;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.pils.statements.StatementWhile;
import be.unamur.info.b314.compiler.listeners.Context;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Anthony DI STASIO
 */
public class UT8WhileTest  extends UnitTestTemplate {
    private static final String RESOURCE_DIR = "/local/semantics/UT8/";

    @Test
    public void given_while_instruction_happy_path_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, TestStatus.OK, "nested-while", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_has_invalid_expression_type_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, TestStatus.KO, "wrong-expression-type", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());

        assertThatThrownBy(result::parseStrategy)
                .isInstanceOf(InvalidDataTypeException.class);
    }

    @Test
    public void given_simple_program_when_compile_then_expect_statements() {
        final GrammarParser parser = getParser("while 2 > 1 do compute 2+2 compute 3-2 done");

        final GrammarParser.WhileInstrContext ctx = (GrammarParser.WhileInstrContext) parser.instruction();
        final StatementWhile statementWhile = StatementWhile.from(ctx, new Context());

        assertThat(statementWhile.getGuard()).isNotNull();
        assertThat(statementWhile.isValid()).isTrue();
        assertThat(statementWhile.getStatements()).isNotNull();
        assertThat(statementWhile.getStatements().size()).isEqualTo(2);
    }

    @Test
    public void given_wrong_expression_when_compile_then_expect_not_valid() {
        final GrammarParser parser = getParser("while 1 do compute 2+2 compute 3-2 done");

        final GrammarParser.WhileInstrContext ctx = (GrammarParser.WhileInstrContext) parser.instruction();
        final StatementWhile statementWhile = StatementWhile.from(ctx, new Context());

        assertThat(statementWhile.isValid()).isFalse();
    }
}