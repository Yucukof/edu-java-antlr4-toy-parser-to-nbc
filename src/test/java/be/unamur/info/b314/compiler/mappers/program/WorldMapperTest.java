package be.unamur.info.b314.compiler.mappers.program;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.program.Program;
import be.unamur.info.b314.compiler.pils.clauses.ClauseDefault;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionNode;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import be.unamur.info.b314.compiler.pils.program.World;
import be.unamur.info.b314.compiler.pils.statements.Statement;
import be.unamur.info.b314.compiler.pils.statements.StatementCompute;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class WorldMapperTest {

    private WorldMapper mapper;

    @Before
    public void before() {
        mapper = new Context().mappers.getWorldMapper();
    }

    @Test
    public void given_simple_world_when_mapper_apply_then_expect_program() {

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.AND)
              .left(ExpressionLeaf.TRUE)
              .right(ExpressionLeaf.FALSE)
              .build();
        assertThat(expression.isValid()).isTrue();

        final Statement statement = StatementCompute.builder()
              .expression(expression)
              .build();
        assertThat(statement.isValid()).isTrue();

        final ClauseDefault clauseDefault = ClauseDefault.builder()
              .statement(statement)
              .build();
        assertThat(clauseDefault.isValid()).isTrue();

        final World world = World.builder()
              .clauseDefault(clauseDefault)
              .build();
        assertThat(world.isValid()).isTrue();
        assertThat(world.toString()).isEqualTo(
              "declare and retain\n" +
                    "by default\n" +
                    "do\n" +
                    "\tcompute true and false\n" +
                    "done");

        final Program program = mapper.apply(world);
        assertThat(program).isNotNull();
        assertThat(program.isValid()).isTrue();
        assertThat(program.toString()).isEqualTo(
              "thread World" +
                    "\n\tcall DEFAULT_1" +
                    "\nendt" +
                    "\n" +
                    "\nsubroutine DEFAULT_1" +
                    "\n\t" +
                    "\n\treturn" +
                    "\nends");
    }

}