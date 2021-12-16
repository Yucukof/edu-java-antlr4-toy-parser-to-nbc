package be.unamur.info.b314.compiler.nbc.program;

import be.unamur.info.b314.compiler.nbc.declarations.Segment;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.nbc.definitions.Definition;
import be.unamur.info.b314.compiler.nbc.definitions.DefinitionType;
import be.unamur.info.b314.compiler.nbc.keywords.Type;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class SegmentTest {

    @Test
    public void given_empty_segment_when_toString_then_expect_string() {
        final Segment segment = Segment.builder().build();

        final String actual = segment.toString();
        assertThat(actual).isNotEmpty();
        assertThat(actual).isEqualTo("dseg segment\ndseg ends");
    }

    @Test
    public void given_segment_with_one_type_when_toString_then_expect_string() {

        final Definition definition = DefinitionType.builder()
              .existingType(Type.DWORD.getToken())
              .identifier(new Identifier("test"))
              .build();

        final Segment segment = Segment.builder()
              .definition(definition)
              .build();

        final String actual = segment.toString();
        assertThat(actual).isNotEmpty();
        assertThat(actual).isEqualTo("dseg segment\n\ttest typedef dword\ndseg ends");
    }

    @Test
    public void given_segment_with_two_declarations_when_toString_then_expect_string() {

        final Definition definition1 = DefinitionType.builder()
              .existingType(Type.DWORD.getToken())
              .identifier(new Identifier("test"))
              .build();

        final Definition definition2 = DefinitionType.builder()
              .existingType("test")
              .identifier(new Identifier("test2"))
              .build();

        final Segment segment = Segment.builder()
              .definition(definition1)
              .definition(definition2)
              .build();

        final String actual = segment.toString();
        assertThat(actual).isNotEmpty();
        assertThat(actual).isEqualTo("dseg segment\n\ttest typedef dword\n\ttest2 typedef test\ndseg ends");
    }

}