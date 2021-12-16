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
public class UT13DefaultTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/syntax/UT13/";
    private static final String OK=RESOURCE_DIR+ "ok/";
    private static final String KO=RESOURCE_DIR+ "ko/";

    @Test
    public void given_default_clause_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "default.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseDefault();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_default_clause_with_declaration_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "default-declare.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseDefault();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_default_clause_with_two_declarations_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "default-declare-two.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseDefault();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_default_clause_with_two_declarations_and_instructions_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "default-declare-two-instruction-two.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseDefault();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_default_clause_with_two_instructions_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "default-instruction-two.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseDefault();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_default_clause_with_two_declare_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "default-two-declare.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseDefault();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_default_clause_with_two_do_when_compile_then_exception_expected() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "default-two-do.B314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseDefault();
        assertThat(result.isFaulty()).isTrue();
    }
}
