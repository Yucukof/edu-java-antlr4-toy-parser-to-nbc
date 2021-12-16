package be.unamur.info.b314.compiler.mappers.values.squares;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.statements.StatementSetMapper;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionNode;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import be.unamur.info.b314.compiler.pils.statements.StatementSet;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import be.unamur.info.b314.compiler.pils.values.squares.Nearby;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class NearbyMapperTest {

    private static Context context;
    private static NearbyMapper mapper;

    @Before
    public void before() throws Exception {
        context = new Context();
        mapper = context.mappers.getNearbyMapper();
    }

    @Test
    public void given_constant_nearby_when_mapper_apply_then_expect_argument() {

        final Nearby nearby = Nearby.builder()
              .coordinates(new ImmutablePair<>(ExpressionLeaf.ONE, ExpressionLeaf.ONE))
              .build();
        assertThat(nearby.isValid()).isTrue();
        assertThat(nearby.toString()).isEqualTo("nearby[1, 1]");

        context.memory.allocate(nearby.getSpaceRequirement());

        final Argument argument = mapper.apply(nearby);
        assertThat(argument).isNotNull();
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo("NEARBY[0][0]");
    }

    @Test
    public void given_semi_variable_nearby_when_mapper_apply_then_expect_statements() {
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
              .operator(Operator.ADD)
              .left(leaf)
              .right(ExpressionLeaf.ONE)
              .build();

        final Nearby nearby = Nearby.builder()
              .coordinates(new ImmutablePair<>(expression, ExpressionLeaf.ONE))
              .build();
        assertThat(nearby.isValid()).isTrue();
        assertThat(nearby.toString()).isEqualTo("nearby[x + 1, 1]");

        context.memory.allocate(nearby.getSpaceRequirement());

        final Argument argument = mapper.apply(nearby);
        assertThat(argument).isNotNull();
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo("NEARBY[integer1][0]");
    }

    @Test
    public void given_inverted_semi_variable_nearby_when_mapper_apply_then_expect_statements() {
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
              .operator(Operator.ADD)
              .left(leaf)
              .right(ExpressionLeaf.ONE)
              .build();

        final Nearby nearby = Nearby.builder()
              .coordinates(new ImmutablePair<>(ExpressionLeaf.ONE, expression))
              .build();

        assertThat(nearby.isValid()).isTrue();
        assertThat(nearby.toString()).isEqualTo("nearby[1, x + 1]");

        context.memory.allocate(nearby.getSpaceRequirement());

        final Argument argument = mapper.apply(nearby);
        assertThat(argument).isNotNull();
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo("NEARBY[0][integer1]");
    }

    @Test
    public void given_variable_nearby_when_mapper_apply_then_expect_statements() {
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
              .operator(Operator.ADD)
              .left(leaf)
              .right(ExpressionLeaf.ONE)
              .build();

        final Nearby nearby = Nearby.builder()
              .coordinates(new ImmutablePair<>(expression, expression))
              .build();
        assertThat(nearby.isValid()).isTrue();
        assertThat(nearby.toString()).isEqualTo("nearby[x + 1, x + 1]");

        context.memory.allocate(nearby.getSpaceRequirement());

        final Argument argument = mapper.apply(nearby);
        assertThat(argument).isNotNull();
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo("NEARBY[integer1][integer2]");
    }

    @Test
    public void given_multi_level_set_statement_with_variable_nearby_when_mapper_apply_then_expect_statements() {

        final StatementSetMapper setMapper = context.mappers.getStatementSetMapper();

        final Variable target = Variable.builder()
              .name("y")
              .type(Type.BOOLEAN)
              .build();
        assertThat(target.isValid()).isTrue();

        final VariableReference targetReference = VariableReference.builder()
              .variable(target)
              .build();
        assertThat(targetReference.isValid()).isTrue();

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

        final Expression innerExpression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(leaf)
              .right(ExpressionLeaf.ONE)
              .build();

        final Nearby nearby = Nearby.builder()
              .coordinates(new ImmutablePair<>(leaf, innerExpression))
              .build();
        assertThat(nearby.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.EQ)
              .left(ExpressionLeaf.DIRT)
              .right(new ExpressionLeaf(nearby))
              .build();
        assertThat(expression.isValid()).isTrue();
        assertThat(expression.isConstant()).isFalse();

        final StatementSet statement = StatementSet.builder()
              .reference(targetReference)
              .expression(expression)
              .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set y to dirt = nearby[x, x + 1]");

        context.memory.allocate(statement.getSpaceRequirement());

        final StructureSimple structure = setMapper.apply(statement);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.toString()).isEqualTo(
              "sub x, x, 1\n" +
                    "add integer1, x, 1\n" +
                    "sub integer1, integer1, 1\n" +
                    "cmp EQ, boolean1, 0, NEARBY[x][integer1]\n" +
                    "mov y, boolean1"
        );
    }

    @Test
    public void given_reversed_multi_level_set_statement_with_variable_nearby_when_mapper_apply_then_expect_statements() {

        final StatementSetMapper setMapper = context.mappers.getStatementSetMapper();

        final Variable target = Variable.builder()
              .name("y")
              .type(Type.BOOLEAN)
              .build();
        assertThat(target.isValid()).isTrue();

        final VariableReference targetReference = VariableReference.builder()
              .variable(target)
              .build();
        assertThat(targetReference.isValid()).isTrue();

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

        final Expression innerExpression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(leaf)
              .right(ExpressionLeaf.ONE)
              .build();

        final Nearby nearby = Nearby.builder()
              .coordinates(new ImmutablePair<>(innerExpression, leaf))
              .build();
        assertThat(nearby.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.EQ)
              .left(ExpressionLeaf.DIRT)
              .right(new ExpressionLeaf(nearby))
              .build();
        assertThat(expression.isValid()).isTrue();
        assertThat(expression.isConstant()).isFalse();

        final StatementSet statement = StatementSet.builder()
              .reference(targetReference)
              .expression(expression)
              .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set y to dirt = nearby[x + 1, x]");

        context.memory.allocate(statement.getSpaceRequirement());

        final StructureSimple structure = setMapper.apply(statement);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.toString()).isEqualTo(
              "add integer1, x, 1\n" +
                    "sub integer1, integer1, 1\n" +
                    "sub x, x, 1\n" +
                    "cmp EQ, boolean1, 0, NEARBY[integer1][x]\n" +
                    "mov y, boolean1"
        );
    }

    @Test
    public void given_multi_level_set_statement_with_twice_variable_nearby_when_mapper_apply_then_expect_statements() {

        final StatementSetMapper setMapper = context.mappers.getStatementSetMapper();

        final Variable target = Variable.builder()
              .name("y")
              .type(Type.BOOLEAN)
              .build();
        assertThat(target.isValid()).isTrue();

        final VariableReference targetReference = VariableReference.builder()
              .variable(target)
              .build();
        assertThat(targetReference.isValid()).isTrue();

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

        final Expression innerExpression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(leaf)
              .right(ExpressionLeaf.ONE)
              .build();

        final Nearby nearby = Nearby.builder()
              .coordinates(new ImmutablePair<>(innerExpression, innerExpression))
              .build();
        assertThat(nearby.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.EQ)
              .left(ExpressionLeaf.DIRT)
              .right(new ExpressionLeaf(nearby))
              .build();
        assertThat(expression.isValid()).isTrue();
        assertThat(expression.isConstant()).isFalse();

        final StatementSet statement = StatementSet.builder()
              .reference(targetReference)
              .expression(expression)
              .build();
        assertThat(statement.isValid()).isTrue();
        assertThat(statement.toString()).isEqualTo("set y to dirt = nearby[x + 1, x + 1]");

        context.memory.allocate(statement.getSpaceRequirement());

        final StructureSimple structure = setMapper.apply(statement);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(6);
        assertThat(structure.toString()).isEqualTo(
              "add integer1, x, 1\n" +
                    "sub integer1, integer1, 1\n" +
                    "add integer2, x, 1\n" +
                    "sub integer2, integer2, 1\n" +
                    "cmp EQ, boolean1, 0, NEARBY[integer1][integer2]\n" +
                    "mov y, boolean1"
        );
    }
}