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
public class UT7IfThenTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/syntax/UT7/";
    private static final String OK = RESOURCE_DIR + "ok/";
    private static final String KO = RESOURCE_DIR + "ko/";

    @Test
    public void given_if_then_instruction_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "if-then.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_if_then_else_instruction_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "if-then-else.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_if_else_instruction_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "if-else.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_if_with_compute_instruction_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "if-compute-then.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_if_then_compute_next_instruction_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "if-then-compute-next.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_if_else_compute_next_instruction_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "if-else-compute-next.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_two_if_instruction_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "if-if.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_missing_if_instruction_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "if-missing.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_if_instruction_without_expression_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "if-expression-missing.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_if_instruction_without_done_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "if-done-missing.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_if_instruction_with_then_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "if-empty-then.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_if_instruction_with_else_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "if-empty-else.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }
}
