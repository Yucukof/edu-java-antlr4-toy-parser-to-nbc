package be.unamur.info.b314.compiler.local.semantics;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import org.junit.Test;

import java.io.File;

import static be.unamur.info.b314.compiler.local.UnitTestTemplate.TestStatus.KO;
import static be.unamur.info.b314.compiler.local.UnitTestTemplate.TestStatus.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Hadrien BAILLY
 */
public class UT5SetTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/semantics/UT5/";

    @Test
    public void given_int_variable_when_set_to_int_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "declare-and-set-integer", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_boolean_variable_when_set_to_boolean_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "declare-and-set-boolean", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_square_variable_when_set_to_square_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "declare-and-set-square", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_boolean_variable_when_set_to_int_then_expect_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "declare-and-set-boolean-to-integer", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThatThrownBy(result::parseStrategy)
              .isInstanceOf(InvalidDataTypeException.class);
    }

    @Test
    public void given_boolean_variable_when_set_to_square_then_expect_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "declare-and-set-boolean-to-square", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThatThrownBy(result::parseStrategy)
              .isInstanceOf(InvalidDataTypeException.class);
    }

    @Test
    public void given_int_variable_when_set_to_boolean_then_expect_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "declare-and-set-integer-to-boolean", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThatThrownBy(result::parseStrategy)
              .isInstanceOf(InvalidDataTypeException.class);
    }

    @Test
    public void given_int_variable_when_set_to_square_then_expect_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "declare-and-set-integer-to-square", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThatThrownBy(result::parseStrategy)
              .isInstanceOf(InvalidDataTypeException.class);
    }

    @Test
    public void given_square_variable_when_set_to_int_then_expect_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "declare-and-set-square-to-integer", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThatThrownBy(result::parseStrategy)
              .isInstanceOf(InvalidDataTypeException.class);
    }

    @Test
    public void given_square_variable_when_set_to_boolean_then_expect_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "declare-and-set-square-to-boolean", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThatThrownBy(result::parseStrategy)
              .isInstanceOf(InvalidDataTypeException.class);
    }

    @Test
    public void given_square_variable_with_indexes_when_set_to_square_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "declare-and-set-with-index", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();

        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_square_variable_with_indexes_out_of_bounds_when_set_to_square_then_expect_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "declare-and-set-with-index-out-of-bounds", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());

//        assertThatThrownBy(result::parseStrategy)
//              .isInstanceOf(InvalidNumberOfIndexesException.class);
        // TODO 29/03/2021 [HBA]: Quand même essayer de détecter ce type d'erreur ?
    }
}
