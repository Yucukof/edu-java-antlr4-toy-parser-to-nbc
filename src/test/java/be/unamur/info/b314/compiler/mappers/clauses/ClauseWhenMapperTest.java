package be.unamur.info.b314.compiler.mappers.clauses;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.structures.StructureWhen;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.clauses.ClauseWhen;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionNode;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import be.unamur.info.b314.compiler.pils.statements.StatementSkip;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class ClauseWhenMapperTest {

    private static ClauseWhenMapper mapper;
    private static Context context;

    @BeforeClass
    public static void beforeClass() {
        context = new Context();
        mapper = context.mappers.getClauseWhenMapper();
    }

    @Test
    public void given_simple_clauseWhen_when_mapper_apply_then_expect_structure_when() {
        final Variable variable = Variable.builder()
                .name("x")
                .type(Type.INTEGER)
                .identifier(new Identifier("test"))
                .build();
        final VariableReference reference = VariableReference.builder()
                .variable(variable)
                .build();
        final Expression leaf = new ExpressionLeaf(reference);
        final Expression expression = ExpressionNode.builder()
                .operator(Operator.GRT)
                .left(leaf)
                .right(ExpressionLeaf.ONE)
                .build();

        final StatementSkip skip = new StatementSkip();

        final ClauseWhen clauseWhen = ClauseWhen.builder()
              .guard(expression)
              .statements(Collections.singletonList(skip))
              .build();

        assertThat(clauseWhen.isValid()).isTrue();

        context.memory.allocate(clauseWhen.getSpaceRequirement());

        final StructureWhen when = mapper.apply(clauseWhen);
        assertThat(when).isNotNull();
        assertThat(when.isValid()).isTrue();
    }

}