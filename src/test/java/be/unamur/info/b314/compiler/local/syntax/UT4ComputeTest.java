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
public class UT4ComputeTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/syntax/UT4/";
    private static final String OK = RESOURCE_DIR + "ok/";
    private static final String KO = RESOURCE_DIR + "ko/";

    @Test
    public void given_compute_instruction_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "compute.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_if_instruction_and_compute_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "if-compute.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_program_and_compute_in_comments_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "program-compute.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_while_instruction_and_compute_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "while-compute.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_world_and_compute_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "world-compute.wld").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_compute_with_error_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "compute-declare.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_compute_with_comment_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "compute-comment.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_compute_with_typo_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "compute-with-typo.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_compute_with_typo_2_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "compute-with-typo-2.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }
}
