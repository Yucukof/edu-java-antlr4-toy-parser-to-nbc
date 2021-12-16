package be.unamur.info.b314.compiler.mappers.statements;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.structures.StructureWhile;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionNode;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import be.unamur.info.b314.compiler.pils.statements.StatementSkip;
import be.unamur.info.b314.compiler.pils.statements.StatementWhile;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Anthony DI STASIO
 */
public class StatementWhileMapperTest {
    private Context context;
    private StatementWhileMapper mapper;

    @Before
    public void before() {
        context = new Context();
        mapper = context.mappers.getStatementWhileMapper();
    }

    @Test
    public void given_simple_statement_while_mapper_apply_then_expect_structure_while() {
        final Variable variable = Variable.builder()
                .name("x")
                .type(Type.INTEGER)
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

        final StatementWhile statementWhile = StatementWhile.builder()
                .guard(expression)
                .statements(Collections.singletonList(skip))
                .build();

        assertThat(statementWhile.isValid()).isTrue();

        context.memory.allocate(statementWhile.getSpaceRequirement());

        final StructureWhile structureWhile = mapper.apply(statementWhile);

        assertThat(structureWhile).isNotNull();
        assertThat(structureWhile.isValid()).isTrue();
    }

}
