package be.unamur.info.b314.compiler.main;

import be.unamur.info.b314.compiler.CompilerJob;
import be.unamur.info.b314.compiler.CompilerResult;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;

public class CompilerTestHelper {

    public static final String RESOURCE_DIR = "/miscellaneous/";

    public static CompilerResult getCompilerResult(final File inputFile, final File outputFile) throws IOException {
        final CompilerJob compilerJob = CompilerJob.builder()
              .directory(inputFile.getParentFile().toPath())
              .inputFile(inputFile.getAbsoluteFile())
              .outputFile(outputFile.getAbsoluteFile())
              .build();

        return compilerJob.compile();
    }

    @Test
    public void given_another_wld_when_launchCompilation_then_expect_ok() throws URISyntaxException {
        launchCompilation(RESOURCE_DIR + "another.b314", new File("another.nbc"), true, "Another world");
    }

    /**
     * Launch compilation and check OK/KO output according to the given parameters.
     *
     * @param input      The name of the input B314 file (in src/test/resources).
     * @param ok         True if the compiler should print 'OK' on stderr for the given
     *                   files.
     * @param message    Message to print if the test fails.
     * @param outputFile Output file where PCode is written
     * @throws URISyntaxException If the given file does not exist in
     *                            src/test/resources.
     */
    public static void launchCompilation(String input, File outputFile, boolean ok, String message) throws URISyntaxException {
        File inputFile = new File(Objects.requireNonNull(CompilerTestHelper.class.getResource(input)).toURI());
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
        // Launch main method
        Main.main(new String[]{"-i", inputFile.getAbsolutePath(), "-o", outputFile.getAbsolutePath()});
        String expected;
        if (ok) {
            expected = String.format("OK%n"); // Using format to prevent EOL compatibility issues
        } else {
            expected = String.format("KO%n");
        }
        assertThat(message, errContent.toString(), endsWith(expected)); // Check that the output ends with OK/KO
    }

}
