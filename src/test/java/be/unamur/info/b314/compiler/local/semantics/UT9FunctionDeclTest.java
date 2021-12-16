package be.unamur.info.b314.compiler.local.semantics;

import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.PilsToNbcConverter;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import be.unamur.info.b314.compiler.pils.declarations.Function;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.exceptions.*;
import be.unamur.info.b314.compiler.pils.program.Strategy;
import be.unamur.info.b314.compiler.listeners.Context;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static be.unamur.info.b314.compiler.local.UnitTestTemplate.TestFileExtension.B314;
import static be.unamur.info.b314.compiler.local.UnitTestTemplate.TestStatus.KO;
import static be.unamur.info.b314.compiler.local.UnitTestTemplate.TestStatus.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Benoît DUVIVIER
 */
public class UT9FunctionDeclTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/semantics/UT9/";

    @Test
    public void given_simple_function_type_int_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "correct_declaration_int", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().fctDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_simple_function_type_void_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "correct_declaration_void", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().fctDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_simple_function_type_boolean_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "correct_declaration_boolean", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().fctDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_simple_function_type_square_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "correct_declaration_square", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getParser().fctDecl();
        assertThat(result.isFaulty()).isFalse();
    }

    @Test
    public void given_simple_function_with_a_local_var_with_the_same_name_but_inside_another_function_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "same-name-but-inside-another-function", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();
    }


    @Test
    public void given_simple_function_type_is_scalar_but_no_return_then_expect_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "scalar_type_no_return", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.getListener().setProgram(new Strategy());

        final GrammarParser.FctDeclContext ctx = result.getParser().fctDecl();
        final ParseTreeWalker walker = new ParseTreeWalker();

        assertThatThrownBy(() -> walker.walk(new PilsToNbcConverter(getWld()), ctx))
              .isInstanceOf(InvalidFunctionDeclarationException.class);
    }

    @Test
    public void given_simple_function_type_is_void_but_return_then_expect_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "void_type_but_return", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());

        final GrammarParser.FctDeclContext ctx = result.getParser().fctDecl();
        final ParseTreeWalker walker = new ParseTreeWalker();

        assertThatThrownBy(() -> walker.walk(new PilsToNbcConverter(getWld()), ctx))
              .isInstanceOf(InvalidFunctionDeclarationException.class);
    }

    @Test
    public void given_simple_function_wrong_return_type_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "return_int_but_expression_is_boolean", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());

        final GrammarParser.FctDeclContext ctx = result.getParser().fctDecl();
        final ParseTreeWalker walker = new ParseTreeWalker();

        assertThatThrownBy(() -> walker.walk(new PilsToNbcConverter(getWld()), ctx))
              .isInstanceOf(InvalidFunctionDeclarationException.class);
    }

    @Test
    public void given_simple_function_return_a_not_defined_symbol_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "return_a_not_defined_symbol", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());

        final GrammarParser.FctDeclContext ctx = result.getParser().fctDecl();
        final ParseTreeWalker walker = new ParseTreeWalker();

        assertThatThrownBy(() -> walker.walk(new PilsToNbcConverter(getWld()), ctx))
              .isInstanceOf(VariableNotFoundException.class);
    }

    @Test
    @Ignore // TODO 09/04/2021 [HBA]: Currently no offense when function and variable have the same name. To investigate
    public void given_simple_function_name_already_taken_by_a_global_variable_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "name_already_taken_by_a_global_variable", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());

        final GrammarParser.StrategyContext ctx = result.getParser().strategy();
        final ParseTreeWalker walker = new ParseTreeWalker();

        assertThatThrownBy(() -> walker.walk(new PilsToNbcConverter(getWld()), ctx))
              .isInstanceOf(InvalidFunctionDeclarationException.class);
    }

    @Test
    public void given_simple_function_name_already_taken_by_another_function_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "name_already_taken_by_another_function", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());

        final GrammarParser.StrategyContext ctx = result.getParser().strategy();
        final ParseTreeWalker walker = new ParseTreeWalker();

        assertThatThrownBy(() -> walker.walk(new PilsToNbcConverter(getWld()), ctx))
              .isInstanceOf(DuplicateNameException.class);
    }

    @Test
    public void given_simple_function_with_two_local_var_with_the_same_name_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "two-local-var-with-same-name", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());

        final GrammarParser.FctDeclContext ctx = result.getParser().fctDecl();
        final ParseTreeWalker walker = new ParseTreeWalker();

        assertThatThrownBy(() -> walker.walk(new PilsToNbcConverter(getWld()), ctx))
              .isInstanceOf(DuplicateNameException.class);
    }

    @Ignore //todo fix
    @Test
    public void given_simple_function_with_a_local_var_with_the_same_name_as_arena_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "same-name-as-arena", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());

        final GrammarParser.StrategyContext ctx = result.getParser().strategy();
        final ParseTreeWalker walker = new ParseTreeWalker();

        assertThatThrownBy(() -> walker.walk(new PilsToNbcConverter(getWld()), ctx))
              .isInstanceOf(InvalidFunctionDeclarationException.class);
    }

    @Ignore //todo fix
    @Test
    public void given_simple_function_with_the_same_name_as_arena_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "function-with-the-same-name-as-arena", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());

        final GrammarParser.StrategyContext ctx = result.getParser().strategy();
        final ParseTreeWalker walker = new ParseTreeWalker();

        assertThatThrownBy(() -> walker.walk(new PilsToNbcConverter(getWld()), ctx))
              .isInstanceOf(InvalidFunctionDeclarationException.class);
    }

    @Ignore //todo fix
    @Test
    public void given_simple_function_with_a_local_var_with_the_same_name_as_the_function_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "same-name-as-function", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThatThrownBy(result::parseFctDecl)
              .isInstanceOf(IllegalArgumentException.class);
    }

    @Ignore //todo fix
    @Test
    public void given_simple_function_with_a_parameter_with_the_same_name_as_the_function_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "parameter-with-same-name-as-function", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThatThrownBy(result::parseFctDecl)
              .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    public void given_simple_function_with_parameters_with_the_same_name_inside_the_function_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "parameter-with-same-name-inside-a-function", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThatThrownBy(result::parseFctDecl)
              .isInstanceOf(SymbolException.class);
    }

    @Test
    public void given_simple_function_with_a_local_var_and_a_parameter_with_the_same_name_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "parameter-with-same-name-as-a-local-var", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        assertThatThrownBy(result::parseFctDecl)
              .isInstanceOf(SymbolException.class);
    }

    @Test
    public void given_simple_program_when_compile_then_expect_statements() {
        String functionAsString = "functionTest1 as function(a as integer): integer  \n" +
              "        declare local locvariable as integer;\n" +
              "                      x as integer;\n" +
              "\n" +
              "        do set x to 1\n" +
              "\n" +
              "        return locvariable \n" +
              "    done";
        final GrammarParser parser = getParser(functionAsString);

        final GrammarParser.FctDeclContext ctx = parser.fctDecl();

        Context symbols = new Context();
        symbols.put(Variable.builder().name("x").type(Type.INTEGER).build());
        final Function function = Function.from(ctx, symbols);

        assertThat(function.getStatements()).isNotNull();
        assertThat(function.isValid()).isTrue();
    }

    /*
     * appels de fonction
     */

    @Test
    public void given_simple_functioncall_then_expect_ok() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, OK, "correct_functioncall", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());
        result.parseStrategy();
        assertThat(result.isFaulty()).isFalse();

    }

    @Test
    public void given_simple_functioncall_wrong_number_of_parameters_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "wrong_number_of_parameters", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());

        final GrammarParser.StrategyContext ctx = result.getParser().strategy();
        final ParseTreeWalker walker = new ParseTreeWalker();

        assertThatThrownBy(() -> walker.walk(new PilsToNbcConverter(getWld()), ctx))
              .isInstanceOf(InvalidFunctionCallException.class);
    }


    @Test
    public void given_simple_functioncall_with_wrong_type_of_parameter_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "wrong_type_of_formal_parameters", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());

        final GrammarParser.StrategyContext ctx = result.getParser().strategy();
        final ParseTreeWalker walker = new ParseTreeWalker();

        assertThatThrownBy(() -> walker.walk(new PilsToNbcConverter(getWld()), ctx))
              .isInstanceOf(InvalidFunctionCallException.class);
    }

    @Test
    public void given_simple_functioncall_of_a_non_defined_function_ko() throws Exception {
        final File inputFile = getFile(RESOURCE_DIR, KO, "function-not-defined-in-a-functioncall", B314);

        final CompilerResult result = CompilerTestHelper.getCompilerResult(inputFile, testFolder.newFile());

        final GrammarParser.StrategyContext ctx = result.getParser().strategy();
        final ParseTreeWalker walker = new ParseTreeWalker();

        assertThatThrownBy(() -> walker.walk(new PilsToNbcConverter(getWld()), ctx))
              .isInstanceOf(FunctionNotFoundException.class);
    }

}
