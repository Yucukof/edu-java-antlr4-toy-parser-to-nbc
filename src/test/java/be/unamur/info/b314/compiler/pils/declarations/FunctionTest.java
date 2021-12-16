package be.unamur.info.b314.compiler.pils.declarations;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.PilsToNbcConverter;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidFunctionDeclarationException;
import be.unamur.info.b314.compiler.pils.program.Program;
import be.unamur.info.b314.compiler.pils.program.World;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Hadrien BAILLY
 */
public class FunctionTest extends UnitTestTemplate {

    @Test
    public void given_void_function_without_statement_or_return_when_walker_then_expect_function() {
        final String input = "f1 as function(x as integer): void " +
              "declare local y as integer; " +
              "do " +
              "done";

        final GrammarParser parser = getParser(input);
        final GrammarParser.DeclarationContext ctx = parser.declaration();

        final ParseTreeWalker walker = new ParseTreeWalker();
        final PilsToNbcConverter listener = new PilsToNbcConverter(getWld());

        listener.setProgram(new World());
        final Program world = listener.getProgram();
        assertThat(world).isNotNull();
        walker.walk(listener, ctx);

        assertThat(world.getDeclarations().size()).isEqualTo(1);
        final Declaration declaration = listener.getProgram().getDeclarations().get(0);
        assertThat(declaration).isInstanceOf(Function.class);
        assertThat(declaration.getName()).isEqualTo("f1");
        assertThat(declaration.getType()).isEqualTo(Type.VOID);
    }

    @Test
    public void given_void_function_with_statements_and_without_return_when_walker_then_expect_function() {
        final String input = "f1 as function(x as integer): void " +
              "declare local y as integer; " +
              "do set y to 1 set y to 2 set y to 0" +
              "done";

        final GrammarParser parser = getParser(input);
        final GrammarParser.DeclarationContext ctx = parser.declaration();

        final ParseTreeWalker walker = new ParseTreeWalker();
        final PilsToNbcConverter listener = new PilsToNbcConverter(getWld());

        listener.setProgram(new World());
        final Program world = listener.getProgram();
        assertThat(world).isNotNull();
        walker.walk(listener, ctx);

        assertThat(world.getDeclarations().size()).isEqualTo(1);
        final Declaration declaration = listener.getProgram().getDeclarations().get(0);
        assertThat(declaration).isInstanceOf(Function.class);
        assertThat(declaration.getName()).isEqualTo("f1");
        assertThat(declaration.getType()).isEqualTo(Type.VOID);
    }

    @Test
    public void given_void_function_without_statement_with_return_when_walker_then_expect_exception() {
        final String input = "f1 as function(x as integer): void " +
              "declare local y as integer; " +
              "do return 1" +
              "done";

        final GrammarParser parser = getParser(input);
        final GrammarParser.DeclarationContext ctx = parser.declaration();

        final ParseTreeWalker walker = new ParseTreeWalker();
        final PilsToNbcConverter listener = new PilsToNbcConverter(getWld());

        listener.setProgram(new World());
        final Program world = listener.getProgram();
        assertThat(world).isNotNull();
        assertThatThrownBy(() -> walker.walk(listener, ctx)).isInstanceOf(InvalidFunctionDeclarationException.class);

    }

    @Test
    public void given_void_function_with_statements_with_return_when_walker_then_expect_exception() {
        final String input = "f1 as function(x as integer): void " +
              "declare local y as integer; " +
              "do " +
              "set y to 1 " +
              "set y to 2 " +
              "set y to 0" +
              "return 1 " +
              "done";

        final GrammarParser parser = getParser(input);
        final GrammarParser.DeclarationContext ctx = parser.declaration();

        final ParseTreeWalker walker = new ParseTreeWalker();
        final PilsToNbcConverter listener = new PilsToNbcConverter(getWld());

        listener.setProgram(new World());
        final Program world = listener.getProgram();
        assertThat(world).isNotNull();
        assertThatThrownBy(() -> walker.walk(listener, ctx)).isInstanceOf(InvalidFunctionDeclarationException.class);

    }

    @Test
    public void given_non_void_function_without_statements_and_return_when_walker_then_expect_exception() {
        final String input = "f1 as function(x as integer): integer " +
              "declare local y as integer; " +
              "do " +
              "set y to 1 " +
              "set y to 2 " +
              "set y to 0" +
              "done";

        final GrammarParser parser = getParser(input);
        final GrammarParser.DeclarationContext ctx = parser.declaration();

        final ParseTreeWalker walker = new ParseTreeWalker();
        final PilsToNbcConverter listener = new PilsToNbcConverter(getWld());

        listener.setProgram(new World());
        final Program world = listener.getProgram();
        assertThat(world).isNotNull();
        assertThatThrownBy(() -> walker.walk(listener, ctx)).isInstanceOf(InvalidFunctionDeclarationException.class);
    }

    @Test
    public void given_non_void_function_with_statements_and_return_when_walker_then_expect_function() {
        final String input = "f1 as function(x as integer): integer " +
              "declare local y as integer; " +
              "do " +
              "set y to 1 " +
              "set y to 2 " +
              "set y to 0" +
              "return 1" +
              "done";

        final GrammarParser parser = getParser(input);
        final GrammarParser.DeclarationContext ctx = parser.declaration();

        final ParseTreeWalker walker = new ParseTreeWalker();
        final PilsToNbcConverter listener = new PilsToNbcConverter(getWld());

        listener.setProgram(new World());
        final Program world = listener.getProgram();
        assertThat(world).isNotNull();
        walker.walk(listener, ctx);

        assertThat(world.getDeclarations().size()).isEqualTo(1);
        final Declaration declaration = listener.getProgram().getDeclarations().get(0);
        assertThat(declaration).isInstanceOf(Function.class);
        assertThat(declaration.getName()).isEqualTo("f1");
        assertThat(declaration.getType()).isEqualTo(Type.INTEGER);
    }

    @Test
    public void given_non_void_function_wihout_statements_and_with_return_when_walker_then_expect_function() {
        final String input = "f1 as function(x as integer): integer " +
              "declare local y as integer; " +
              "do return 1" +
              "done";

        final GrammarParser parser = getParser(input);
        final GrammarParser.DeclarationContext ctx = parser.declaration();

        final ParseTreeWalker walker = new ParseTreeWalker();
        final PilsToNbcConverter listener = new PilsToNbcConverter(getWld());

        listener.setProgram(new World());
        final Program world = listener.getProgram();
        assertThat(world).isNotNull();
        walker.walk(listener, ctx);

        assertThat(world.getDeclarations().size()).isEqualTo(1);
        final Declaration declaration = listener.getProgram().getDeclarations().get(0);
        assertThat(declaration).isInstanceOf(Function.class);
        assertThat(declaration.getName()).isEqualTo("f1");
        assertThat(declaration.getType()).isEqualTo(Type.INTEGER);
    }

    @Test
    public void given_non_void_function_with_statements_without_return_when_walker_then_expect_exception() {
        final String input = "f1 as function(x as integer): integer " +
              "declare local y as integer; " +
              "do " +
              "set y to 1 " +
              "set y to 2 " +
              "set y to 0" +
              "done";

        final GrammarParser parser = getParser(input);
        final GrammarParser.DeclarationContext ctx = parser.declaration();

        final ParseTreeWalker walker = new ParseTreeWalker();
        final PilsToNbcConverter listener = new PilsToNbcConverter(getWld());

        listener.setProgram(new World());
        final Program world = listener.getProgram();
        assertThat(world).isNotNull();
        assertThatThrownBy(() -> walker.walk(listener, ctx)).isInstanceOf(InvalidFunctionDeclarationException.class);

    }

    @Test
    public void given_self_reference_in_function_when_walker_then_expect_function() {
        final String input = "f1 as function(x as integer): integer " +
              "declare local y as integer; " +
              "do " +
              "if x=0 " +
              "then set y to 0 " +
              "else set y to f1(x-1) " +
              "done " +
              "return y " +
              "done";

        final GrammarParser parser = getParser(input);
        final GrammarParser.DeclarationContext ctx = parser.declaration();

        final ParseTreeWalker walker = new ParseTreeWalker();
        final PilsToNbcConverter listener = new PilsToNbcConverter(getWld());

        listener.setProgram(new World());
        final Program world = listener.getProgram();
        assertThat(world).isNotNull();
        walker.walk(listener, ctx);

        assertThat(world.getDeclarations().size()).isEqualTo(1);
        final Declaration declaration = listener.getProgram().getDeclarations().get(0);
        assertThat(declaration).isInstanceOf(Function.class);
        assertThat(declaration.getName()).isEqualTo("f1");
        assertThat(declaration.getType()).isEqualTo(Type.INTEGER);
    }

    @Test
    public void given_function_without_argument_when_walker_then_expect_function_with_no_args() {
        final String input = "f1 as function(): integer " +
              "declare local z as integer; " +
              "do " +
              "set z to 1" +
              "return z " +
              "done";

        final GrammarParser parser = getParser(input);
        final GrammarParser.DeclarationContext ctx = parser.declaration();

        final ParseTreeWalker walker = new ParseTreeWalker();
        final PilsToNbcConverter listener = new PilsToNbcConverter(getWld());

        listener.setProgram(new World());
        final Program world = listener.getProgram();
        assertThat(world).isNotNull();
        walker.walk(listener, ctx);

        assertThat(world.getDeclarations().size()).isEqualTo(1);
        final Declaration declaration = listener.getProgram().getDeclarations().get(0);
        assertThat(declaration).isInstanceOf(Function.class);
        final Function function = (Function) declaration;
        assertThat(function.isValid()).isTrue();
        assertThat(function.getName()).isEqualTo("f1");
        assertThat(function.getType()).isEqualTo(Type.INTEGER);
        assertThat(function.getArguments().size()).isEqualTo(0);
    }

    @Test
    public void given_function_with_2_argument_when_walker_then_expect_function_with_2_args() {
        final String input = "f1 as function(x as integer, y as boolean): integer " +
              "declare local z as integer; " +
              "do " +
              "set z to 1" +
              "return z " +
              "done";

        final GrammarParser parser = getParser(input);
        final GrammarParser.DeclarationContext ctx = parser.declaration();

        final ParseTreeWalker walker = new ParseTreeWalker();
        final PilsToNbcConverter listener = new PilsToNbcConverter(getWld());

        listener.setProgram(new World());
        final Program world = listener.getProgram();
        assertThat(world).isNotNull();
        walker.walk(listener, ctx);

        assertThat(world.getDeclarations().size()).isEqualTo(1);
        final Declaration declaration = listener.getProgram().getDeclarations().get(0);
        assertThat(declaration).isInstanceOf(Function.class);
        final Function function = (Function) declaration;
        assertThat(function.isValid()).isTrue();
        assertThat(function.getName()).isEqualTo("f1");
        assertThat(function.getType()).isEqualTo(Type.INTEGER);
        assertThat(function.getArguments().size()).isEqualTo(2);
    }


}