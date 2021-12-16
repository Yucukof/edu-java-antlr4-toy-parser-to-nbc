#!/usr/bin/env python2
#encoding: UTF-8

# Creates Java test files from existing directories and files. 
# Test files must be in src/test/resources/semantics/testseriename/testfile.b314
# Script must be launched from src/test/resources folder

import os

filePath=os.path.dirname(os.path.realpath(__file__))
semanticsDir=os.path.join(filePath, 'semantics') 

for serie in [x for x in os.listdir(semanticsDir) if not x.startswith('.')]:
    junitName = 'B314{}SemanticsTest'.format(serie)
    with open('{}/../java/be/unamur/info/b314/compiler/main/{}.java'.format(filePath, junitName),'w') as fout:
        b314filesDir=os.path.join(semanticsDir, serie)
        fout.write('package be.unamur.info.b314.compiler.main;\n\n')
        fout.write('import java.io.File;\n')
        fout.write('import org.apache.commons.io.FileUtils;\n')
        fout.write('import org.junit.Test;\n')
        fout.write('import static org.junit.Assert.*;\n')
        fout.write('import static org.hamcrest.Matchers.*;\n')
        fout.write('import org.junit.Rule;\n')
        fout.write('import org.junit.rules.TemporaryFolder;\n')
        fout.write('import org.junit.rules.TestRule;\n')
        fout.write('import org.junit.rules.TestWatcher;\n')
        fout.write('import org.junit.runner.Description;\n')
        fout.write('import org.slf4j.Logger;\n')
        fout.write('import org.slf4j.LoggerFactory;\n')
        fout.write('\n')
        fout.write('public class {} {{\n'.format(junitName))
        fout.write('\n')
        fout.write('    private static final Logger LOG = LoggerFactory.getLogger({}.class);\n'.format(junitName))
        fout.write('\n')
        fout.write('    @Rule\n')
        fout.write('    public TemporaryFolder testFolder = new TemporaryFolder(); // Create a temporary folder for outputs deleted after tests\n')
        fout.write('\n')
        fout.write('    @Rule\n')
        fout.write('    public TestRule watcher = new TestWatcher() { // Prints message on logger before each test\n')
        fout.write('        @Override\n')
        fout.write('        protected void starting(Description description) {\n')
        fout.write('            LOG.info(String.format("Starting test: %s()...",\n')
        fout.write('                    description.getMethodName()));\n')
        fout.write('        }\n')
        fout.write('    ;\n')
        fout.write('    };\n')
        fout.write('\n')
        for inputFile in [x for x in os.listdir(b314filesDir) if not x.startswith('.')]:
            fout.write('    @Test\n')
            fout.write('    public void test{}_{}() throws Exception{{\n'.format(serie, inputFile.replace('.b314', '')))
            fout.write('        File pcodeFile = testFolder.newFile();\n')
            fout.write('        CompilerTestHelper.launchCompilation("/semantics/{}/{}", pcodeFile, true, "{}: {}");\n'.format(serie, inputFile, serie, inputFile.replace('.b314', '')))
            fout.write('        LOG.debug("PCode is: {{}", FileUtils.readFileToString(pcodeFile));\n')
            fout.write('        InterpreterResult result;\n')
            fout.write('        // Turns: 1\n')
            fout.write('        LOG.debug("Starting interpretation with 1 turn");\n')
            with open('{}/{}'.format(b314filesDir,inputFile), 'r') as b314file:
                b314file.readline() # Skip first line
                input1 = [i for i in b314file.readline().strip().split(',') if not len(i) == 0]
                output1 = [o for o in b314file.readline().strip().split(',') if not len(o) == 0]
                fout.write('        result = PCodeInterpreter.getInterpreter().execute(pcodeFile, 1')
                for i in input1:
                    fout.write(', {}'.format(i));
                fout.write(');\n')
                fout.write('        assertThat("Interpreter exist status was not 0", result.getExitStatus(), equalTo(0));\n')
                fout.write('        assertThat("Wrong number of outputs, there was 1 turn", result.getOutLines(), hasSize(1));\n')
                fout.write('        assertThat(result.getOutLines(), contains("{}"'.format(output1[0]))
                for o in output1[1:]:
                    fout.write(', "{}"'.format(o));
                fout.write('));\n')
                fout.write('        // Turns: 3\n')
                fout.write('        LOG.debug("Starting interpretation with 3 turn");\n')
                input1 = [i for i in b314file.readline().strip().split(',') if not len(i) == 0]
                output1 = [o for o in b314file.readline().strip().split(',') if not len(o) == 0]
                fout.write('        result = PCodeInterpreter.getInterpreter().execute(pcodeFile, 3')
                for i in input1:
                    fout.write(', {}'.format(i));
                fout.write(');\n')
                fout.write('        assertThat("Interpreter exist status was not 0", result.getExitStatus(), equalTo(0));\n')
                fout.write('        assertThat("Wrong number of outputs, there was 1 turn", result.getOutLines(), hasSize(3));\n')
                fout.write('        assertThat(result.getOutLines(), contains("{}"'.format(output1[0]))
                for o in output1[1:]:
                    fout.write(', "{}"'.format(o));
                fout.write('));\n')
                fout.write('        // Turns 5\n')
                fout.write('        LOG.debug("Starting interpretation with 5 turn");\n')
                input1 = [i for i in b314file.readline().strip().split(',') if not len(i) == 0]
                output1 = [o for o in b314file.readline().strip().split(',') if not len(o) == 0]
                fout.write('        result = PCodeInterpreter.getInterpreter().execute(pcodeFile, 5')
                for i in input1:
                    fout.write(', {}'.format(i));
                fout.write(');\n')
                fout.write('        assertThat("Interpreter exist status was not 0", result.getExitStatus(), equalTo(0));\n')
                fout.write('        assertThat("Wrong number of outputs, there was 1 turn", result.getOutLines(), hasSize(5));\n')
                fout.write('        assertThat(result.getOutLines(), contains("{}"'.format(output1[0]))
                for o in output1[1:]:
                    fout.write(', "{}"'.format(o));
                fout.write('));\n')
            fout.write('    }\n\n')
        fout.write('}')
