package be.unamur.info.b314.compiler.local.syntax;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class UT10WhenTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/syntax/UT10/";
    private static final String OK =RESOURCE_DIR + "ok/";
    private static final String KO =RESOURCE_DIR + "ko/";

    @Test
    public void given_when_clause_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "when.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseWhen();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_when_with_declare_instruction_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "when-declare.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseWhen();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_when_clause_with_two_declarations_and_one_instruction_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "when-declare-twice.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseWhen();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_when_clause_with_two_declarations_and_two_instructions_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "when-declare-twice-and-instructions.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseWhen();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_when_clause_with_declarations_without_local_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "when-declare-without-local.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseWhen();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_when_clause_with_declaration_without_variable_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "when-declare-without-variable.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseWhen();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_when_clause_without_instruction_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "when-without-instruction.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseWhen();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_when_clause_with_two_expressions_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "when-with-two-expressions.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseWhen();
        assertThat(result.isFaulty()).isTrue();
    }
}
