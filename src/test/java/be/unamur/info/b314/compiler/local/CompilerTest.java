package be.unamur.info.b314.compiler.local;

import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;

public class CompilerTest {
    private static final Logger log = LoggerFactory.getLogger(CompilerTest.class);

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder(); // Create a temporary folder for outputs deleted after tests

    @Rule
    public TestRule watcher = new TestWatcher() { // Prints message on logger before each test
        @Override
        protected void starting(Description description) {
            log.info(String.format("Starting test: %s()...",
                  description.getMethodName()));
        }
    };

    @Test
    public void given_another_program_when_launch_compilation_then_success() throws Exception {
        final String input = "/miscellaneous/another.b314";
        CompilerTestHelper.launchCompilation(input
              , Paths.get(new File(Objects.requireNonNull(CompilerTestHelper.class.getResource(input)).toURI()).getParent(),"test").toFile()
              , true
              , "miscellaneous: another.B314");
    }
}