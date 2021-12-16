package be.unamur.info.b314.compiler.mappers.clauses;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.definitions.DefinitionVariable;
import be.unamur.info.b314.compiler.nbc.routines.Routine;
import be.unamur.info.b314.compiler.pils.clauses.ClauseDefault;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionNode;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import be.unamur.info.b314.compiler.pils.statements.StatementCompute;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveBoolean;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

import static be.unamur.info.b314.compiler.mappers.NameFactory.NameCategory.DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class ClauseDefaultMapperTest {

    private static Context context;
    private static ClauseDefaultMapper mapper;

    @BeforeClass
    public static void beforeClass() {
        context = new Context();
        mapper = context.mappers.getClauseDefaultMapper();
    }

    @Test
    public void given_simple_clauseDefault_when_mapper_apply_then_expect_routine() {

        final Variable variable = Variable.builder()
              .name("test")
              .type(Type.INTEGER)
              .build();
        variable.set(ExpressionLeaf.ONE);
        assertThat(variable.isValid()).isTrue();

        final ExpressionLeaf left = ExpressionLeaf.builder()
              .value(PrimitiveBoolean.TRUE)
              .build();

        final ExpressionLeaf right = ExpressionLeaf.builder()
              .value(PrimitiveBoolean.FALSE)
              .build();

        final ExpressionNode expression = ExpressionNode.builder()
              .left(left)
              .right(right)
              .operator(Operator.EQ)
              .build();

        final StatementCompute compute = StatementCompute.builder()
              .expression(expression)
              .build();

        final ClauseDefault clauseDefault = ClauseDefault.builder()
              .declarations(Collections.singletonList(variable))
              .statements(Collections.singletonList(compute))
              .build();

        context.names.createNew(DEFAULT);

        final Routine routine = mapper.apply(clauseDefault);
        assertThat(routine).isNotNull();
        assertThat(routine.isValid()).isTrue();
        assertThat(routine.getSegments().size()).isEqualTo(1);
        assertThat(routine.getSegments().get(0).getDefinitions().size()).isEqualTo(1);
        assertThat(routine.getSegments().get(0).getDefinitions().get(0))
              .isInstanceOf(DefinitionVariable.class);
    }

}