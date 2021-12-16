package be.unamur.info.b314.compiler.pils.expressions;

import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class ExpressionNodeTest {

    @Test
    public void given_simple_node_expression_when_getSpaceRequirement_then_expect_1() {

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(ExpressionLeaf.ZERO)
              .right(ExpressionLeaf.ONE)
              .build();
        assertThat(expression.isValid()).isTrue();

        assertThat(expression.getSpaceRequirement().getInteger()).isEqualTo(0);
        assertThat(expression.getSpaceRequirement().getBool()).isEqualTo(0);
        assertThat(expression.getSpaceRequirement().getSquare()).isEqualTo(0);
    }

    @Test
    public void given_left_balanced_node_expression_when_getSpaceRequirement_then_expect_1() {

        final Expression left = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(ExpressionLeaf.ZERO)
              .right(ExpressionLeaf.ONE)
              .build();
        assertThat(left.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(left)
              .right(ExpressionLeaf.ONE)
              .build();
        assertThat(expression.isValid()).isTrue();

        assertThat(expression.getSpaceRequirement().getInteger()).isEqualTo(0);
        assertThat(expression.getSpaceRequirement().getBool()).isEqualTo(0);
        assertThat(expression.getSpaceRequirement().getSquare()).isEqualTo(0);
    }

    @Test
    public void given_right_balanced_node_expression_when_getSpaceRequirement_then_expect_1() {

        final Expression right = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(ExpressionLeaf.ZERO)
              .right(ExpressionLeaf.ONE)
              .build();
        assertThat(right.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(ExpressionLeaf.ONE)
              .right(right)
              .build();
        assertThat(expression.isValid()).isTrue();

        assertThat(expression.getSpaceRequirement().getInteger()).isEqualTo(0);
        assertThat(expression.getSpaceRequirement().getBool()).isEqualTo(0);
        assertThat(expression.getSpaceRequirement().getSquare()).isEqualTo(0);
    }

    @Test
    public void given_binary_node_expression_when_getSpaceRequirement_then_expect_2() {

        final Expression left = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(ExpressionLeaf.ZERO)
              .right(ExpressionLeaf.ONE)
              .build();
        assertThat(left.isValid()).isTrue();

        final Expression right = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(ExpressionLeaf.ZERO)
              .right(ExpressionLeaf.ONE)
              .build();
        assertThat(right.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(left)
              .right(right)
              .build();
        assertThat(expression.isValid()).isTrue();

        assertThat(expression.getSpaceRequirement().getInteger()).isEqualTo(0);
        assertThat(expression.getSpaceRequirement().getBool()).isEqualTo(0);
        assertThat(expression.getSpaceRequirement().getSquare()).isEqualTo(0);
    }

    @Test
    public void given_primitive_left_right_balanced_node_expression_when_getSpaceRequirement_then_expect_1() {

        final Expression left = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(ExpressionLeaf.ZERO)
              .right(ExpressionLeaf.ONE)
              .build();
        assertThat(left.isValid()).isTrue();

        final Expression right = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(left)
              .right(ExpressionLeaf.ONE)
              .build();
        assertThat(right.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(ExpressionLeaf.ONE)
              .right(right)
              .build();
        assertThat(expression.isValid()).isTrue();
        assertThat(expression.isPrimitive()).isTrue();

        assertThat(expression.getSpaceRequirement().getInteger()).isEqualTo(0);
        assertThat(expression.getSpaceRequirement().getBool()).isEqualTo(0);
        assertThat(expression.getSpaceRequirement().getSquare()).isEqualTo(0);
    }

    @Test
    public void given_single_constant_variable_node_expression_when_getSpaceRequirement_then_expect_1() {

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.INTEGER)
              .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
              .variable(variable)
              .build();
        assertThat(reference.isValid()).isTrue();

        final Expression x = new ExpressionLeaf(reference);
        assertThat(x.isValid()).isTrue();

        final Expression left = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(x)
              .right(ExpressionLeaf.ONE)
              .build();
        assertThat(left.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(left)
              .right(ExpressionLeaf.ONE)
              .build();
        assertThat(expression.isValid()).isTrue();
        assertThat(expression.isPrimitive()).isFalse();
        assertThat(expression.toString()).isEqualTo("x + 1 + 1");

        assertThat(expression.getSpaceRequirement().getInteger()).isEqualTo(1);
        assertThat(expression.getSpaceRequirement().getBool()).isEqualTo(0);
        assertThat(expression.getSpaceRequirement().getSquare()).isEqualTo(0);
    }

    @Test
    public void given_double_constant_variable_node_expression_when_getSpaceRequirement_then_expect_2() {

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.INTEGER)
              .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
              .variable(variable)
              .build();
        assertThat(reference.isValid()).isTrue();

        final Expression x = new ExpressionLeaf(reference);
        assertThat(x.isValid()).isTrue();

        final Expression left = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(x)
              .right(ExpressionLeaf.ONE)
              .build();
        assertThat(left.isValid()).isTrue();

        final Expression right = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(x)
              .right(ExpressionLeaf.ONE)
              .build();
        assertThat(right.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(left)
              .right(right)
              .build();
        assertThat(expression.isValid()).isTrue();
        assertThat(expression.isPrimitive()).isFalse();
        assertThat(expression.toString()).isEqualTo("x + 1 + x + 1");

        assertThat(expression.getSpaceRequirement().getInteger()).isEqualTo(2);
        assertThat(expression.getSpaceRequirement().getBool()).isEqualTo(0);
        assertThat(expression.getSpaceRequirement().getSquare()).isEqualTo(0);
    }

    @Test
    public void given_right_double_constant_variable_node_expression_when_getSpaceRequirement_then_expect_1() {

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.INTEGER)
              .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
              .variable(variable)
              .build();
        assertThat(reference.isValid()).isTrue();

        final Expression x = new ExpressionLeaf(reference);
        assertThat(x.isValid()).isTrue();

        final Expression farRight = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(x)
              .right(ExpressionLeaf.ONE)
              .build();
        assertThat(farRight.isValid()).isTrue();

        final Expression right = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(x)
              .right(farRight)
              .build();
        assertThat(right.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(ExpressionLeaf.ONE)
              .right(right)
              .build();
        assertThat(expression.isValid()).isTrue();
        assertThat(expression.isPrimitive()).isFalse();
        assertThat(expression.toString()).isEqualTo("1 + x + x + 1");

        assertThat(expression.getSpaceRequirement().getInteger()).isEqualTo(1);
        assertThat(expression.getSpaceRequirement().getBool()).isEqualTo(0);
        assertThat(expression.getSpaceRequirement().getSquare()).isEqualTo(0);
    }

    @Test
    public void given_simple_double_constant_variable_node_expression_when_getSpaceRequirement_then_expect_1() {

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.INTEGER)
              .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
              .variable(variable)
              .build();
        assertThat(reference.isValid()).isTrue();

        final Expression x = new ExpressionLeaf(reference);
        assertThat(x.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(x)
              .right(x)
              .build();
        assertThat(expression.isValid()).isTrue();
        assertThat(expression.isPrimitive()).isFalse();
        assertThat(expression.toString()).isEqualTo("x + x");

        assertThat(expression.getSpaceRequirement().getInteger()).isEqualTo(1);
        assertThat(expression.getSpaceRequirement().getBool()).isEqualTo(0);
        assertThat(expression.getSpaceRequirement().getSquare()).isEqualTo(0);
    }

}