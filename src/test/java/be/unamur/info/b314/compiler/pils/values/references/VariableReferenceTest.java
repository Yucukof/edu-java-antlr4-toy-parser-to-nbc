package be.unamur.info.b314.compiler.pils.values.references;

import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidNumberOfIndexesException;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveInteger;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Hadrien BAILLY
 */
public class VariableReferenceTest {

    private static final Expression ZERO = new ExpressionLeaf(PrimitiveInteger.ZERO);
    private static final Expression ONE = new ExpressionLeaf(PrimitiveInteger.ONE);
    private static final List<Expression> indexes = Arrays.asList(ZERO, ONE);

    @Test
    public void given_valid_integer_variable_and_indexes_when_assert_array_isValid_then_expect_true_and_correct_type() {

        final Variable variable = Variable.builder()
              .name("TEST")
              .type(Type.INTEGER)
              .dimensions(Arrays.asList(2, 2))
              .build();
        assertThat(variable.isValid()).isTrue();
        assertThat(variable.isInteger()).isTrue();
        assertThat(variable.isArray()).isTrue();

        final VariableReference array = VariableReference.builder()
              .variable(variable)
              .indexes(indexes)
              .build();
        assertThat(array.isValid()).isTrue();

        assertThat(array.getType()).isNotEqualTo(Type.SQUARE);
        assertThat(array.getType()).isEqualTo(Type.INTEGER);
        assertThat(array.getType()).isNotEqualTo(Type.BOOLEAN);

        assertThat(array.isSquare()).isFalse();
        assertThat(array.isInteger()).isTrue();
        assertThat(array.isBoolean()).isFalse();

    }

    @Test
    public void given_valid_square_variable_and_indexes_when_assert_array_isValid_then_expect_true_and_correct_type() {

        final Variable variable = Variable.builder()
              .name("TEST")
              .type(Type.SQUARE)
              .dimensions(Arrays.asList(2, 2))
              .build();
        assertThat(variable.isValid()).isTrue();
        assertThat(variable.isSquare()).isTrue();
        assertThat(variable.isArray()).isTrue();

        final VariableReference array = VariableReference.builder()
              .variable(variable)
              .indexes(indexes)
              .build();
        assertThat(array.isValid()).isTrue();

        assertThat(array.getType()).isEqualTo(Type.SQUARE);
        assertThat(array.getType()).isNotEqualTo(Type.INTEGER);
        assertThat(array.getType()).isNotEqualTo(Type.BOOLEAN);

        assertThat(array.isSquare()).isTrue();
        assertThat(array.isInteger()).isFalse();
        assertThat(array.isBoolean()).isFalse();

    }

    @Test
    public void given_valid_boolean_variable_and_indexes_when_assert_array_isValid_then_expect_true_and_correct_type() {

        final Variable variable = Variable.builder()
              .name("TEST")
              .type(Type.BOOLEAN)
              .dimensions(Arrays.asList(2, 2))
              .build();
        assertThat(variable.isValid()).isTrue();
        assertThat(variable.isBoolean()).isTrue();
        assertThat(variable.isArray()).isTrue();

        final VariableReference array = VariableReference.builder()
              .variable(variable)
              .indexes(indexes)
              .build();
        assertThat(array.isValid()).isTrue();

        assertThat(array.getType()).isNotEqualTo(Type.SQUARE);
        assertThat(array.getType()).isNotEqualTo(Type.INTEGER);
        assertThat(array.getType()).isEqualTo(Type.BOOLEAN);

        assertThat(array.isSquare()).isFalse();
        assertThat(array.isInteger()).isFalse();
        assertThat(array.isBoolean()).isTrue();

    }

    @Test
    public void given_non_array_variable_when_assert_array_isValid_then_expect_false() {

        final Variable variable = Variable.builder()
              .name("TEST")
              .type(Type.INTEGER)
              .build();

        assertThat(variable.isValid()).isTrue();
        assertThat(variable.isInteger()).isTrue();
        assertThat(variable.isArray()).isFalse();

        final VariableReference array = VariableReference.builder()
              .variable(variable)
              .indexes(indexes)
              .build();

        assertThatThrownBy(array::isValid).isInstanceOf(InvalidNumberOfIndexesException.class);

    }

    @Test
    public void given_scalar_array_when_toString_then_only_name() {

        final Variable variable = Variable.builder()
              .name("test")
              .type(Type.BOOLEAN)
              .build();

        assertThat(variable.isValid()).isTrue();
        assertThat(variable.isScalar()).isTrue();

        final VariableReference ref = VariableReference.builder()
              .variable(variable)
              .build();

        assertThat(ref.isValid()).isTrue();
        assertThat(ref.toString()).isEqualTo("test");
    }

    @Test
    public void given_indexed_array_when_toString_then_name_and_indexes() {

        final Variable variable = Variable.builder()
              .name("test")
              .type(Type.BOOLEAN)
              .dimensions(Arrays.asList(2, 2))
              .build();

        assertThat(variable.isValid()).isTrue();
        assertThat(variable.isScalar()).isFalse();

        final VariableReference ref = VariableReference.builder()
              .variable(variable)
              .indexes(indexes)
              .build();

        assertThat(ref.isValid()).isTrue();
        assertThat(ref.toString()).isEqualTo("test[0,1]");
    }

}