package be.unamur.info.b314.compiler.mappers.statements;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.declarations.FunctionMapper;
import be.unamur.info.b314.compiler.mappers.values.FunctionCallMapper;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.declarations.Function;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionNode;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import be.unamur.info.b314.compiler.pils.statements.StatementSet;
import be.unamur.info.b314.compiler.pils.values.references.FunctionCall;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class StatementSetMapperTest {

    private static FunctionMapper functionMapper;
    private static FunctionCallMapper callMapper;
    private Context context;
    private StatementSetMapper mapper;

    @Before
    public void before() {
        context = new Context();
        mapper = context.mappers.getStatementSetMapper();
        functionMapper = context.mappers.getFunctionMapper();
        callMapper = context.mappers.getFunctionCallMapper();
    }

    @Test
    public void given_simple_set_statement_when_mapper_apply_then_expect_structure() {
        final Variable variable = Variable.builder()
                .name("test")
                .type(Type.INTEGER)
                .dimensions(Collections.emptyList())
                .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
                .variable(variable)
                .indexes(Collections.emptyList())
                .build();
        assertThat(reference.isValid()).isTrue();

        final StatementSet statement = StatementSet.builder()
                .reference(reference)
                .expression(ExpressionLeaf.ONE)
                .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set test to 1");

        final StructureSimple result = mapper.apply(statement);
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getStatements().size()).isEqualTo(1);
        assertThat(result.toString()).isEqualTo("mov test, 1");
    }

    @Test
    public void given_addition_set_statement_when_mapper_apply_then_expect_structure() {
        final Variable variable = Variable.builder()
                .name("test")
                .type(Type.INTEGER)
                .dimensions(Collections.emptyList())
                .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
                .variable(variable)
                .indexes(Collections.emptyList())
                .build();
        assertThat(reference.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
                .operator(Operator.ADD)
                .left(ExpressionLeaf.ONE)
                .right(ExpressionLeaf.ONE)
                .build();
        assertThat(expression.isValid()).isTrue();

        final StatementSet statement = StatementSet.builder()
                .reference(reference)
                .expression(expression)
                .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set test to 1 + 1");

        final StructureSimple result = mapper.apply(statement);
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getStatements().size()).isEqualTo(1);
        assertThat(result.toString()).isEqualTo("mov test, 2");
    }

    @Test
    public void given_simple_set_statement_with_single_index_when_mapper_apply_then_expect_structure() {
        final Variable variable = Variable.builder()
                .name("test")
                .type(Type.INTEGER)
                .dimensions(Collections.singletonList(2))
                .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
                .variable(variable)
                .indexes(Collections.singletonList(ExpressionLeaf.ONE))
                .build();
        assertThat(reference.isValid()).isTrue();

        final StatementSet statement = StatementSet.builder()
                .reference(reference)
                .expression(ExpressionLeaf.ONE)
                .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set test[1] to 1");

        final StructureSimple result = mapper.apply(statement);
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getStatements().size()).isEqualTo(1);
        assertThat(result.toString()).isEqualTo("mov test[1], 1");
    }

    @Test
    public void given_simple_set_statement_with_double_index_when_mapper_apply_then_expect_structure() {
        final Variable variable = Variable.builder()
                .name("test")
                .type(Type.INTEGER)
                .dimensions(Arrays.asList(2, 2))
                .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
                .variable(variable)
                .indexes(Arrays.asList(ExpressionLeaf.ONE, ExpressionLeaf.ZERO))
                .build();
        assertThat(reference.isValid()).isTrue();

        final StatementSet statement = StatementSet.builder()
                .reference(reference)
                .expression(ExpressionLeaf.ONE)
                .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set test[1,0] to 1");

        final StructureSimple result = mapper.apply(statement);
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getStatements().size()).isEqualTo(1);
        assertThat(result.toString()).isEqualTo("mov test[1][0], 1");
    }

    @Test
    public void given_simple_set_statement_with_addition_index_when_mapper_apply_then_expect_structure() {

        final Variable variable = Variable.builder()
                .name("test")
                .type(Type.INTEGER)
                .dimensions(Collections.singletonList(3))
                .build();
        assertThat(variable.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
                .operator(Operator.ADD)
                .left(ExpressionLeaf.ONE)
                .right(ExpressionLeaf.ONE)
                .build();
        assertThat(expression.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
                .variable(variable)
                .indexes(Collections.singletonList(expression))
                .build();
        assertThat(reference.isValid()).isTrue();

        final StatementSet statement = StatementSet.builder()
                .reference(reference)
                .expression(ExpressionLeaf.ONE)
                .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set test[1 + 1] to 1");

        final StructureSimple result = mapper.apply(statement);
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getStatements().size()).isEqualTo(1);
        assertThat(result.toString()).isEqualTo("mov test[2], 1");
    }

    @Test
    public void given_simple_variable_set_statement_when_mapper_apply_then_expect_structure() {
        final Variable variable = Variable.builder()
                .name("test")
                .type(Type.INTEGER)
                .dimensions(Collections.emptyList())
                .build();
        assertThat(variable.isValid()).isTrue();

        final Variable x = Variable.builder()
                .name("x")
                .type(Type.INTEGER)
                .dimensions(Collections.emptyList())
                .build();
        assertThat(x.isValid()).isTrue();

        final VariableReference xRef = VariableReference.builder()
                .variable(x)
                .indexes(Collections.emptyList())
                .build();
        assertThat(x.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
                .variable(variable)
                .indexes(Collections.emptyList())
                .build();
        assertThat(reference.isValid()).isTrue();

        final ExpressionLeaf expression = new ExpressionLeaf(xRef);
        assertThat(expression.isValid()).isTrue();

        final StatementSet statement = StatementSet.builder()
                .reference(reference)
                .expression(expression)
                .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set test to x");

        final StructureSimple result = mapper.apply(statement);
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getStatements().size()).isEqualTo(1);
        assertThat(result.toString()).isEqualTo("mov test, x");
    }

    @Test
    public void given_addition_with_variable_set_statement_when_mapper_apply_then_expect_structure() {
        final Variable variable = Variable.builder()
                .name("test")
                .type(Type.INTEGER)
                .dimensions(Collections.emptyList())
                .build();
        assertThat(variable.isValid()).isTrue();

        final Variable x = Variable.builder()
                .name("x")
                .type(Type.INTEGER)
                .dimensions(Collections.emptyList())
                .build();
        assertThat(x.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
                .variable(variable)
                .indexes(Collections.emptyList())
                .build();
        assertThat(reference.isValid()).isTrue();

        final VariableReference xRef = VariableReference.builder()
                .variable(x)
                .indexes(Collections.emptyList())
                .build();
        assertThat(x.isValid()).isTrue();

        final Expression expression = new ExpressionLeaf(xRef);
        assertThat(expression.isValid()).isTrue();

        final Expression node = ExpressionNode.builder()
                .operator(Operator.ADD)
                .left(expression)
                .right(ExpressionLeaf.ONE)
                .build();
        assertThat(expression.isValid()).isTrue();

        final StatementSet statement = StatementSet.builder()
                .reference(reference)
                .expression(node)
                .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set test to x + 1");

        context.memory.allocate(1, 0, 0);

        final StructureSimple result = mapper.apply(statement);
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getStatements().size()).isEqualTo(2);
        assertThat(result.toString()).isEqualTo("add integer1, x, 1\nmov test, integer1");
    }

    @Test
    public void given_simple_set_statement_with_addition_index_with_variable_when_mapper_apply_then_expect_structure() {

        final Variable variable = Variable.builder()
                .name("test")
                .type(Type.INTEGER)
                .dimensions(Collections.singletonList(3))
                .build();
        assertThat(variable.isValid()).isTrue();

        final Variable x = Variable.builder()
                .name("x")
                .type(Type.INTEGER)
                .dimensions(Collections.emptyList())
                .build();
        assertThat(x.isValid()).isTrue();

        final VariableReference xRef = VariableReference.builder()
                .variable(x)
                .indexes(Collections.emptyList())
                .build();
        assertThat(x.isValid()).isTrue();

        final Expression expression = new ExpressionLeaf(xRef);
        assertThat(expression.isValid()).isTrue();

        final Expression node = ExpressionNode.builder()
                .operator(Operator.ADD)
                .left(expression)
                .right(ExpressionLeaf.ONE)
                .build();
        assertThat(expression.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
                .variable(variable)
                .indexes(Collections.singletonList(node))
                .build();
        assertThat(reference.isValid()).isTrue();

        final StatementSet statement = StatementSet.builder()
                .reference(reference)
                .expression(ExpressionLeaf.ONE)
                .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set test[x + 1] to 1");

        context.memory.allocate(1, 0, 0);

        final StructureSimple result = mapper.apply(statement);
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getStatements().size()).isEqualTo(2);
        assertThat(result.toString()).isEqualTo("add integer1, x, 1\nmov test[integer1], 1");
    }

    @Test
    public void given_simple_function_set_statement_when_mapper_apply_then_expect_structure() {
        final Variable variable6 = Variable.builder()
                .name("test")
                .type(Type.INTEGER)
                .dimensions(Collections.emptyList())
                .build();
        assertThat(variable6.isValid()).isTrue();

        final Function function = Function.builder()
                .name("f")
                .type(Type.INTEGER)
                .arguments(Collections.emptyList())
                .output(ExpressionLeaf.ONE)
                .outputIdentifier(new Identifier("result"))
                .build();
        assertThat(function.isValid()).isTrue();

        final FunctionCall f = FunctionCall.builder()
                .function(function)
                .arguments(Collections.emptyList())
                .build();
        assertThat(f.isValid()).isTrue();

        final VariableReference reference6 = VariableReference.builder()
                .variable(variable6)
                .indexes(Collections.emptyList())
                .build();
        assertThat(reference6.isValid()).isTrue();

        final StatementSet statement = StatementSet.builder()
                .reference(reference6)
                .expression(new ExpressionLeaf(f))
                .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set test to f()");

        final StructureSimple result = mapper.apply(statement);

        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getStatements().size()).isEqualTo(2);
        assertThat(result.toString()).isEqualTo("call f\nmov test, result");
    }

    @Test
    public void given_complex_function_set_statement_when_mapper_apply_then_expect_structure() {
        final Variable variable = Variable.builder()
                .name("x")
                .type(Type.INTEGER)
                .build();
        assertThat(variable.isValid()).isTrue();
        variable.set(ExpressionLeaf.ONE);
        assertThat(variable.isValid()).isTrue();

        final Variable argument = Variable.builder()
                .name("a1")
                .type(Type.INTEGER)
                .build();
        assertThat(argument.isValid()).isTrue();
        argument.set(ExpressionLeaf.ONE);
        assertThat(argument.isValid()).isTrue();

        final Function function = Function.builder()
                .name("f")
                .type(Type.INTEGER)
                .arguments(Collections.singleton(argument))
                .output(ExpressionLeaf.ONE)
                .build();
        assertThat(function.isValid()).isTrue();

        context.mappers.getFunctionMapper().apply(function);

        final FunctionCall f = FunctionCall.builder()
                .function(function)
                .arguments(Collections.singleton(ExpressionLeaf.ONE))
                .build();
        assertThat(f.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
                .variable(variable)
                .indexes(Collections.emptyList())
                .build();
        assertThat(reference.isValid()).isTrue();

        final StatementSet statement = StatementSet.builder()
                .reference(reference)
                .expression(new ExpressionLeaf(f))
                .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set x to f(1)");

        final StructureSimple result = mapper.apply(statement);

        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getStatements().size()).isEqualTo(3);
        assertThat(result.toString()).isEqualTo("mov f_a1, 1\ncall f\nmov x, f_result");
    }
}