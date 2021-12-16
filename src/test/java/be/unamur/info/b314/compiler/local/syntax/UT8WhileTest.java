package be.unamur.info.b314.compiler.local.syntax;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class UT8WhileTest extends UnitTestTemplate {
    private static final String RESOURCE_DIR = "/local/syntax/UT8/";
    private static final String RESOURCE_DIR_OK = RESOURCE_DIR + "ok/";
    private static final String RESOURCE_DIR_KO = RESOURCE_DIR + "ko/";
    public static final String EXTENSION = ".B314";

    @Test
    public void test_while_contains_one_instruction_ok() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class
                .getResource(RESOURCE_DIR_OK + "contains_one_instruction" + EXTENSION).toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void test_while_contains_several_instructions_ok() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class
                .getResource(RESOURCE_DIR_OK + "contains_several_instructions" + EXTENSION).toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void test_while_contains_one_instruction_and_comments_ok() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class
                .getResource(RESOURCE_DIR_OK + "contains_one_instruction_and_comments" + EXTENSION).toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void test_while_missing_while_word_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class
                .getResource(RESOURCE_DIR_KO + "missing_while_word" + EXTENSION).toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_while_missing_do_word_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class
                .getResource(RESOURCE_DIR_KO + "missing_do_word" + EXTENSION).toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_while_missing_done_word_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class
                .getResource(RESOURCE_DIR_KO + "missing_done_word" + EXTENSION).toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_while_missing_expression_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class
                .getResource(RESOURCE_DIR_KO + "missing_expression" + EXTENSION).toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_while_missing_instruction_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class
                .getResource(RESOURCE_DIR_KO + "missing_instruction" + EXTENSION).toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_while_contains_typo_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class
                .getResource(RESOURCE_DIR_KO + "contains_typo" + EXTENSION).toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }
}
