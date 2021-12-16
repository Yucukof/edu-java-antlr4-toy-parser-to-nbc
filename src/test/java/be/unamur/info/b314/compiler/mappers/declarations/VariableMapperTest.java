package be.unamur.info.b314.compiler.mappers.declarations;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.definitions.DefinitionVariable;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class VariableMapperTest {

    private VariableMapper mapper;

    @Before
    public void before() {
        mapper = new Context().mappers.getVariableMapper();
    }

    @Test
    public void given_scalar_boolean_variable_declaration_when_mapper_apply_then_expect_definition_variable() {

        final Variable variable = Variable.builder()
              .name("test")
              .type(Type.BOOLEAN)
              .dimensions(Collections.emptyList())
              .build();
        variable.set(ExpressionLeaf.TRUE);
        assertThat(variable.isValid()).isTrue();

        final DefinitionVariable declaration = mapper.apply(variable);
        assertThat(declaration).isNotNull();
        assertThat(declaration.isValid()).isTrue();
        assertThat(declaration.toString()).isEqualTo("test byte = 1");
    }

    @Test
    public void given_scalar_integer_variable_declaration_when_mapper_apply_then_expect_definition_variable() {

        final Variable variable = Variable.builder()
              .name("test")
              .type(Type.INTEGER)
              .dimensions(Collections.emptyList())
              .build();
        variable.set(ExpressionLeaf.ONE);
        assertThat(variable.isValid()).isTrue();

        final DefinitionVariable declaration = mapper.apply(variable);
        assertThat(declaration).isNotNull();
        assertThat(declaration.isValid()).isTrue();
        assertThat(declaration.toString()).isEqualTo("test sdword = 1");
    }

    @Test
    public void given_scalar_square_variable_declaration_when_mapper_apply_then_expect_definition_variable() {

        final Variable variable = Variable.builder()
              .name("test")
              .type(Type.SQUARE)
              .dimensions(Collections.emptyList())
              .build();
        variable.set(ExpressionLeaf.DIRT);
        assertThat(variable.isValid()).isTrue();

        final DefinitionVariable declaration = mapper.apply(variable);
        assertThat(declaration).isNotNull();
        assertThat(declaration.isValid()).isTrue();
        assertThat(declaration.toString()).isEqualTo("test sbyte = 0");
    }

    @Test
    public void given_array_integer_variable_declaration_when_mapper_apply_then_expect_definition_variable() {

        final Variable variable = Variable.builder()
              .name("test")
              .type(Type.INTEGER)
              .dimensions(Arrays.asList(2, 2))
              .build();
        assertThat(variable.isValid()).isTrue();

        final DefinitionVariable declaration = mapper.apply(variable);
        assertThat(declaration).isNotNull();
        assertThat(declaration.isValid()).isTrue();
        assertThat(declaration.toString()).isEqualTo("test sdword[][]");
    }

}