package be.unamur.info.b314.compiler.local.semantics;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import org.junit.Test;

import java.io.File;
import java.util.Map;

import static be.unamur.info.b314.compiler.local.UnitTestTemplate.TestStatus.KO;
import static be.unamur.info.b314.compiler.local.UnitTestTemplate.TestStatus.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Hadrien BAILLY
 */
public class UT14OperatorTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/semantics/UT14/";

    @Test
    public void given_int_variables_when_int_operation_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "operation-integer", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();

        final Map<String, Variable> symbols = result.getListener().getContext().getSymbols().getVariables();
        assertThat(symbols).isNotNull();
        assertThat(symbols.size()).isEqualTo(13);

        final Variable arena = result.getSymbols().get("arena");
        assertThat(arena).isNotNull();
        assertThat(arena.getType()).isEqualTo(Type.SQUARE);

        final Variable intVariable = symbols.get("int");
        assertThat(intVariable).isNotNull();
        assertThat(intVariable.getType()).isEqualTo(Type.INTEGER);

        final Variable boolVariable = symbols.get("bool");
        assertThat(boolVariable).isNotNull();
        assertThat(boolVariable.getType()).isEqualTo(Type.BOOLEAN);
    }

    @Test
    public void given_bool_variables_when_bool_operation_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "operation-boolean", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();

        final Map<String, Variable> symbols = result.getListener().getContext().getSymbols().getVariables();
        assertThat(symbols).isNotNull();
        assertThat(symbols.size()).isEqualTo(14);

        final Variable arena = result.getSymbols().get("arena");
        assertThat(arena).isNotNull();
        assertThat(arena.getType()).isEqualTo(Type.SQUARE);

        final Variable var1 = symbols.get("test1");
        assertThat(var1).isNotNull();
        assertThat(var1.getType()).isEqualTo(Type.BOOLEAN);

        final Variable var2 = symbols.get("test2");
        assertThat(var2).isNotNull();
        assertThat(var2.getType()).isEqualTo(Type.BOOLEAN);

        final Variable var3 = symbols.get("test3");
        assertThat(var3).isNotNull();
        assertThat(var3.getType()).isEqualTo(Type.BOOLEAN);
    }

    @Test
    public void given_square_variables_when_square_operation_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "operation-square", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();

        final Map<String, Variable> symbols = result.getListener().getContext().getSymbols().getVariables();
        assertThat(symbols).isNotNull();
        assertThat(symbols.size()).isEqualTo(12);

        final Variable arena = result.getSymbols().get("arena");
        assertThat(arena).isNotNull();
        assertThat(arena.getType()).isEqualTo(Type.SQUARE);
        final Variable var1 = symbols.get("test");
        assertThat(var1).isNotNull();
        assertThat(var1.getType()).isEqualTo(Type.BOOLEAN);

    }

    @Test
    public void given_various_variables_when_square_operation_then_expect_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "operation-square-invalid", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThatThrownBy(result::parseStrategy).isInstanceOf(InvalidDataTypeException.class);
    }

    @Test
    public void given_various_variables_when_boolean_operation_then_expect_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "operation-boolean-invalid", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThatThrownBy(result::parseStrategy).isInstanceOf(InvalidDataTypeException.class);
    }

    @Test
    public void given_various_variables_when_integer_operation_then_expect_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "operation-integer-invalid", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThatThrownBy(result::parseStrategy).isInstanceOf(InvalidDataTypeException.class);
    }
}
