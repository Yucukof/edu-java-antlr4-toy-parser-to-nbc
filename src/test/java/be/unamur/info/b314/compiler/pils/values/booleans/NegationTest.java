package be.unamur.info.b314.compiler.pils.values.booleans;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionNode;
import be.unamur.info.b314.compiler.pils.values.Value;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveBoolean;
import be.unamur.info.b314.compiler.visitors.ExpressionVisitor;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Hadrien BAILLY
 */
public class NegationTest extends UnitTestTemplate {

    @Test
    public void given_negation_instruction_when_visitExpression_then_expect_negated_value() {

        final GrammarParser parser = getParser("not true");
        final GrammarParser.ExpressionContext ctx = parser.expression();

        final Expression expression = new ExpressionVisitor(new Context()).visitExpression(ctx);
        assertThat(expression).isInstanceOf(ExpressionLeaf.class);

        final ExpressionLeaf leaf = (ExpressionLeaf) expression;
        assertThat(leaf.getValue()).isInstanceOf(Negation.class);

        final Negation negation = (Negation) leaf.getValue();
        assertThat(negation.getExpression()).isInstanceOf(ExpressionLeaf.class);

        final Value value = negation.getExpression().getValue();
        assertThat(value).isInstanceOf(PrimitiveBoolean.class);
        assertThat(value).isEqualTo(PrimitiveBoolean.TRUE);

        assertThat(negation.getBoolValue()).isFalse();
        assertThat(expression.getValue().getBoolValue()).isFalse();

    }

    @Test
    public void given_negation_and_AND_operation_instruction_when_visitExpression_then_expect_negated_value() {

        final GrammarParser parser = getParser("not false and false");
        final GrammarParser.ExpressionContext ctx = parser.expression();

        final Expression expression = new ExpressionVisitor(new Context()).visitExpression(ctx);
        assertThat(expression).isInstanceOf(ExpressionLeaf.class);

        final Negation negation = (Negation) expression.getValue();
        assertThat(negation.getExpression()).isInstanceOf(ExpressionNode.class);

        final Value value = negation.getExpression().getValue();
        assertThat(value).isInstanceOf(PrimitiveBoolean.class);
        assertThat(value).isEqualTo(PrimitiveBoolean.FALSE);

        assertThat(negation.getBoolValue()).isTrue();
        assertThat(expression.getValue().getBoolValue()).isTrue();

    }

    @Test
    public void given_negation_and_LESS_operation_instruction_when_visitExpression_then_expect_negated_value() {

        final GrammarParser parser = getParser("not 2 < 1");
        final GrammarParser.ExpressionContext ctx = parser.expression();

        final Expression expression = new ExpressionVisitor(new Context()).visitExpression(ctx);
        assertThat(expression).isInstanceOf(ExpressionLeaf.class);
        assertThat(expression.getValue()).isInstanceOf(Negation.class);

        final Negation negation = (Negation) expression.getValue();
        assertThat(negation.getExpression()).isInstanceOf(ExpressionNode.class);

        final Value value = negation.getExpression().getValue();
        assertThat(value).isInstanceOf(PrimitiveBoolean.class);
        assertThat(value).isEqualTo(PrimitiveBoolean.FALSE);

        assertThat(negation.getBoolValue()).isTrue();
        assertThat(expression.getValue().getBoolValue()).isTrue();

    }

    @Test
    public void given_invalid_boolean_negation_when_visitExpression_then_expect_exception() {

        final GrammarParser parser = getParser("not ennemi");
        final GrammarParser.ExpressionContext context = parser.expression();

        assertThatThrownBy(() -> new ExpressionVisitor(new Context()).visitExpression(context))
              .isInstanceOf(InvalidDataTypeException.class);
    }

}