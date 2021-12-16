package be.unamur.info.b314.compiler.pils.expressions;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class ExpressionLeafTest {

    @Test
    public void given_leaves_with_primitive_values_when_getSpaceRequirement_then_expect_0() {

        assertThat(ExpressionLeaf.ZERO.getSpaceRequirement()).isEqualTo(SpaceRequirement.NONE);
        assertThat(ExpressionLeaf.FALSE.getSpaceRequirement()).isEqualTo(SpaceRequirement.NONE);
        assertThat(ExpressionLeaf.DIRT.getSpaceRequirement()).isEqualTo(SpaceRequirement.NONE);
    }
}