package be.unamur.info.b314.compiler.local.syntax;


import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;


public class UT6NextTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/syntax/UT6/";
    private static final String OK = RESOURCE_DIR + "ok/";
    private static final String KO = RESOURCE_DIR + "ko/";

    //
    // Serie variables OK
    //
    @Test
    public void testnextaction_inside_a_function_ok() throws Exception{
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "inside_a_function.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().fctDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void testnextaction_inside_when_clause_ok() throws Exception{
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "inside_when_clause.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseWhen();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void testnextaction_inside_default_clause_ok() throws Exception{
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "inside_default_clause.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().clauseDefault();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void testnextaction_after_a_variable_and_a_function_ok() throws Exception{
        final File inputFile = new File(CompilerTestHelper.class.getResource(OK + "after_a_variable_and_a_function.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isFalse();
    }


    //
    // Serie variables KO
    //

    @Test
    public void testnextaction_before_declare_and_retain_ko() throws Exception{
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "before_declare_and_retain.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testnextaction_between_declare_and_retain_and_a_function_ko() throws Exception{
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "between_declare_and_retain_and_a_function.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isTrue();
    }


    @Test
    public void testnextaction_between_declare_and_retain_and_a_global_variable_ko() throws Exception{
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "between_declare_and_retain_and_a_global_variable.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isTrue();
    }


    @Test
    public void testnextaction_between_a_default_clause_and_a_when_clause_ko() throws Exception{
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "between_a_default_clause_and_a_when_clause.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().root();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testnextaction_action_without_next_ko() throws Exception{
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "action_without_next.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testnextaction_next_without_action_ko() throws Exception{
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "next_without_action.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testnextaction_incomplete_action_ko() throws Exception{
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "incomplete_action.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

    @Test
    public void testnextaction_wrong_action_ko() throws Exception{
        final File inputFile = new File(CompilerTestHelper.class.getResource(KO + "wrong_action.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().instruction();
        assertThat(result.isFaulty()).isTrue();
    }

}
