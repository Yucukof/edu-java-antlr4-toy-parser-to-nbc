package be.unamur.info.b314.compiler.local.syntax;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import org.junit.Test;

import java.io.File;

import static be.unamur.info.b314.compiler.local.UnitTestTemplate.TestStatus.KO;
import static be.unamur.info.b314.compiler.local.UnitTestTemplate.TestStatus.OK;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Anthony DI STASIO
 *       5. L'affectation (instruction set ExprG to ExprD)
 */
public class UT5SetTest extends UnitTestTemplate {
    private static final String RESOURCE_DIR = "/local/syntax/UT5/";

    @Test
    public void test_set_integer_reference_to_integer_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "set_integer_reference_to_integer", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void test_set_array_reference_to_integer_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "set_array_reference_to_integer", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void test_set_function_reference_to_integer_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "set_function_reference_to_integer", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_set_integer_reference_to_boolean_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "set_integer_reference_to_boolean", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void test_set_array_reference_to_boolean_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "set_array_reference_to_boolean", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void test_set_function_reference_to_boolean_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "set_function_reference_to_boolean", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_set_with_comment_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "set_with_comment", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void test_set_2d_array_to_integer_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "set_2d_array_to_integer", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void test_set_reference_to_itself_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "set_reference_to_itself", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void test_set_missing_set_word_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "missing_set_word", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_set_missing_to_word_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "missing_to_word", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_set_missing_reference_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "missing_reference", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_set_missing_expression_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "missing_expression", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_set_contains_wrong_chars_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "contains_wrong_chars", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_set_missing_square_bracket_in_array_reference_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "missing_square_bracket_in_array_reference", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_set_missing_parenthesis_in_function_reference_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "missing_parenthesis_in_function_reference", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_negative_integer_when_set_instruction_then_expect_ok() {
       final GrammarParser parser = getParser("set i to -1");
        assertThat(parser.instruction().exception).isNull();
    }

    @Test
    public void given_negative_sum_integer_when_set_instruction_then_expect_ok() {
        final GrammarParser parser = getParser("set i to 1--1");
        assertThat(parser.instruction().exception).isNull();
    }

    @Test
    public void given_latitude_when_set_instruction_then_expect_ok() {
        final GrammarParser parser = getParser("set i to latitude");
        assertThat(parser.instruction().exception).isNull();
    }

    @Test
    public void given_longitude_when_set_instruction_then_expect_ok() {
        final GrammarParser parser = getParser("set i to longitude");
        assertThat(parser.instruction().exception).isNull();
    }

    @Test
    public void given_gridsize_when_set_instruction_then_expect_ok() {
        final GrammarParser parser = getParser("set i to grid size");
        assertThat(parser.instruction().exception).isNull();
    }

    @Test
    public void given_life_when_set_instruction_then_expect_ok() {
        final GrammarParser parser = getParser("set i to life");
        assertThat(parser.instruction().exception).isNull();
    }

    @Test
    public void given_map_count_when_set_instruction_then_expect_ok() {
        final GrammarParser parser = getParser("set i to map count");
        assertThat(parser.instruction().exception).isNull();
    }

    @Test
    public void given_ammo_count_when_set_instruction_then_expect_ok() {
        final GrammarParser parser = getParser("set i to ammo count");
        assertThat(parser.instruction().exception).isNull();
    }

    @Test
    public void given_radio_count_when_set_instruction_then_expect_ok() {
        final GrammarParser parser = getParser("set i to radio count");
        assertThat(parser.instruction().exception).isNull();
    }

    @Test
    public void given_fruits_count_when_set_instruction_then_expect_ok() {
        final GrammarParser parser = getParser("set i to fruits count");
        assertThat(parser.instruction().exception).isNull();
    }

    @Test
    public void given_soda_count_when_set_instruction_then_expect_ok() {
        final GrammarParser parser = getParser("set i to soda count");
        assertThat(parser.instruction().exception).isNull();
    }
}
