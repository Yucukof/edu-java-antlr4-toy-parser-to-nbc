package be.unamur.info.b314.compiler.mappers.values.integers;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.Environment;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.keywords.Item;
import be.unamur.info.b314.compiler.pils.values.integers.Count;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Hadrien BAILLY
 */
public class CountMapperTest {

    private CountMapper mapper;

    @Before
    public void before() throws Exception {
        mapper = new Context().mappers.getCountMapper();
    }

    @Test
    public void given_count_radio_when_mapper_apply_then_expect_argument() {

        final Count count = Count.builder()
              .item(Item.RADIO)
              .build();
        assertThat(count.isValid()).isTrue();

        final Argument argument = mapper.apply(count);
        assertThat(argument).isNotNull().isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo(Environment.ItemVariable.RADIO.name());

    }

    @Test
    public void given_count_map_when_mapper_apply_then_expect_argument() {

        final Count count = Count.builder()
              .item(Item.MAP)
              .build();
        assertThat(count.isValid()).isTrue();

        final Argument argument = mapper.apply(count);
        assertThat(argument).isNotNull().isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo(Environment.ItemVariable.MAP.name());

    }

    @Test
    public void given_count_ammo_when_mapper_apply_then_expect_argument() {

        final Count count = Count.builder()
              .item(Item.AMMO)
              .build();
        assertThat(count.isValid()).isTrue();

        final Argument argument = mapper.apply(count);
        assertThat(argument).isNotNull().isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo(Environment.ItemVariable.AMMO.name());

    }

    @Test
    public void given_count_fruits_when_mapper_apply_then_expect_argument() {

        final Count count = Count.builder()
              .item(Item.FRUITS)
              .build();
        assertThat(count.isValid()).isTrue();

        final Argument argument = mapper.apply(count);
        assertThat(argument).isNotNull().isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo(Environment.ItemVariable.FRUITS.name());

    }

    @Test
    public void given_count_soda_when_mapper_apply_then_expect_argument() {

        final Count count = Count.builder()
              .item(Item.SODA)
              .build();
        assertThat(count.isValid()).isTrue();

        final Argument argument = mapper.apply(count);
        assertThat(argument).isNotNull().isInstanceOf(Identifier.class);
        assertThat(argument.isValid()).isTrue();
        assertThat(argument.toString()).isEqualTo(Environment.ItemVariable.SODA.name());

    }
}