package be.unamur.info.b314.compiler.pils.declarations;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.listeners.PilsToNbcConverter;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidNumberOfIndexesException;
import be.unamur.info.b314.compiler.pils.exceptions.NegativeArrayIndexException;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.keywords.Square;
import be.unamur.info.b314.compiler.pils.program.Program;
import be.unamur.info.b314.compiler.pils.program.World;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveSquare;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Hadrien BAILLY
 */
public class VariableTest extends UnitTestTemplate {

    private static final List<Integer> dimensions = Arrays.asList(2, 2);
    private static final List<Integer> indexes = Arrays.asList(1, 1);
    private static final List<Integer> dimension = Collections.singletonList(1);
    private static final List<Integer> index = Collections.singletonList(0);
    private Context symbols;
    private Variable variable;

    @Before
    public void before() {
        this.symbols = new Context();
        this.variable = Variable.builder().build();
    }

    @Test
    public void given_simple_variable_when_from_context_then_expect_object() {
        final GrammarParser parser = getParser("x as integer;");

        final GrammarParser.VarDeclContext ctx = parser.varDecl();

        final Variable variable = Variable.from(ctx, symbols);

        assertThat(variable.isValid()).isTrue();
        assertThat(variable.isInteger()).isTrue();
        assertThat(variable.getName()).isEqualTo("x");
    }

    @Test
    public void given_array_variable_when_from_context_then_expect_object() {
        final GrammarParser parser = getParser("x as integer[2,2];");

        final GrammarParser.VarDeclContext ctx = parser.varDecl();

        final Variable variable = Variable.from(ctx, symbols);

        assertThat(variable.isValid()).isTrue();
        assertThat(variable.isInteger()).isTrue();
        assertThat(variable.getName()).isEqualTo("x");
        assertThat(variable.getDimensions().size()).isEqualTo(2);
        assertThat(variable.getDimensions().get(0)).isEqualTo(2);
        assertThat(variable.getDimensions().get(1)).isEqualTo(2);
    }

    @Test
    public void given_array_variable_with_0_dimension_when_from_context_then_expect_object() {
        final GrammarParser parser = getParser("x as integer[0];");

        final GrammarParser.VarDeclContext ctx = parser.varDecl();

        final Variable variable = Variable.from(ctx, symbols);

        assertThat(variable.isValid()).isTrue();
        assertThat(variable.isInteger()).isTrue();
        assertThat(variable.getName()).isEqualTo("x");
        assertThat(variable.getDimensions().size()).isEqualTo(1);
        assertThat(variable.getDimensions().get(0)).isEqualTo(0);
    }

    @Test
    public void given_variable_with_no_dimension_when_isScalar_then_expect_true() {
        assertThat(variable).isNotNull();
        assertThat(variable.isScalar()).isTrue();

        variable.setDimensions(new ArrayList<>());
        assertThat(variable.isScalar()).isTrue();
    }

    @Test
    public void given_variable_with_any_dimension_when_isScalar_then_expect_false() {

        variable.setDimensions(dimension);
        assertThat(variable.isScalar()).isFalse();

        variable.setDimensions(indexes);
        assertThat(variable.isScalar()).isFalse();
    }

    @Test
    public void given_variable_without_type_and_value_when_get_then_expect_exception() {
        assertThat(variable.getType()).isNull();
        assertThat(variable.getValues().size()).isEqualTo(0);
        assertThat(variable.isScalar()).isTrue();
        assertThatThrownBy(() -> variable.get()).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void given_variable_without_value_but_with_type_when_get_then_expect_default_value() {
        variable.setType(Type.INTEGER);
        variable.setValues(null);
        assertThat(variable.getType()).isNotNull().isEqualTo(Type.INTEGER);
        assertThat(variable.getValues()).isNull();
        assertThat(variable.isScalar()).isTrue();
        assertThat(variable.get()).isNotNull();
        assertThat(variable.get().getIntValue()).isNull();

        variable.setType(Type.SQUARE);
        variable.setValues(null);
        assertThat(variable.getType()).isNotNull().isEqualTo(Type.SQUARE);
        assertThat(variable.getValues()).isNull();
        assertThat(variable.isScalar()).isTrue();
        assertThat(variable.get()).isNotNull().isInstanceOf(PrimitiveSquare.class);
        assertThat(variable.get().getSquareValue()).isNull();

        variable.setType(Type.BOOLEAN);
        variable.setValues(null);
        assertThat(variable.getType()).isNotNull().isEqualTo(Type.BOOLEAN);
        assertThat(variable.getValues()).isNull();
        assertThat(variable.isScalar()).isTrue();
        assertThat(variable.get()).isNotNull();
        assertThat(variable.get().isBoolean()).isTrue();
        assertThat(variable.get().getBoolValue()).isNull();
    }

    @Test
    public void given_variable_with_value_and_type_when_get_then_expect_value() {
        variable.setType(Type.INTEGER);
        variable.set(ExpressionLeaf.ONE);
        assertThat(variable.getType()).isNotNull().isEqualTo(Type.INTEGER);
        assertThat(variable.getValues()).isNotNull();
        assertThat(variable.isScalar()).isTrue();
        assertThat(variable.get().isInteger()).isTrue();
        assertThat(variable.get().getIntValue()).isEqualTo(1);

        variable.setType(Type.SQUARE);
        variable.set(ExpressionLeaf.DIRT);
        assertThat(variable.getType()).isNotNull().isEqualTo(Type.SQUARE);
        assertThat(variable.getValues()).isNotNull();
        assertThat(variable.isScalar()).isTrue();
        assertThat(variable.get().isSquare()).isTrue();
        assertThat(variable.get().getSquareValue()).isNotNull().isEqualTo(Square.DIRT);

        variable.setType(Type.BOOLEAN);
        variable.set(ExpressionLeaf.TRUE);
        assertThat(variable.getType()).isNotNull().isEqualTo(Type.BOOLEAN);
        assertThat(variable.getValues()).isNotNull();
        assertThat(variable.isScalar()).isTrue();
        assertThat(variable.get().isBoolean()).isTrue();
        assertThat(variable.get().getBoolValue()).isEqualTo(true);
    }

    @Test
    public void given_valid_indexes_when_walker_then_expect_variable_ref() {
        final String input = "declare and retain\n" +
              "    arena as square[7,7];\n" +
              "by default\n" +
              "do\n" +
              "    set arena[0,0] to player\n" +
              "    set arena[0,1] to graal\n" +
              "    set arena[0,2] to ennemi\n" +
              "    set arena[0,3] to zombie\n" +
              "    set arena[0,4] to radar\n" +
              "    set arena[0,5] to map\n" +
              "    set arena[0,6] to radio\n" +
              "done";
        final GrammarParser parser = getParser(input);

        final GrammarParser.WorldContext ctx = parser.world();
        final PilsToNbcConverter listener = new PilsToNbcConverter(getWld());
        listener.setProgram(new World());

        final ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, ctx);

        final Program program = listener.getProgram();
        assertThat(program.getDeclarations().size()).isEqualTo(1);
        assertThat(program.getClauseDefault().getStatements().size()).isEqualTo(7);

    }

    @Test
    public void given_invalid_number_of_indexes_when_walker_then_expect_exception() {
        final String input = "declare and retain " +
              "x as integer[2,2]; " +
              "by default do " +
              "set x[1] to 1 " +
              "done ";
        final GrammarParser parser = getParser(input);

        final GrammarParser.WorldContext ctx = parser.world();
        final PilsToNbcConverter listener = new PilsToNbcConverter(getWld());
        listener.setProgram(new World());

        final ParseTreeWalker walker = new ParseTreeWalker();
        assertThatThrownBy(() -> walker.walk(listener, ctx)).isInstanceOf(InvalidNumberOfIndexesException.class);

    }

    @Test
    public void given_negative_index_when_walker_then_expect_exception() {
        final String input = "declare and retain " +
              "x as integer[2,2]; " +
              "by default do " +
              "set x[-1,1] to 1 " +
              "done ";
        final GrammarParser parser = getParser(input);

        final GrammarParser.WorldContext ctx = parser.world();
        final PilsToNbcConverter listener = new PilsToNbcConverter(getWld());
        listener.setProgram(new World());

        final ParseTreeWalker walker = new ParseTreeWalker();
        assertThatThrownBy(() -> walker.walk(listener, ctx)).isInstanceOf(NegativeArrayIndexException.class);
    }

    // TODO 27/04/2021 [HBA]: Créer un set pour l'opération set
    // TODO 27/04/2021 [HBA]: Créer un set pour l'opération setAt

}