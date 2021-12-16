package be.unamur.info.b314.compiler.local.semantics;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionNode;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.visitors.ExpressionVisitor;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Hadrien BAILLY
 */
public class UT15ExpressionTest extends UnitTestTemplate {

    private static final String RESOURCE_DIR = "/local/semantics/UT15/";

    @Test
    public void given_no_operation_when_visitExpression_then_expect_value() {

        final GrammarParser parser = getParser("1");
        final GrammarParser.ExpressionContext context = parser.expression();

        final Expression expression = new ExpressionVisitor(new Context()).visitExpression(context);
        assertThat(expression).isNotNull().isInstanceOf(ExpressionLeaf.class);
        final ExpressionLeaf operand = (ExpressionLeaf) expression;
        assertThat(operand.getType()).isEqualTo(Type.INTEGER);
        assertThat(operand.isPrimitive()).isTrue();
        assertThat(operand.getValue().getIntValue()).isEqualTo(1);
    }

    @Test
    public void given_simple_integer_operation_when_visitExpression_then_expect_value() {

        final GrammarParser parser = getParser("1+1");
        final GrammarParser.ExpressionContext context = parser.expression();

        final Expression expression = new ExpressionVisitor(new Context()).visitExpression(context);
        final ExpressionNode expressionNode = (ExpressionNode) expression;
        assertThat(expressionNode.getOperator()).isNotNull().isEqualTo(Operator.ADD);
        assertThat(expressionNode.getType()).isEqualTo(Type.INTEGER);
        assertThat(expressionNode.isPrimitive()).isTrue();
        assertThat(expressionNode.getValue().getIntValue()).isEqualTo(2);
    }

    @Test
    public void given_no_precedence_integer_operation_when_visitExpression_then_expect_value() {

        final GrammarParser parser = getParser("1+1+1");
        final GrammarParser.ExpressionContext context = parser.expression();

        final Expression expression = new ExpressionVisitor(new Context()).visitExpression(context);
        final ExpressionNode expressionNode = (ExpressionNode) expression;
        assertThat(expressionNode.getOperator()).isNotNull().isEqualTo(Operator.ADD);
        assertThat(expressionNode.getType()).isEqualTo(Type.INTEGER);
        assertThat(expressionNode.isPrimitive()).isTrue();
        assertThat(expressionNode.getValue().getIntValue()).isEqualTo(3);
    }

    @Test
    public void given_simple_precedence_integer_operation_when_visitExpression_then_expect_value() {

        final GrammarParser parser = getParser("1+1*2");
        final GrammarParser.ExpressionContext context = parser.expression();

        final Expression expression = new ExpressionVisitor(new Context()).visitExpression(context);
        final ExpressionNode expressionNode = (ExpressionNode) expression;
        assertThat(expressionNode.getOperator()).isNotNull().isEqualTo(Operator.ADD);
        assertThat(expressionNode.getType()).isEqualTo(Type.INTEGER);
        assertThat(expressionNode.isPrimitive()).isTrue();
        assertThat(expressionNode.getValue().getIntValue()).isEqualTo(3);
    }

    @Test
    public void given_reversed_precedence_integer_operation_when_visitExpression_then_expect_value() {

        final GrammarParser parser = getParser("1*2+1");
        final GrammarParser.ExpressionContext context = parser.expression();

        final Expression expression = new ExpressionVisitor(new Context()).visitExpression(context);
        final ExpressionNode expressionNode = (ExpressionNode) expression;
        assertThat(expressionNode.getOperator()).isNotNull().isEqualTo(Operator.ADD);
        assertThat(expressionNode.getType()).isEqualTo(Type.INTEGER);
        assertThat(expressionNode.isPrimitive()).isTrue();
        assertThat(expressionNode.getValue().getIntValue()).isEqualTo(3);
    }

    @Test
    public void given_mixed_precedence_integer_operation_when_visitExpression_then_expect_value() {

        final GrammarParser parser = getParser("2+1*3+1");
        final GrammarParser.ExpressionContext context = parser.expression();

        final Expression expression = new ExpressionVisitor(new Context()).visitExpression(context);
        final ExpressionNode expressionNode = (ExpressionNode) expression;
        assertThat(expressionNode.getOperator()).isNotNull().isEqualTo(Operator.ADD);
        assertThat(expressionNode.getType()).isEqualTo(Type.INTEGER);
        assertThat(expressionNode.isPrimitive()).isTrue();
        assertThat(expressionNode.getValue().getIntValue()).isEqualTo(6);
    }

    @Test
    public void given_mixed_type_operation_when_visitExpression_then_expect_value() {

        final GrammarParser parser = getParser("1<2 or dirt=graal");
        final GrammarParser.ExpressionContext context = parser.expression();

        final Expression expression = new ExpressionVisitor(new Context()).visitExpression(context);
        assertThat(expression).isNotNull();
        assertThat(expression.isPrimitive()).isTrue();
        assertThat(expression.getType()).isEqualTo(Type.BOOLEAN);
    }

    @Test
    public void given_invalid_type_operation_when_visitExpression_then_expect_exception() {

        final GrammarParser parser = getParser("dirt=1");
        final GrammarParser.ExpressionContext context = parser.expression();

        assertThatThrownBy(() -> new ExpressionVisitor(new Context()).visitExpression(context)).isInstanceOf(InvalidDataTypeException.class);
    }

    @Test
    public void given_operation_with_parenthesis_when_visitExpression_then_expect_exception() {

        final GrammarParser parser = getParser("2*(2+3)+1");
        final GrammarParser.ExpressionContext context = parser.expression();

        final Expression expression = new ExpressionVisitor(new Context()).visitExpression(context);
        final ExpressionNode expressionNode = (ExpressionNode) expression;
        assertThat(expressionNode.getOperator()).isNotNull().isEqualTo(Operator.ADD);
        assertThat(expressionNode.getType()).isEqualTo(Type.INTEGER);
    }

}
