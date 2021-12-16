package be.unamur.info.b314.compiler.local.semantics;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

public class UT12semanticsCommentsTest extends UnitTestTemplate {

    private static final String resourceDir = "/local/semantics/UT12/";
    //
    // Serie variables OK
    //
    @Test
    public void testvariables_id_operationAdd_ok() throws Exception{
        final File inputFile = new File(CompilerTestHelper.class.getResource(resourceDir + "id_operationAdd.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThat(result.getParser().instruction().exception, nullValue());
    }


    //
    // Serie variables KO
    //
    @Test
    public void testvariables_id_operationAdd_mistake_ko() throws Exception {
        final File inputFile = new File(CompilerTestHelper.class.getResource(resourceDir + "id_operationAdd_mistake.b314").toURI());

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThat(result.getParser().instruction().exception, nullValue());
    }


}