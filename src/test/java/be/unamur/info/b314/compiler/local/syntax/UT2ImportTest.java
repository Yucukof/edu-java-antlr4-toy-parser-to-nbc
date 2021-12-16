package be.unamur.info.b314.compiler.local.syntax;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import org.junit.Test;

import java.io.File;

import static be.unamur.info.b314.compiler.local.UnitTestTemplate.TestStatus.KO;
import static be.unamur.info.b314.compiler.local.UnitTestTemplate.TestStatus.OK;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * La déclaration de l'instruction : 'import FileDecl'.
 *
 * @author Anthony DI STASIO
 */
public class UT2ImportTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/syntax/UT2/";

    @Test
    public void test_import_filename_contains_only_uppercase_letters_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "filename_contains_only_uppercase_letters", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().impDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void test_import_filename_contains_only_lowercase_letters_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "filename_contains_only_lowercase_letters", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().impDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void test_import_filename_contains_lowercase_and_uppercase_letters_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "filename_contains_lowercase_and_uppercase_letters", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().impDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void test_import_filename_contains_letters_and_digits_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "filename_contains_letters_and_digits", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().impDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void test_import_filename_is_empty_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "filename_is_empty", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().impDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_import_missing_import_word_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "missing_import_word", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().impDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_import_missing_file_extension_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "missing_file_extension", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().impDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_import_wrong_file_extension_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "wrong_file_extension", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().impDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_import_malformed_syntax_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "malformed_syntax", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().impDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_import_filename_starts_with_digits_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "filename_starts_with_digits", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().impDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test // TODO: @ is ok
    public void test_import_filename_contains_special_characters_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "filename_contains_special_characters", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().impDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void test_import_word_is_malformed_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "import_word_is_malformed", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().impDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_more_than_one_import_when_test_rule_root_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "more-than-one-import", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().impDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void given_zero_import_when_test_rule_root_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "zero-import", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().impDecl();
        assertThat(result.isFaulty()).isTrue();
    }
}
