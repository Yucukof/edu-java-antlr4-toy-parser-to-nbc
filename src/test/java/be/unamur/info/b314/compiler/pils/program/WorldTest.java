package be.unamur.info.b314.compiler.pils.program;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.PilsToNbcConverter;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Anthony DI STASIO
 */
public class WorldTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/semantics/world/";

    @Test
    public void given_another_program_when_compile_then_no_exception() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, TestStatus.OK, "another", TestFileExtension.WLD);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseWorld(new PilsToNbcConverter(getWld()));
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_world_with_global_variables_and_statements_and_default_clause_parsing_then_expect_program() {
        final GrammarParser parser = getParser("declare and retain " +
                "x as integer; " +
                "i as integer; " +
                "j as integer; " +
                "arena as square[27, 27]; " +
                "by default do " +
                "compute 1 + 1 " +
                "compute 2 + 2 " +
                "set i to 1 " +
                "set j to 1 " +
                "set arena[1 ,1] to player " +
                "set arena[22 ,14] to ennemi " +
                "set arena[8 ,12] to zombie " +
                "set arena[4 ,7] to dirt " +
                "set arena[4 ,4] to radio " +
                "set arena[4 ,5] to radar " +
                "set arena[12 ,7] to soda " +
                "set arena[12 ,8] to map " +
                "set arena[25 ,25] to graal " +
                "next move east " +
                "next use soda " +
                "if 1=1 then compute 1 done " +
                "while i > 2 do compute 1+1 done " +
                "done"
        );

        final GrammarParser.WorldContext ctx = parser.world();

        final PilsToNbcConverter converter = new PilsToNbcConverter(getWld());
        final ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(converter, ctx);

        final Program program = converter.getProgram();
        assertThat(program).isNotNull().isInstanceOf(World.class);

        final World world = (World) program;

        assertThat(world.getDeclarations()).hasSize(4);
        assertThat(world.getStatements()).hasSize(0);
        assertThat(world.getClauseDefault().getDeclarations()).hasSize(0);
        assertThat(world.getClauseDefault().getStatements()).hasSize(17);
    }
}
