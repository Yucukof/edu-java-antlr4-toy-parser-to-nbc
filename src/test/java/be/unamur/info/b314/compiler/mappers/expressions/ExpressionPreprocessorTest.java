package be.unamur.info.b314.compiler.mappers.expressions;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionNode;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import be.unamur.info.b314.compiler.pils.values.booleans.Negation;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class ExpressionPreprocessorTest {

    private Context context;
    private ExpressionPreprocessor mapper;

    @Before
    public void before() {
        context = new Context();
        mapper = context.mappers.getExpressionPreprocessor();
    }

    @Test
    public void given_constant_expressionLeaf_when_preprocessor_apply_then_expect_zero_statement() {

        final Expression expression = ExpressionLeaf.ZERO;
        assertThat(expression.isValid()).isTrue();

        final StructureSimple structure = mapper.apply(expression);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(0);
    }

    @Test
    public void given_simple_constant_addition_when_preprocessor_apply_then_expect_zero_statement() {

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(ExpressionLeaf.ZERO)
              .right(ExpressionLeaf.ONE)
              .build();
        assertThat(expression.isValid()).isTrue();

        final StructureSimple structure = mapper.apply(expression);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(0);
    }

    @Test
    public void given_chained_constant_addition_when_preprocessor_apply_then_expect_zero_statements() {

        final Expression subExpression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(ExpressionLeaf.ZERO)
              .right(ExpressionLeaf.ONE)
              .build();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(subExpression)
              .right(ExpressionLeaf.ONE)
              .build();
        assertThat(expression.isValid()).isTrue();

        final StructureSimple structure = mapper.apply(expression);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(0);
    }

    @Test
    public void given_constant_inverted_tree_when_preprocessor_apply_then_expect_zero_statement() {

        final Expression subExpression = ExpressionNode.builder()
              .operator(Operator.MUL)
              .left(ExpressionLeaf.ZERO)
              .right(ExpressionLeaf.ONE)
              .build();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(ExpressionLeaf.ONE)
              .right(subExpression)
              .build();
        assertThat(expression.isValid()).isTrue();

        final StructureSimple structure = mapper.apply(expression);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(0);
    }

    @Test
    public void given_constant_binary_tree_when_preprocessor_apply_then_expect_zero_statement() {

        final Expression subExpression1 = ExpressionNode.builder()
              .operator(Operator.EQ)
              .left(ExpressionLeaf.ZERO)
              .right(ExpressionLeaf.ONE)
              .build();

        final Expression subExpression2 = ExpressionNode.builder()
              .operator(Operator.EQ)
              .left(ExpressionLeaf.TRUE)
              .right(ExpressionLeaf.FALSE)
              .build();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.AND)
              .left(subExpression1)
              .right(subExpression2)
              .build();
        assertThat(expression.isValid()).isTrue();
        assertThat(expression.toString()).isEqualTo("0 = 1 and true = false");

        final StructureSimple structure = mapper.apply(expression);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(0);

    }

    @Test
    public void given_expressionLeaf_with_primitive_negation_when_preprocessor_apply_then_expect_zero_statement() {

        final Negation negation = Negation.builder()
              .expression(ExpressionLeaf.TRUE)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.isPrimitive()).isTrue();

        final Expression expression = new ExpressionLeaf(negation);
        assertThat(expression.isValid()).isTrue();

        final StructureSimple structure = mapper.apply(expression);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(0);
    }

    @Test
    public void given_expressionLeaf_with_variable_negation_when_preprocessor_apply_then_expect_zero_statement() {

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.BOOLEAN)
              .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
              .variable(variable)
              .build();
        assertThat(reference.isValid()).isTrue();

        final Expression leaf = new ExpressionLeaf(reference);
        assertThat(leaf.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(leaf)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.isPrimitive()).isFalse();

        final Expression expression = new ExpressionLeaf(negation);
        assertThat(expression.isValid()).isTrue();
        assertThat(expression.toString()).isEqualTo("not (x)");

        context.memory.allocate(0, 1, 0);

        final StructureSimple structure = mapper.apply(expression);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(1);
        assertThat(structure.toString()).isEqualTo("neg boolean1, x");
    }
}