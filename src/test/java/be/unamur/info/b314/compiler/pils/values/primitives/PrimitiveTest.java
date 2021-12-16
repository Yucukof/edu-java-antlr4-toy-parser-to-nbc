package be.unamur.info.b314.compiler.pils.values.primitives;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class PrimitiveTest {

    @Test
    public void given_primitive_values_when_getSpaceRequirement_then_expect_0() {

        assertThat(PrimitiveInteger.ZERO.getSpaceRequirement()).isEqualTo(SpaceRequirement.NONE);
        assertThat(PrimitiveBoolean.FALSE.getSpaceRequirement()).isEqualTo(SpaceRequirement.NONE);
        assertThat(PrimitiveSquare.DIRT.getSpaceRequirement()).isEqualTo(SpaceRequirement.NONE);
    }
}
