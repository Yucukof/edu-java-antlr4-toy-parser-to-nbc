package be.unamur.info.b314.compiler.local.syntax;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class UT12syntaxeCommentsTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/syntax/UT12/";
    private static final String OK = RESOURCE_DIR+ "ok/";
    private static final String KO = RESOURCE_DIR+ "ko/";

    @Test
    public void testcomments_comments_everywhere_ok() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "comments_everywhere.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void testcomments_empty_ok() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "empty.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void testcomments_empty_multiple_lines_and_tab_ok() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "empty_multiple_lines_and_tab.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void testcomments_at_least_one_instruction_in_default_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "at_least_one_instruction_in_default.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testcomments_missing_declare_bloc_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "missing_declare_bloc.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testcomments_missing_default_bloc_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "missing_default_bloc.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

}