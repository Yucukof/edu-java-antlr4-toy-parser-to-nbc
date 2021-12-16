package be.unamur.info.b314.compiler.local.syntax;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class UT3GlobalVarDeclTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/syntax/UT3/";
    private static final String OK = RESOURCE_DIR + "ok/";
    private static final String KO = RESOURCE_DIR + "ko/";


    //
    // Serie variables OK
    //

    @Test
    public void testglobalvar_correct_declaration_ok() throws Exception {

        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "correct_declaration.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void testglobalvar_less_than_two_dimension_array_ok() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "less_than_two_dimension_array.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void testglobalvar_declared_after_a_function_ok() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "declared_after_a_function.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isFalse();
    }


    //
    // Serie variables KO
    //

    @Test
    public void testglobalvar_declared_before_declare_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "global_before_declare.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isTrue();

    }


    @Test
    public void testglobalvar_var_name_starts_by_a_number_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "var_name_starts_by_a_number.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testglobalvar_missing_end_of_declaration_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "missing_end_of_declaration.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testglobalvar_missing_type_and_as_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "missing_type_and_as.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testglobalvar_missing_as_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "missing_as.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testglobalvar_wrong_declaration_form_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "wrong_declaration_form.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testglobalvar_global_var_declared_inside_a_function_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "global_var_declared_inside_a_function.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().fctDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testglobalvar_non_existing_type_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "non_existing_type.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testglobalvar_case_sensitive_names_of_types_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "Case_sensitive_names_of_types.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isTrue();
    }


    @Test
    public void testglobalvar_wrong_separator_for_array_length_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "wrong_separator_for_array_length.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testglobalvar_wrong_separator_between_array_lengths_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "wrong_separator_between_array_lengths.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testglobalvar_length_of_array_is_not_a_number_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "length_of_array_is_not_a_number.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testglobalvar_missing_length_in_array_declaration_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "missing_length_in_array_declaration.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testglobalvar_more_than_two_dimension_array_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "more_than_two_dimension_array.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().varDecl();
        assertThat(result.isFaulty()).isTrue();
    }

}
