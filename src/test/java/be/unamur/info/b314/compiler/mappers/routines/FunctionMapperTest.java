package be.unamur.info.b314.compiler.mappers.routines;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.declarations.FunctionMapper;
import be.unamur.info.b314.compiler.mappers.values.FunctionCallMapper;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.routines.Subroutine;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.pils.declarations.Function;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionNode;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import be.unamur.info.b314.compiler.pils.values.Value;
import be.unamur.info.b314.compiler.pils.values.references.FunctionCall;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FunctionMapperTest  extends UnitTestTemplate {

    private static FunctionMapper mapper;
    private static FunctionCallMapper callMapper;
    private static Context context;

    @BeforeClass
    public static void beforeClass() {
        context = new Context();
        mapper = context.mappers.getFunctionMapper();
        callMapper = context.mappers.getFunctionCallMapper();
    }

    @Test
    public void given_simple_complete_function_mapper_apply_then_expect_function() {

        String functionAsString = "functionTest1 as function(a as integer , z as boolean): integer  \n" +
                "        declare local locvariable as integer;\n" +
                "                      x as integer;\n" +
                "\n" +
                "        do set x to 1\n" +
                "\n" +
                "        return locvariable \n" +
                "    done";

        final GrammarParser parser = getParser(functionAsString);

        final GrammarParser.FctDeclContext ctx = parser.fctDecl();

        be.unamur.info.b314.compiler.listeners.Context symbols = new be.unamur.info.b314.compiler.listeners.Context();

        final Function function = Function.from(ctx, symbols);
        context.memory.allocate(function.getSpaceRequirement());

        final Subroutine finalFunction = mapper.apply(function);
        assertThat(finalFunction).isNotNull();
        assertThat(finalFunction.isValid()).isTrue();

    }
//sds
    @Test
    public void given_simple_function_mapper_without_local_variable_apply_then_expect_function() {

        String functionAsString = "functionTest1 as function(b as integer , z as boolean): integer  \n" +
                "        do set b to 1\n" +
                "\n" +
                "        return b \n" +
                "    done";

        final GrammarParser parser = getParser(functionAsString);

        final GrammarParser.FctDeclContext ctx = parser.fctDecl();

        be.unamur.info.b314.compiler.listeners.Context symbols = new be.unamur.info.b314.compiler.listeners.Context();

        final Function function = Function.from(ctx, symbols);
        context.memory.allocate(function.getSpaceRequirement());

        final Subroutine finalFunction = mapper.apply(function);

        assertThat(finalFunction).isNotNull();
        assertThat(finalFunction.isValid()).isTrue();
    }

    @Test
    public void given_simple_function_mapper_without_return_apply_then_expect_function() {

        String functionAsString = "functionTest1 as function(b as integer , z as boolean): void  \n" +

                "        do set b to 1\n" +
                "\n" +

                "    done";

        final GrammarParser parser = getParser(functionAsString);

        final GrammarParser.FctDeclContext ctx = parser.fctDecl();

        be.unamur.info.b314.compiler.listeners.Context symbols = new be.unamur.info.b314.compiler.listeners.Context();

        final Function function = Function.from(ctx, symbols);
        context.memory.allocate(function.getSpaceRequirement());

        final Subroutine finalfunction = mapper.apply(function);
        assertThat(finalfunction).isNotNull();

        assertThat(finalfunction.isValid()).isTrue();

    }

    @Test
    public void given_simple_function_mapper_with_two_arguments_in_a_set_apply_then_expect_function() {

        String functionAsString = "functionTest1 as function(b as integer , z as boolean): void  \n" +

                "        do set b to 1\n" +
                "\n" +
                "        so set z to b"+
                "\n" +

                "    done";

        final GrammarParser parser = getParser(functionAsString);

        final GrammarParser.FctDeclContext ctx = parser.fctDecl();

        be.unamur.info.b314.compiler.listeners.Context symbols = new be.unamur.info.b314.compiler.listeners.Context();

        final Function function = Function.from(ctx, symbols);
        context.memory.allocate(function.getSpaceRequirement());

        final Subroutine finalfunction = mapper.apply(function);
        assertThat(finalfunction).isNotNull();

        assertThat(finalfunction.isValid()).isTrue();

    }

    @Test
    public void given_simple_function_mapper_without_argument_apply_then_expect_function() {

        String functionAsString = "functionTest1 as function(): integer  \n" +
                "        declare local b as integer;\n" +
                "        do set b to 1\n" +
                "\n" +
                "        return b \n" +
                "    done";

        final GrammarParser parser = getParser(functionAsString);

        final GrammarParser.FctDeclContext ctx = parser.fctDecl();

        be.unamur.info.b314.compiler.listeners.Context symbols = new be.unamur.info.b314.compiler.listeners.Context();

        final Function function = Function.from(ctx, symbols);
        context.memory.allocate(function.getSpaceRequirement());

        final Subroutine finalfunction = mapper.apply(function);
        assertThat(finalfunction).isNotNull();

        assertThat(finalfunction.isValid()).isTrue();

    }

    @Test
    public void given_simple_function_mapper_with_expression_node_return_apply_then_expect_function() {

        String functionAsString = "functionTest1 as function(): integer  \n" +
                "        declare local b as integer;\n" +
                "        do set b to 1\n" +
                "\n" +
                "        return b + 1 \n" +
                "    done";

        final GrammarParser parser = getParser(functionAsString);

        final GrammarParser.FctDeclContext ctx = parser.fctDecl();

        be.unamur.info.b314.compiler.listeners.Context symbols = new be.unamur.info.b314.compiler.listeners.Context();

        final Function function = Function.from(ctx, symbols);
        context.memory.allocate(function.getSpaceRequirement());

        final Subroutine finalfunction = mapper.apply(function);
        assertThat(finalfunction).isNotNull();

        assertThat(finalfunction.isValid()).isTrue();

    }

    @Test
    public void given_simple_functionCall_mapper_apply_then_expect_identifier() {

        //String functionCallAsString = "functionTest1(a,b)";

        final Function function = Function.builder()
                .name("FunctionTest1")
                .type(Type.INTEGER)
                .output(ExpressionLeaf.ONE)
                .build();

        mapper.apply(function);

        FunctionCall functionCall = FunctionCall.builder()
                .function(function)
                .build();

        Argument argument = callMapper.apply(functionCall);
        assertThat(argument).isNotNull();
        assertThat(argument.isValid()).isTrue();
    }

    @Test
    public void given_simple_functionCall_mapper_with_argument_apply_then_expect_identifier() {

        //String functionCallAsString = "functionTest1(x)";

        final Variable x = Variable.builder()
                .name("x")
                .type(Type.INTEGER)
                .build();
        x.set(ExpressionLeaf.ONE);
        assertThat(x.isValid()).isTrue();

        final Variable y = Variable.builder()
                .name("y")
                .type(Type.INTEGER)
                .build();
        assertThat(y.isValid()).isTrue();

        final Function function = Function.builder()
                .name("functionTest1")
                .type(Type.INTEGER)
                .argument(x)
                .output(ExpressionLeaf.ONE)
                .build();

        mapper.apply(function);

        final VariableReference reference = VariableReference.builder()
                .variable(y)
                .build();

        final Expression leaf = new ExpressionLeaf(reference);

        FunctionCall functionCall = FunctionCall.builder()
                .function(function)
                .argument(leaf)
                .build();

        Argument argument = callMapper.apply(functionCall);
        assertThat(argument).isNotNull();
        assertThat(argument.isValid()).isTrue();
    }

    @Test
    public void given_simple_functionCall_mapper_with_two_arguments_apply_then_expect_identifier() {

        //String functionCallAsString = "functionTest1(x)";

        final Variable variable = Variable.builder()
                .name("w")
                .type(Type.INTEGER)
                .build();
        variable.set(ExpressionLeaf.ONE);
        assertThat(variable.isValid()).isTrue();

        final Variable variable2 = Variable.builder()
                .name("x")
                .type(Type.INTEGER)
                .build();
        variable2.set(ExpressionLeaf.ONE);
        assertThat(variable2.isValid()).isTrue();

        final List<Variable> argEff= new ArrayList<>();
        argEff.add(variable);
        argEff.add(variable2);

        final Function function = Function.builder()
                .name("functionTest1")
                .type(Type.INTEGER)
                .arguments(argEff)
                .output(ExpressionLeaf.ONE)
                .build();

        mapper.apply(function);

        final Variable variable3 = Variable.builder()
                .name("y")
                .type(Type.INTEGER)
                .build();

        final Variable variable4 = Variable.builder()
                .name("z")
                .type(Type.INTEGER)
                .build();

         final VariableReference reference = VariableReference.builder()
                .variable(variable3)
                .build();

        final VariableReference reference2 = VariableReference.builder()
                .variable(variable4)
                .build();

        final Expression leaf = new ExpressionLeaf(reference);
        final Expression leaf2 = new ExpressionLeaf(reference2);

        List <Expression> leaf3 = new ArrayList<>();
        leaf3.add(leaf);
        leaf3.add(leaf2);


        FunctionCall functionCall = FunctionCall.builder()
                .arguments(leaf3)
                .function(function)
                .build();

        Argument argument = callMapper.apply(functionCall);
        assertThat(argument).isNotNull();
        assertThat(argument.isValid()).isTrue();
    }

    @Test
    public void given_simple_functionCall_mapper_with_return_apply_then_expect_identifier() {

        //String functionCallAsString = "functionTest1(x)";

        final Variable x = Variable.builder()
                .name("x")
                .type(Type.INTEGER)
                .build();
        x.set(ExpressionLeaf.ONE);
        assertThat(x.isValid()).isTrue();

        final Variable y = Variable.builder()
                .name("y")
                .type(Type.INTEGER)
                .build();
        y.set(ExpressionLeaf.ONE);
        assertThat(y.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
                .variable(y)
                .build();

        final ExpressionLeaf output = new ExpressionLeaf(reference);

        final Function function = Function.builder()
                .name("functionTest1")
                .type(Type.INTEGER)
                .argument(x)
                .output(output)
                .build();

        mapper.apply(function);

        final Expression leaf = new ExpressionLeaf(reference);

        FunctionCall functionCall = FunctionCall.builder()
                .function(function)
                .argument(leaf)
                .build();

        Argument argument = callMapper.apply(functionCall);
        assertThat(argument).isNotNull();
        assertThat(argument.isValid()).isTrue();
    }


    @Test
    public void given_simple_functionCall_mapper_with_expression_node_argument_apply_then_expect_identifier() {

        //String functionCallAsString = "functionTest1(x)";

        final Variable variable = Variable.builder()
                .name("x")
                .type(Type.INTEGER)
                .build();
        variable.set(ExpressionLeaf.ONE);
        assertThat(variable.isValid()).isTrue();

        final Function function = Function.builder()
                .name("functionTest1")
                .type(Type.INTEGER)
                .argument(variable)
                .output(ExpressionLeaf.ONE)
                .build();

        mapper.apply(function);

        final Variable variable2 = Variable.builder()
                .name("y")
                .type(Type.INTEGER)
                .build();

        final VariableReference reference = VariableReference.builder()
                .variable(variable2)
                .build();

        final Expression leaf = new ExpressionLeaf(reference);

        final Expression expression = ExpressionNode.builder()
                .operator(Operator.ADD)
                .left(leaf)
                .right(ExpressionLeaf.ONE)
                .build();

        context.memory.allocate(1, 0, 0);

        FunctionCall functionCall = FunctionCall.builder()
                .function(function)
                .argument(expression)
                .build();

        Argument argument = callMapper.apply(functionCall);
        assertThat(argument).isNotNull();
        assertThat(argument.isValid()).isTrue();
    }

}
