package be.unamur.info.b314.compiler.mappers.values.booleans;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.statements.StatementSetMapper;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.pils.declarations.Function;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionNode;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import be.unamur.info.b314.compiler.pils.statements.StatementSet;
import be.unamur.info.b314.compiler.pils.values.booleans.Negation;
import be.unamur.info.b314.compiler.pils.values.references.FunctionCall;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class NegationMapperTest {

    private static NegationMapper mapper;
    private static Context context;

    @Before
    public void before() throws Exception {
        context = new Context();
        mapper = context.mappers.getNegationMapper();
    }

    @Test
    public void give_simple_negation_when_apply_preprocessor_then_expect_argument() {

        final Negation negation = Negation.builder()
              .expression(ExpressionLeaf.TRUE)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.toString()).isEqualTo("not (true)");

        final Argument argument = mapper.apply(negation);
        assertThat(argument).isNotNull();
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo("0");
    }

    @Test
    public void give_complex_constant_negation_when_apply_preprocessor_then_expect_argument() {

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.EQ)
              .left(ExpressionLeaf.ONE)
              .right(ExpressionLeaf.ZERO)
              .build();
        assertThat(expression.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(expression)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.toString()).isEqualTo("not (1 = 0)");

        final Argument argument = mapper.apply(negation);
        assertThat(argument).isNotNull();
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo("1");
    }

    @Test
    public void give_simple_variable_negation_when_apply_preprocessor_then_expect_argument() {

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.BOOLEAN)
              .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
              .variable(variable)
              .build();
        assertThat(reference.isValid()).isTrue();

        final Expression expression = new ExpressionLeaf(reference);
        assertThat(expression.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(expression)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.toString()).isEqualTo("not (x)");

        context.memory.allocate(0, 1, 0);

        final Argument argument = mapper.apply(negation);
        assertThat(argument).isNotNull();
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo("boolean1");
    }

    @Test
    public void give_complex_variable_negation_when_apply_preprocessor_then_expect_argument() {

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.INTEGER)
              .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
              .variable(variable)
              .build();
        assertThat(reference.isValid()).isTrue();

        final Expression leaf = new ExpressionLeaf(reference);
        assertThat(leaf.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.EQ)
              .left(leaf)
              .right(ExpressionLeaf.ZERO)
              .build();
        assertThat(expression.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(expression)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.toString()).isEqualTo("not (x = 0)");

        context.memory.allocate(0, 1, 0);

        final Argument argument = mapper.apply(negation);
        assertThat(argument).isNotNull();
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo("boolean1");
    }

    @Test
    public void give_function_call_with_no_arg_negation_when_apply_preprocessor_then_expect_argument() {

        final Function function = Function.builder()
              .name("f")
              .type(Type.BOOLEAN)
              .output(ExpressionLeaf.TRUE)
              .build();
        assertThat(function.isValid()).isTrue();

        final FunctionCall call = FunctionCall.builder()
              .function(function)
              .build();
        assertThat(call.isValid()).isTrue();

        final Expression expression = new ExpressionLeaf(call);
        assertThat(expression.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(expression)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.toString()).isEqualTo("not (f())");

        context.memory.allocate(negation.getSpaceRequirement());

        // TODO 22/04/2021 [HBA]: update
        final Argument argument = mapper.apply(negation);
        assertThat(argument).isNotNull();
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo("boolean1");
    }

    @Test
    public void give_function_call_with_simple_arg_negation_when_apply_preprocessor_then_expect_argument() {

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.SQUARE)
              .build();

        final Function function = Function.builder()
              .name("f")
              .type(Type.BOOLEAN)
              .argument(variable)
              .output(ExpressionLeaf.TRUE)
              .build();
        assertThat(function.isValid()).isTrue();

        final FunctionCall call = FunctionCall.builder()
              .function(function)
              .argument(ExpressionLeaf.DIRT)
              .build();
        assertThat(call.isValid()).isTrue();

        final Expression expression = new ExpressionLeaf(call);
        assertThat(expression.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(expression)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.toString()).isEqualTo("not (f(dirt))");

        context.memory.allocate(negation.getSpaceRequirement());

        // TODO 22/04/2021 [HBA]: update
        final Argument argument = mapper.apply(negation);
        assertThat(argument).isNotNull();
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo("boolean1");
    }

    @Test
    public void give_function_call_with_complex_arg_negation_when_apply_preprocessor_then_expect_argument() {

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.BOOLEAN)
              .build();

        final Function function = Function.builder()
              .name("f")
              .type(Type.BOOLEAN)
              .argument(variable)
              .output(ExpressionLeaf.TRUE)
              .build();
        assertThat(function.isValid()).isTrue();

        final Expression node = ExpressionNode.builder()
              .operator(Operator.EQ)
              .left(ExpressionLeaf.DIRT)
              .right(ExpressionLeaf.ROCK)
              .build();

        final FunctionCall call = FunctionCall.builder()
              .function(function)
              .argument(node)
              .build();
        assertThat(call.isValid()).isTrue();

        final Expression expression = new ExpressionLeaf(call);
        assertThat(expression.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(expression)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.toString()).isEqualTo("not (f(dirt = rock))");

        context.memory.allocate(negation.getSpaceRequirement());

        // TODO 22/04/2021 [HBA]: update
        final Argument argument = mapper.apply(negation);
        assertThat(argument).isNotNull();
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo("boolean1");
    }

    @Test
    public void given_constant_set_expression_when_apply_mapper_then_expect_correct_value() {

        final StatementSetMapper setMapper = context.mappers.getStatementSetMapper();

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.BOOLEAN)
              .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
              .variable(variable)
              .build();
        assertThat(reference.isValid()).isTrue();

        final Expression value = ExpressionNode.builder()
              .operator(Operator.EQ)
              .left(ExpressionLeaf.DIRT)
              .right(ExpressionLeaf.ROCK)
              .build();
        assertThat(value.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(value)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.isConstant()).isTrue();

        final Expression expression = new ExpressionLeaf(negation);
        assertThat(expression.isValid()).isTrue();

        final StatementSet statement = StatementSet.builder()
              .reference(reference)
              .expression(expression)
              .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set x to not (dirt = rock)");

        context.memory.allocate(statement.getSpaceRequirement());

        final StructureSimple structure = setMapper.apply(statement);
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(1);
        assertThat(structure.toString()).isEqualTo("mov x, 1");
    }

    @Test
    public void given_variable_set_expression_when_apply_mapper_then_expect_correct_value() {

        final StatementSetMapper setMapper = context.mappers.getStatementSetMapper();

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.BOOLEAN)
              .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
              .variable(variable)
              .build();
        assertThat(reference.isValid()).isTrue();

        final Expression x = new ExpressionLeaf(reference);
        assertThat(x.isValid()).isTrue();

        final Expression value = ExpressionNode.builder()
              .operator(Operator.EQ)
              .left(x)
              .right(ExpressionLeaf.FALSE)
              .build();
        assertThat(value.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(value)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.isConstant()).isFalse();

        final Expression expression = new ExpressionLeaf(negation);
        assertThat(expression.isValid()).isTrue();

        final StatementSet statement = StatementSet.builder()
              .reference(reference)
              .expression(expression)
              .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set x to not (x = false)");

        context.memory.allocate(statement.getSpaceRequirement());

        final StructureSimple structure = setMapper.apply(statement);
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(3);
        assertThat(structure.toString()).isEqualTo(
              "cmp EQ, boolean1, x, 0\n" +
                    "neg boolean1, boolean1\n" +
                    "mov x, boolean1"
        );
    }

    @Test
    public void given_multi_level_variable_set_expression_when_apply_mapper_then_expect_correct_value() {

        final StatementSetMapper setMapper = context.mappers.getStatementSetMapper();

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.BOOLEAN)
              .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
              .variable(variable)
              .build();
        assertThat(reference.isValid()).isTrue();

        final Expression x = new ExpressionLeaf(reference);
        assertThat(x.isValid()).isTrue();

        final Expression left = ExpressionNode.builder()
              .operator(Operator.EQ)
              .left(x)
              .right(ExpressionLeaf.FALSE)
              .build();
        assertThat(left.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(left)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.isConstant()).isFalse();

        final Expression imbrication = new ExpressionLeaf(negation);
        assertThat(imbrication.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.AND)
              .left(imbrication)
              .right(ExpressionLeaf.TRUE)
              .build();
        assertThat(expression.isValid()).isTrue();

        final StatementSet statement = StatementSet.builder()
              .reference(reference)
              .expression(expression)
              .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set x to not (x = false) and true");

        context.memory.allocate(statement.getSpaceRequirement());

        final StructureSimple structure = setMapper.apply(statement);
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(4);
        assertThat(structure.toString()).isEqualTo(
              "cmp EQ, boolean1, x, 0\n"+
              "neg boolean1, boolean1\n"+
              "and boolean1, boolean1, 1\n"+
              "mov x, boolean1"
        );
    }

    @Test
    public void given_reverse_multi_level_variable_set_expression_when_apply_mapper_then_expect_correct_value() {

        final StatementSetMapper setMapper = context.mappers.getStatementSetMapper();

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.BOOLEAN)
              .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
              .variable(variable)
              .build();
        assertThat(reference.isValid()).isTrue();

        final Expression x = new ExpressionLeaf(reference);
        assertThat(x.isValid()).isTrue();

        final Expression right = ExpressionNode.builder()
              .operator(Operator.EQ)
              .left(x)
              .right(ExpressionLeaf.FALSE)
              .build();
        assertThat(right.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(right)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.isConstant()).isFalse();

        final Expression imbrication = new ExpressionLeaf(negation);
        assertThat(imbrication.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.AND)
              .left(ExpressionLeaf.TRUE)
              .right(imbrication)
              .build();
        assertThat(expression.isValid()).isTrue();

        final StatementSet statement = StatementSet.builder()
              .reference(reference)
              .expression(expression)
              .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set x to true and not (x = false)");

        context.memory.allocate(statement.getSpaceRequirement());

        final StructureSimple structure = setMapper.apply(statement);
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(4);
        assertThat(structure.toString()).isEqualTo(
              "cmp EQ, boolean1, x, 0\n"+
                    "neg boolean1, boolean1\n"+
                    "and boolean1, 1, boolean1\n"+
                    "mov x, boolean1"
        );
    }
}