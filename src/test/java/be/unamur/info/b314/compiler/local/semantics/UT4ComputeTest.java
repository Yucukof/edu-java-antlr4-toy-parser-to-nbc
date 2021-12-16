package be.unamur.info.b314.compiler.local.semantics;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import org.junit.Test;

import java.io.File;

import static be.unamur.info.b314.compiler.local.UnitTestTemplate.TestStatus.OK;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class UT4ComputeTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/semantics/UT4/";

    @Test
    public void given_boolean_operation_when_compute_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "compute-boolean", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();

        assertThat(result.getSymbols().size()).isEqualTo(12);

        final Variable arena = result.getSymbols().get("arena");
        assertThat(arena).isNotNull();
        assertThat(arena.getType()).isEqualTo(Type.SQUARE);

        final Variable variable = result.getSymbols().get("test");
        assertThat(variable).isNotNull();
        assertThat(variable.getType()).isEqualTo(Type.BOOLEAN);
    }

    @Test
    public void given_integer_operation_when_compute_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "compute-integer", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();

        assertThat(result.getSymbols().size()).isEqualTo(12);

        final Variable arena = result.getSymbols().get("arena");
        assertThat(arena).isNotNull();
        assertThat(arena.getType()).isEqualTo(Type.SQUARE);

        final Variable variable = result.getSymbols().get("test");
        assertThat(variable).isNotNull();
        assertThat(variable.getType()).isEqualTo(Type.INTEGER);
    }

    @Test
    public void given_square_operation_when_compute_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "compute-square", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();

        assertThat(result.getSymbols().size()).isEqualTo(12);

        final Variable arena = result.getSymbols().get("arena");
        assertThat(arena).isNotNull();
        assertThat(arena.getType()).isEqualTo(Type.SQUARE);

        final Variable variable = result.getSymbols().get("test");
        assertThat(variable).isNotNull();
        assertThat(variable.getType()).isEqualTo(Type.SQUARE);

    }
}
