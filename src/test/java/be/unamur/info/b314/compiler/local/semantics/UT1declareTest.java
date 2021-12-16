package be.unamur.info.b314.compiler.local.semantics;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.listeners.PilsToNbcConverter;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import org.junit.Test;

import java.io.File;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class UT1declareTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/semantics/UT1/";
    private static final String OK = RESOURCE_DIR + "ok/";
    private static final String KO = RESOURCE_DIR + "ko/";

    @Test
    public void given_simple_program_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(Objects.requireNonNull(CompilerTestHelper.class.getResource(OK + "another.B314")).toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_another_program_when_compile_then_no_exception() throws Exception {
        final File inputFile = new File(Objects.requireNonNull(CompilerTestHelper.class.getResource(OK + "another.wld")).toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseWorld(new PilsToNbcConverter(testFolder.newFile().toPath()));
        assertThat(result.isFaulty()).isFalse();
    }
}
