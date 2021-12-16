package be.unamur.info.b314.compiler.listeners;

import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidNumberOfIndexesException;
import be.unamur.info.b314.compiler.pils.exceptions.SymbolException;
import be.unamur.info.b314.compiler.pils.keywords.Reserved;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static be.unamur.info.b314.compiler.pils.keywords.Property.ARENA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Hadrien BAILLY
 */
public class ContextTest {

    private static final Variable VARIABLE = Variable.builder().name("test").type(Type.BOOLEAN).build();
    private static Context context;

    @BeforeClass
    public static void beforeClass() {
        assertThat(VARIABLE.isValid()).isTrue();
    }

    @Before
    public void before() {
        context = new Context();
        assertThat(context.isValid()).isTrue();
    }

    @Test
    public void given_new_variable_and_empty_table_when_put_then_expect_not_empty() {

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        context.put(VARIABLE);

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(1);
        final Variable variable = context.getVariable(VARIABLE.getName());
        assertThat(variable.getName()).isEqualTo(VARIABLE.getName());
        assertThat(variable.getType()).isEqualTo(VARIABLE.getType());
        assertThat(variable.getDimensions()).isEqualTo(VARIABLE.getDimensions());
        assertThat(variable.getLevel()).isEqualTo(context.getDepth());

    }

    @Test
    public void given_duplicate_variable_of_same_level_when_put_then_expect_exception() {

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        context.put(VARIABLE);
        assertThatThrownBy(() -> context.put(VARIABLE)).isInstanceOf(SymbolException.class);

    }

    @Test
    public void given_duplicate_variable_of_different_level_when_put_then_expect_variable() {

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        context.put(VARIABLE);
        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(1);

        context.pushAndPropagate();
        assertThat(context.getDepth()).isEqualTo(1);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(1);

        context.put(VARIABLE);

        assertThat(context.getDepth()).isEqualTo(1);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(1);
        final Variable variable = context.getVariable(VARIABLE.getName());
        assertThat(variable.getName()).isEqualTo(VARIABLE.getName());
        assertThat(variable.getType()).isEqualTo(VARIABLE.getType());
        assertThat(variable.getDimensions()).isEqualTo(VARIABLE.getDimensions());
        assertThat(variable.getLevel()).isEqualTo(context.getDepth());

    }

    @Test
    public void given_non_arena_reserved_word_when_put_then_expect_exception() {

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        Reserved.getKeywords().stream()
              .filter(keyword -> !ARENA.matches(keyword))
              .map(keyword -> Variable.builder().name(keyword).type(Type.BOOLEAN).build())
              .forEach(variable -> assertThatThrownBy(() -> context.put(variable)).isInstanceOf(SymbolException.class));

    }

    @Test
    public void given_arena_reserved_word_and_valid_variable_when_put_then_expect_success() {

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        final Variable arena = Variable.builder()
              .name(ARENA.getToken())
              .type(ARENA.getType())
              .dimensions(ARENA.getDimensions())
              .build();

        context.canDeclareArena(true);
        context.put(arena);

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(1);
        final Variable variable = context.getVariable(arena.getName());
        assertThat(variable.getName()).isEqualTo(arena.getName());
        assertThat(variable.getType()).isEqualTo(arena.getType());
        assertThat(variable.getDimensions()).isEqualTo(arena.getDimensions());
        assertThat(variable.getLevel()).isEqualTo(context.getDepth());

    }

    @Test
    public void given_arena_reserved_word_and_invalid_variable_when_put_then_expect_exception() {

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        final Variable arenaWithoutDimensions = Variable.builder()
              .name(ARENA.getToken())
              .type(ARENA.getType())
              .build();

        context.canDeclareArena(true);
        assertThatThrownBy(() -> context.put(arenaWithoutDimensions)).isInstanceOf(InvalidNumberOfIndexesException.class);

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        final Variable arenaWithBooleanType = Variable.builder()
              .name(ARENA.getToken())
              .type(Type.BOOLEAN)
              .dimensions(ARENA.getDimensions())
              .build();

        assertThatThrownBy(() -> context.put(arenaWithBooleanType)).isInstanceOf(InvalidDataTypeException.class);

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        final Variable arenaWithIntegerType = Variable.builder()
              .name(ARENA.getToken())
              .type(Type.INTEGER)
              .dimensions(ARENA.getDimensions())
              .build();

        assertThatThrownBy(() -> context.put(arenaWithIntegerType)).isInstanceOf(InvalidDataTypeException.class);

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        final Variable arenaWithVoidType = Variable.builder()
              .name(ARENA.getToken())
              .type(Type.INTEGER)
              .dimensions(ARENA.getDimensions())
              .build();

        assertThatThrownBy(() -> context.put(arenaWithVoidType)).isInstanceOf(InvalidDataTypeException.class);

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        final Variable arenaWithInvalidIndexes = Variable.builder()
              .name(ARENA.getToken())
              .type(Type.SQUARE)
              .dimensions(Arrays.asList(1, 2))
              .build();

        assertThatThrownBy(() -> context.put(arenaWithInvalidIndexes)).isInstanceOf(IllegalArgumentException.class);

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);
    }

    @Test
    public void given_level_0_not_empty_table_when_pushAndPropagate_then_expect_unchanged_beside_level() {

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        context.put(VARIABLE);
        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(1);

        context.pushAndPropagate();
        assertThat(context.getDepth()).isEqualTo(1);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(1);

        context.pushAndPropagate();
        assertThat(context.getDepth()).isEqualTo(2);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(1);
    }

    @Test
    public void given_level_0_not_empty_table_when_pushAndReset_then_expect_empty_deeper_table() {

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        context.put(VARIABLE);
        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(1);

        context.pushAndReset();
        assertThat(context.getDepth()).isEqualTo(1);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        context.put(VARIABLE);
        assertThat(context.getDepth()).isEqualTo(1);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(1);

        context.pushAndReset();
        assertThat(context.getDepth()).isEqualTo(2);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);
    }

    @Test
    public void given_existing_variable_from_same_level_when_get_then_expect_variable() {

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        context.put(VARIABLE);
        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(1);

        final Variable variable = context.getVariable(VARIABLE.getName());
        assertThat(variable.getName()).isEqualTo(VARIABLE.getName());
        assertThat(variable.getType()).isEqualTo(VARIABLE.getType());
        assertThat(variable.getDimensions()).isEqualTo(VARIABLE.getDimensions());
        assertThat(variable.getLevel()).isEqualTo(context.getDepth());
    }

    @Test
    public void given_existing_variable_from_higher_level_and_propagation_when_get_then_expect_variable() {

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        context.put(VARIABLE);
        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(1);
        context.pushAndPropagate();
        assertThat(context.getDepth()).isEqualTo(1);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(1);

        final Variable variable = context.getVariable(VARIABLE.getName());
        assertThat(variable.getName()).isEqualTo(VARIABLE.getName());
        assertThat(variable.getType()).isEqualTo(VARIABLE.getType());
        assertThat(variable.getDimensions()).isEqualTo(VARIABLE.getDimensions());
        assertThat(variable.getLevel()).isEqualTo(context.getDepth() - 1);
    }

    @Test
    public void given_existing_variable_from_higher_level_and_reset_when_get_then_expect_exception() {

        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        context.put(VARIABLE);
        assertThat(context.getDepth()).isEqualTo(0);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(1);
        context.pushAndReset();
        assertThat(context.getDepth()).isEqualTo(1);
        assertThat(context.getSymbols().getVariables().size()).isEqualTo(0);

        assertThatThrownBy(() -> context.getVariable(VARIABLE.getName())).isInstanceOf(SymbolException.class);

    }

}