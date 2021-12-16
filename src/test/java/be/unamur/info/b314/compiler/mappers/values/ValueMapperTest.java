package be.unamur.info.b314.compiler.mappers.values;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.constants.ConstantNumerical;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.nbc.symbols.IndexedIdentifier;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveBoolean;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveInteger;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveSquare;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class ValueMapperTest {

    private ValueMapper mapper;

    @Before
    public void before() {
        mapper = new Context().mappers.getValueMapper();
    }

    @Test
    public void given_constant_integer_when_mapper_apply_then_expect_argument() {
        final PrimitiveInteger zero = PrimitiveInteger.ZERO;

        final Argument argument = mapper.apply(zero);
        assertThat(argument)
              .isNotNull()
              .isInstanceOf(ConstantNumerical.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo("0");
    }

    @Test
    public void given_constant_boolean_when_mapper_apply_then_expect_argument() {
        final PrimitiveBoolean zero = PrimitiveBoolean.FALSE;

        final Argument argument = mapper.apply(zero);
        assertThat(argument)
              .isNotNull()
              .isInstanceOf(ConstantNumerical.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo("0");
    }

    @Test
    public void given_constant_square_when_mapper_apply_then_expect_argument() {
        final PrimitiveSquare zero = PrimitiveSquare.DIRT;

        final Argument argument = mapper.apply(zero);
        assertThat(argument)
              .isNotNull()
              .isInstanceOf(ConstantNumerical.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo("0");
    }

    @Test
    public void given_scalar_variable_when_mapper_apply_then_expect_argument() {

        final Variable variable = Variable.builder()
              .name("test")
              .type(Type.INTEGER)
              .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
              .variable(variable)
              .build();
        assertThat(reference.isValid()).isTrue();

        final Argument argument = mapper.apply(reference);
        assertThat(argument)
              .isNotNull()
              .isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo("test");
    }

    @Test
    public void given_constant_array_variable_when_mapper_apply_then_expect_argument() {

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

        final Argument argument = mapper.apply(reference);
        assertThat(argument)
              .isNotNull()
              .isInstanceOf(IndexedIdentifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo("test[1][0]");
    }
}