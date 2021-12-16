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
public class UT1declareTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/syntax/UT1/";
    private static final String OK = RESOURCE_DIR + "ok/";
    private static final String KO = RESOURCE_DIR + "ko/";

    @Test
    public void given_skip_and_no_comment_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "normal.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_skip_and_comments_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "comments.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_skip_and_keywords_in_comments_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "declare-in-comments.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_declare_and_your_turn_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "declare-without-default-with-your-turn.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_missing_retain_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "declare-without-retain.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_declare_and_missing_instruction_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "declare-without-instruction.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_declare_and_missing_default_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "declare-without-default.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_declare_and_missing_default_and_instruction_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "declare-without-default-with-instruction.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isTrue();
    }
}
