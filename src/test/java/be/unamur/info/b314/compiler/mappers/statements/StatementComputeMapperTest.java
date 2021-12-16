package be.unamur.info.b314.compiler.mappers.statements;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.statements.StatementCompute;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class StatementComputeMapperTest {

    private StatementComputeMapper mapper;

    @Before
    public void before(){
        mapper = new Context().mappers.getStatementComputeMapper();
    }

    @Test
    public void given_simple_compute_statement_when_mapper_apply_then_expect_structure(){

        final StatementCompute compute = StatementCompute.builder()
              .expression(ExpressionLeaf.ONE)
              .build();
        assertThat(compute.isValid()).isTrue();

        final StructureSimple structure  = mapper.apply(compute);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
    }

}