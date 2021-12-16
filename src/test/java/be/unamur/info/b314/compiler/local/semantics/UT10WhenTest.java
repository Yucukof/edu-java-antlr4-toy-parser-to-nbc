package be.unamur.info.b314.compiler.local.semantics;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.pils.clauses.ClauseWhen;
import be.unamur.info.b314.compiler.listeners.Context;
import org.junit.Test;

import java.io.File;

import static be.unamur.info.b314.compiler.local.UnitTestTemplate.TestStatus.OK;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class UT10WhenTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/semantics/UT10/";

    @Test
    public void given_simple_program_when_compile_then_no_exception() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "when", TestFileExtension.B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        final GrammarParser.RootContext ctx = result.getParser().root();
        result.parse(ctx);
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_simple_program_when_compile_then_expect_statements() {
        final GrammarParser parser = getParser("when 1+1 do if true then compute 2*2 else compute 3*3 done done");

        final GrammarParser.ClauseWhenContext ctx = parser.clauseWhen();
        final ClauseWhen when = ClauseWhen.from(ctx, new Context());

        assertThat(when.getGuard()).isNotNull();
        assertThat(when.getStatements()).isNotNull();

    }
}
