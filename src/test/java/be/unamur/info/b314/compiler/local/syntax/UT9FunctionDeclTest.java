package be.unamur.info.b314.compiler.local.syntax;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class UT9FunctionDeclTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/syntax/UT9/";
    private static final String OK = RESOURCE_DIR + "ok/";
    private static final String KO = RESOURCE_DIR + "ko/";



    //
    // Serie fonctions OK
    //
    @Test
    public void testfunctionDecl_correct_declaration_ok() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "correct_declaration.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().fctDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void testfunctionDecl_declared_after_another_function_ok() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "declared_after_another_function.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isFalse();
    }

    //
    // Serie fonctions KO
    //

    @Test
    public void testfunctionDecl_declared_before_declare_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "declared_before_declare.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testfunctionDecl_function_name_starts_by_a_number_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "function_name_starts_by_a_number.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().fctDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testfunctionDecl_missing_end_of_declaration_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "missing_end_of_declaration.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().fctDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testfunctionDecl_missing_as_and_function_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "missing_as_and_function.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().fctDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testfunctionDecl_missing_as_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "missing_as.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().fctDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testfunctionDecl_wrong_declaration_form_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "wrong_declaration_form.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().fctDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testfunctionDecl_function_declared_inside_another_function_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "function_declared_inside_another_function.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testfunctionDecl_non_existing_return_type_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "non_existing_return_type.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().fctDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testfunctionDecl_case_sensitive_name_of_return_type_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "case_sensitive_name_of_return_type.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().fctDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testfunctionDecl_function_without_parameter_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "function_without_parameter.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().fctDecl();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testfunctionDecl_empty_function_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "empty_function.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().fctDecl();
        assertThat(result.isFaulty()).isTrue();
    }

}
