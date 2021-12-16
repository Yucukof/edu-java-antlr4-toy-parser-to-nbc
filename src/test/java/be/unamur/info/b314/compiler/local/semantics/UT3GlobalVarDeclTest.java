package be.unamur.info.b314.compiler.local.semantics;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import be.unamur.info.b314.compiler.pils.exceptions.DuplicateNameException;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.pils.exceptions.ReservedNameException;
import be.unamur.info.b314.compiler.pils.exceptions.SymbolException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static be.unamur.info.b314.compiler.local.UnitTestTemplate.TestFileExtension.B314;
import static be.unamur.info.b314.compiler.local.UnitTestTemplate.TestStatus.KO;
import static be.unamur.info.b314.compiler.local.UnitTestTemplate.TestStatus.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UT3GlobalVarDeclTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/semantics/UT3/";

    @Test
    public void given_simple_global_var_decl_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "correct-global-declaration", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_simple_global_var_decl_with_same_name_as_local_var_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "same-name-as-local-variable", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_simple_global_var_decl_with_same_name_as_parameter_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "same-name-as-parameter", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_simple_global_var_used_in_a_function_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "used-in-a-function", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();
    }


    @Test
    public void given_two_global_var_with_same_name_then_expect_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "two-global-var-with-the-same-name", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThatThrownBy(result::parseStrategy)
              .isInstanceOf(SymbolException.class);
    }

    @Test
    public void given_a_global_var_with_same_name_as_a_function_then_expect_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "global-var-with-the-same-name-as-a-function", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThatThrownBy(result::parseStrategy)
              .isInstanceOf(DuplicateNameException.class);
    }

    @Test
    public void given_simple_global_var_with_the_same_name_as_arena_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "global-var-with-the-same-name-as-arena", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThatThrownBy(result::parseStrategy)
              .isInstanceOf(ReservedNameException.class);
    }

}
