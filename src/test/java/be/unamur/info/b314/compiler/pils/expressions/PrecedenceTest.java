package be.unamur.info.b314.compiler.pils.expressions;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.visitors.ExpressionVisitor;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class PrecedenceTest extends UnitTestTemplate {

    private static final ExpressionVisitor expressionVisitor = new ExpressionVisitor(new Context());

    @Test
    public void given_simple_expression_when_visitExpression_then_expect_correct_value() {

        final GrammarParser parser = getParser("1+1");
        final GrammarParser.ExpressionContext ctx = parser.expression();

        final Expression expression = expressionVisitor.visitExpression(ctx);

        assertThat(expression.isValid()).isTrue();
        assertThat(expression.isInteger()).isTrue();
        assertThat(expression.isPrimitive()).isTrue();
        assertThat(expression.getValue().getIntValue()).isEqualTo(2);
        assertThat(expression.toString()).isEqualTo("1 + 1");

    }


    @Test
    public void given_left_tree_expression_when_visitExpression_then_expect_correct_value() {

        final GrammarParser parser = getParser("2*2+1");
        final GrammarParser.ExpressionContext ctx = parser.expression();

        final Expression expression = expressionVisitor.visitExpression(ctx);

        assertThat(expression.isValid()).isTrue();
        assertThat(expression.isInteger()).isTrue();
        assertThat(expression.isPrimitive()).isTrue();
        assertThat(expression.getValue().getIntValue()).isEqualTo(5);
        assertThat(expression.toString()).isEqualTo("2 * 2 + 1");

    }


    @Test
    public void given_right_tree_expression_when_visitExpression_then_expect_correct_value() {

        final GrammarParser parser = getParser("1+2*2");
        final GrammarParser.ExpressionContext ctx = parser.expression();

        final Expression expression = expressionVisitor.visitExpression(ctx);

        assertThat(expression.isValid()).isTrue();
        assertThat(expression.isInteger()).isTrue();
        assertThat(expression.isPrimitive()).isTrue();
        assertThat(expression.getValue().getIntValue()).isEqualTo(5);
        assertThat(expression.toString()).isEqualTo("1 + 2 * 2");

    }


    @Test
    public void given_balance_tree_expression_when_visitExpression_then_expect_correct_value() {

        final GrammarParser parser = getParser("1*1+2*2");
        final GrammarParser.ExpressionContext ctx = parser.expression();

        final Expression expression = expressionVisitor.visitExpression(ctx);

        assertThat(expression.isValid()).isTrue();
        assertThat(expression.isInteger()).isTrue();
        assertThat(expression.isPrimitive()).isTrue();
        assertThat(expression.getValue().getIntValue()).isEqualTo(5);
        assertThat(expression.toString()).isEqualTo("1 * 1 + 2 * 2");

    }


    @Test
    public void given_complex_tree_expression_when_visitExpression_then_expect_correct_value() {

        final GrammarParser parser = getParser("not false and 0 > 45 + 3 or graal is north or (false and nearby[1,1] = zombie)");
        final GrammarParser.ExpressionContext ctx = parser.expression();

        final Expression expression = expressionVisitor.visitExpression(ctx);

        assertThat(expression.isValid()).isTrue();
        assertThat(expression.isBoolean()).isTrue();
        assertThat(expression.toString()).isEqualTo("not (false and 0 > 45 + 3 or GRAAL is north or (false and nearby[1, 1] = zombie))");

    }


}