package be.unamur.info.b314.compiler.mappers.values.integers;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.Environment;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.values.integers.Position;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class PositionMapperTest {

    private PositionMapper mapper;

    @Before
    public void before() throws Exception {
        mapper = new Context().mappers.getPositionMapper();
    }

    @Test
    public void given_longitude_when_mapper_apply_then_expect_argument() {

        final Position position = Position.LONGITUDE;
        assertThat(position.isValid()).isTrue();

        final Argument argument = mapper.apply(position);
        assertThat(argument).isNotNull().isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo(Environment.PositionVariable.LONGITUDE.name());

    }

    @Test
    public void given_latitude_when_mapper_apply_then_expect_argument() {

        final Position position = Position.LATITUDE;
        assertThat(position.isValid()).isTrue();

        final Argument argument = mapper.apply(position);
        assertThat(argument).isNotNull().isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo(Environment.PositionVariable.LATITUDE.name());

    }

    @Test
    public void given_grid_size_when_mapper_apply_then_expect_argument() {

        final Position position = Position.GRID_SIZE;
        assertThat(position.isValid()).isTrue();

        final Argument argument = mapper.apply(position);
        assertThat(argument).isNotNull().isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo(Environment.PositionVariable.GRID_SIZE.name());

    }
}