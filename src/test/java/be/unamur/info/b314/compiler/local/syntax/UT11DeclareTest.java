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
public class UT11DeclareTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/syntax/UT11/";
    private static final String OK = RESOURCE_DIR + "ok/";
    private static final String KO = RESOURCE_DIR + "ko/";

    @Test
    public void given_x_as_integer_instruction_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "x-as-integer.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_x_as_integer1_instruction_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "x-as-integer-1.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_x_as_integer2_instruction_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "x-as-integer-2.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_x_as_integer3_instruction_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "x-as-integer-3.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_x_as_square_instruction_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "x-as-square.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_x_as_square1_instruction_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "x-as-square-1.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_x_as_square2_instruction_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "x-as-integer-2.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_x_as_square3_instruction_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "x-as-square-3.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_x_as_boolean_instruction_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "x-as-boolean.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_x_as_boolean1_instruction_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "x-as-boolean-1.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_x_as_boolean2_instruction_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "x-as-boolean-2.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_x_as_boolean3_instruction_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "x-as-boolean-3.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isTrue();
    }
}
