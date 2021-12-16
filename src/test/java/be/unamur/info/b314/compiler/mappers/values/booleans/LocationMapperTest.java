package be.unamur.info.b314.compiler.mappers.values.booleans;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.Environment;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.keywords.Direction;
import be.unamur.info.b314.compiler.pils.keywords.Target;
import be.unamur.info.b314.compiler.pils.values.booleans.Location;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class LocationMapperTest {

    private LocationMapper mapper;

    @Before
    public void before() {
        mapper = new Context().mappers.getLocationMapper();
    }

    @Test
    public void given_graal_is_north_when_mapper_apply_then_expect_argument() {
        final Location location = Location.builder()
              .target(Target.GRAAL)
              .direction(Direction.NORTH)
              .build();
        assertThat(location.isValid()).isTrue();

        final Argument argument = mapper.apply(location);
        assertThat(argument).isNotNull().isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo(Environment.LocationVariable.GRAAL_NORTH.name());

    }

    @Test
    public void given_graal_is_south_when_mapper_apply_then_expect_argument() {
        final Location location = Location.builder()
              .target(Target.GRAAL)
              .direction(Direction.SOUTH)
              .build();
        assertThat(location.isValid()).isTrue();

        final Argument argument = mapper.apply(location);
        assertThat(argument).isNotNull().isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo(Environment.LocationVariable.GRAAL_SOUTH.name());

    }

    @Test
    public void given_graal_is_east_when_mapper_apply_then_expect_argument() {
        final Location location = Location.builder()
              .target(Target.GRAAL)
              .direction(Direction.EAST)
              .build();
        assertThat(location.isValid()).isTrue();

        final Argument argument = mapper.apply(location);
        assertThat(argument).isNotNull().isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo(Environment.LocationVariable.GRAAL_EAST.name());

    }

    @Test
    public void given_graal_is_west_when_mapper_apply_then_expect_argument() {
        final Location location = Location.builder()
              .target(Target.GRAAL)
              .direction(Direction.WEST)
              .build();
        assertThat(location.isValid()).isTrue();

        final Argument argument = mapper.apply(location);
        assertThat(argument).isNotNull().isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo(Environment.LocationVariable.GRAAL_WEST.name());

    }

    @Test
    public void given_enemy_is_north_when_mapper_apply_then_expect_argument() {

        final Location location = Location.builder()
              .target(Target.ENEMY)
              .direction(Direction.NORTH)
              .build();
        assertThat(location.isValid()).isTrue();

        final Argument argument = mapper.apply(location);
        assertThat(argument).isNotNull().isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo(Environment.LocationVariable.ENEMY_NORTH.name());

    }

    @Test
    public void given_enemy_is_south_when_mapper_apply_then_expect_argument() {

        final Location location = Location.builder()
              .target(Target.ENEMY)
              .direction(Direction.SOUTH)
              .build();
        assertThat(location.isValid()).isTrue();

        final Argument argument = mapper.apply(location);
        assertThat(argument).isNotNull().isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo(Environment.LocationVariable.ENEMY_SOUTH.name());

    }

    @Test
    public void given_enemy_is_east_when_mapper_apply_then_expect_argument() {

        final Location location = Location.builder()
              .target(Target.ENEMY)
              .direction(Direction.EAST)
              .build();
        assertThat(location.isValid()).isTrue();

        final Argument argument = mapper.apply(location);
        assertThat(argument).isNotNull().isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo(Environment.LocationVariable.ENEMY_EAST.name());

    }

    @Test
    public void given_enemy_is_west_when_mapper_apply_then_expect_argument() {

        final Location location = Location.builder()
              .target(Target.ENEMY)
              .direction(Direction.WEST)
              .build();
        assertThat(location.isValid()).isTrue();

        final Argument argument = mapper.apply(location);
        assertThat(argument).isNotNull().isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo(Environment.LocationVariable.ENEMY_WEST.name());

    }
}