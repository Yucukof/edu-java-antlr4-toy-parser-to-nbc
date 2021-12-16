package be.unamur.info.b314.compiler.local;

import be.unamur.info.b314.compiler.GrammarLexer;
import be.unamur.info.b314.compiler.GrammarParser;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
public class UnitTestTemplate {

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

    protected static GrammarParser getParser(final String string) {
        return new GrammarParser(new CommonTokenStream(new GrammarLexer(CharStreams.fromString(string))));
    }

    public File getFile(final String directory, final TestStatus status, final String file, final TestFileExtension fileExtension) throws URISyntaxException {
        return new File(Objects.requireNonNull(UnitTestTemplate.class.getResource(directory + status.getPath() + file + fileExtension.getExtension())).toURI());
    }

    public Path getWld() {
        try {
            return Paths.get("src", "test", "resources", "wld");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public enum TestStatus {
        OK("ok/"),
        KO("ko/");

        private final String path;

        TestStatus(final String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public enum TestFileExtension {
        B314(".b314"),
        WLD(".wld");

        private final String extension;

        TestFileExtension(final String extension) {
            this.extension = extension;
        }

        public String getExtension() {
            return extension;
        }
    }
}
