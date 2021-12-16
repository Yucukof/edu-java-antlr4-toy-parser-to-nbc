package be.unamur.info.b314.compiler.mappers.values.squares;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionNode;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import be.unamur.info.b314.compiler.pils.values.squares.Nearby;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class NearbyPreprocessorTest {

    private static Context context;
    private static NearbyPreprocessor preprocessor;

    @Before
    public void before() throws Exception {
        context = new Context();
        preprocessor = context.mappers.getNearbyPreprocessor();
    }

    @Test
    public void given_constant_nearby_when_preprocessorApply_then_expect_empty_structure() {
        final Nearby nearby = Nearby.builder()
              .coordinates(new ImmutablePair<>(ExpressionLeaf.ONE, ExpressionLeaf.ONE))
              .build();
        assertThat(nearby.isValid()).isTrue();
        assertThat(nearby.isConstant()).isTrue();

        context.memory.allocate(nearby.getSpaceRequirement());

        final StructureSimple structure = preprocessor.apply(nearby);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(0);
    }

    @Test
    public void given_semi_variable_nearby_when_preprocessorApply_then_expect_statements() {
        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.INTEGER)
              .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
              .variable(variable)
              .build();
        assertThat(reference.isValid()).isTrue();

        final Expression leaf = new ExpressionLeaf(reference);
        assertThat(leaf.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(leaf)
              .right(ExpressionLeaf.ONE)
              .build();

        final Nearby nearby = Nearby.builder()
              .coordinates(new ImmutablePair<>(expression, ExpressionLeaf.ONE))
              .build();
        assertThat(nearby.isValid()).isTrue();
        assertThat(nearby.isConstant()).isFalse();

        context.memory.allocate(nearby.getSpaceRequirement());

        final StructureSimple structure = preprocessor.apply(nearby);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(2);
        assertThat(structure.toString()).isEqualTo("add integer1, x, 1\nsub integer1, integer1, 1");
    }

    @Test
    public void given_variable_nearby_when_preprocessorApply_then_expect_statements() {
        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.INTEGER)
              .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
              .variable(variable)
              .build();
        assertThat(reference.isValid()).isTrue();

        final Expression leaf = new ExpressionLeaf(reference);
        assertThat(leaf.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.ADD)
              .left(leaf)
              .right(ExpressionLeaf.ONE)
              .build();

        final Nearby nearby = Nearby.builder()
              .coordinates(new ImmutablePair<>(expression, expression))
              .build();
        assertThat(nearby.isValid()).isTrue();
        assertThat(nearby.isConstant()).isFalse();

        context.memory.allocate(nearby.getSpaceRequirement());

        final StructureSimple structure = preprocessor.apply(nearby);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(4);
        assertThat(structure.toString()).isEqualTo("add integer1, x, 1\nsub integer1, integer1, 1\nadd integer2, x, 1\nsub integer2, integer2, 1");
    }
}