package be.unamur.info.b314.compiler.mappers;

import be.unamur.info.b314.compiler.nbc.declarations.Segment;
import be.unamur.info.b314.compiler.nbc.definitions.Definition;
import be.unamur.info.b314.compiler.nbc.definitions.DefinitionVariable;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class SpaceRequirementTest {

    @Test
    public void given_NONE_space_requirement_when_assign_then_expect_0_values() {
        final SpaceRequirement requirement = SpaceRequirement.NONE;
        assertThat(requirement).isNotNull();
        assertThat(requirement.getInteger()).isEqualTo(0);
        assertThat(requirement.getBool()).isEqualTo(0);
        assertThat(requirement.getSquare()).isEqualTo(0);
    }

    @Test
    public void given_empty_space_requirement_when_add_then_expect_value_modified() {
        final SpaceRequirement requirement = SpaceRequirement.NONE;
        assertThat(requirement).isNotNull();

        assertThat(requirement.getInteger()).isEqualTo(0);
        assertThat(requirement.getBool()).isEqualTo(0);
        assertThat(requirement.getSquare()).isEqualTo(0);

        final SpaceRequirement addInt = requirement.add(Type.INTEGER);

        assertThat(addInt.getInteger()).isEqualTo(1);
        assertThat(addInt.getBool()).isEqualTo(0);
        assertThat(addInt.getSquare()).isEqualTo(0);

        final SpaceRequirement addBool = addInt.add(Type.BOOLEAN);

        assertThat(addBool.getInteger()).isEqualTo(1);
        assertThat(addBool.getBool()).isEqualTo(1);
        assertThat(addBool.getSquare()).isEqualTo(0);

        final SpaceRequirement addSquare = addBool.add(Type.SQUARE);

        assertThat(addSquare.getInteger()).isEqualTo(1);
        assertThat(addSquare.getBool()).isEqualTo(1);
        assertThat(addSquare.getSquare()).isEqualTo(1);
    }

    @Test
    public void given_two_space_requirements_when_merge_then_expect_high_water_mark() {

        SpaceRequirement requirement1 = SpaceRequirement.NONE;
        requirement1 = requirement1.add(Type.INTEGER);
        requirement1 = requirement1.add(Type.INTEGER);
        requirement1 = requirement1.add(Type.INTEGER);
        requirement1 = requirement1.add(Type.BOOLEAN);
        requirement1 = requirement1.add(Type.SQUARE);
        assertThat(requirement1.getInteger()).isEqualTo(3);
        assertThat(requirement1.getBool()).isEqualTo(1);
        assertThat(requirement1.getSquare()).isEqualTo(1);

        SpaceRequirement requirement2 = SpaceRequirement.NONE;
        requirement2 = requirement2.add(Type.INTEGER);
        requirement2 = requirement2.add(Type.INTEGER);
        requirement2 = requirement2.add(Type.BOOLEAN);
        requirement2 = requirement2.add(Type.BOOLEAN);
        requirement2 = requirement2.add(Type.BOOLEAN);
        requirement2 = requirement2.add(Type.SQUARE);
        assertThat(requirement2.getInteger()).isEqualTo(2);
        assertThat(requirement2.getBool()).isEqualTo(3);
        assertThat(requirement2.getSquare()).isEqualTo(1);

        final SpaceRequirement merge = SpaceRequirement.merge(requirement1, requirement2);
        assertThat(merge.getInteger()).isEqualTo(3);
        assertThat(merge.getBool()).isEqualTo(3);
        assertThat(merge.getSquare()).isEqualTo(1);
    }

    @Test
    public void given_two_space_requirements_when_combine_then_expect_sums() {

        SpaceRequirement requirement1 = SpaceRequirement.NONE;
        requirement1 = requirement1.add(Type.INTEGER);
        requirement1 = requirement1.add(Type.INTEGER);
        requirement1 = requirement1.add(Type.INTEGER);
        requirement1 = requirement1.add(Type.BOOLEAN);
        requirement1 = requirement1.add(Type.SQUARE);
        assertThat(requirement1.getInteger()).isEqualTo(3);
        assertThat(requirement1.getBool()).isEqualTo(1);
        assertThat(requirement1.getSquare()).isEqualTo(1);

        SpaceRequirement requirement2 = SpaceRequirement.NONE;
        requirement2 = requirement2.add(Type.INTEGER);
        requirement2 = requirement2.add(Type.INTEGER);
        requirement2 = requirement2.add(Type.BOOLEAN);
        requirement2 = requirement2.add(Type.BOOLEAN);
        requirement2 = requirement2.add(Type.BOOLEAN);
        requirement2 = requirement2.add(Type.SQUARE);
        assertThat(requirement2.getInteger()).isEqualTo(2);
        assertThat(requirement2.getBool()).isEqualTo(3);
        assertThat(requirement2.getSquare()).isEqualTo(1);

        final SpaceRequirement combine = SpaceRequirement.combine(requirement1, requirement2);
        assertThat(combine.getInteger()).isEqualTo(5);
        assertThat(combine.getBool()).isEqualTo(4);
        assertThat(combine.getSquare()).isEqualTo(2);
    }

    @Test
    public void given_empty_space_requirement_when_get_space_reservation_then_expect_empty_segment() {
        final SpaceRequirement requirement = SpaceRequirement.NONE;
        assertThat(requirement).isNotNull();
        assertThat(requirement.size()).isEqualTo(0);

        final Segment segment = requirement.getSpaceReservation();
        assertThat(segment).isNotNull();
        assertThat(segment.isValid()).isTrue();
        assertThat(segment.size()).isEqualTo(0);
    }

    @Test
    public void given_simple_space_requirement_when_get_space_reservation_then_expect_simple_segment() {
        SpaceRequirement requirement = SpaceRequirement.NONE;

        requirement = requirement.add(Type.INTEGER);
        requirement = requirement.add(Type.BOOLEAN);
        requirement = requirement.add(Type.SQUARE);

        assertThat(requirement).isNotNull();
        assertThat(requirement.size()).isEqualTo(3);

        assertThat(requirement.getInteger()).isEqualTo(1);
        assertThat(requirement.getBool()).isEqualTo(1);
        assertThat(requirement.getSquare()).isEqualTo(1);

        final Segment segment = requirement.getSpaceReservation();
        assertThat(segment).isNotNull();
        assertThat(segment.isValid()).isTrue();
        assertThat(segment.size()).isEqualTo(3);

        final Definition intDef = segment.getDefinitions().get(0);
        assertThat(intDef).isInstanceOf(DefinitionVariable.class);
        assertThat(intDef.getIdentifier().toString()).isEqualTo("integer1");
        assertThat(((DefinitionVariable) intDef).getTypeName())
              .isEqualTo(be.unamur.info.b314.compiler.nbc.keywords.Type.SuperType.INT.getType().getIdentifier());

        final Definition boolDef = segment.getDefinitions().get(1);
        assertThat(boolDef).isInstanceOf(DefinitionVariable.class);
        assertThat(boolDef.getIdentifier().toString()).isEqualTo("boolean1");
        assertThat(((DefinitionVariable) boolDef).getTypeName())
              .isEqualTo(be.unamur.info.b314.compiler.nbc.keywords.Type.SuperType.BOOL.getType().getIdentifier());

        final Definition squareDef = segment.getDefinitions().get(2);
        assertThat(squareDef).isInstanceOf(DefinitionVariable.class);
        assertThat(squareDef.getIdentifier().toString()).isEqualTo("square1");
        assertThat(((DefinitionVariable) squareDef).getTypeName())
              .isEqualTo(be.unamur.info.b314.compiler.nbc.keywords.Type.SuperType.SQUARE.getType().getIdentifier());
    }

    @Test
    public void given_complex_space_requirement_when_get_space_reservation_then_expect_simple_segment() {
        SpaceRequirement requirement = SpaceRequirement.NONE;

        requirement = requirement.add(Type.INTEGER);
        requirement = requirement.add(Type.INTEGER);
        requirement = requirement.add(Type.INTEGER);
        requirement = requirement.add(Type.SQUARE);

        assertThat(requirement).isNotNull();
        assertThat(requirement.size()).isEqualTo(4);

        assertThat(requirement.getInteger()).isEqualTo(3);
        assertThat(requirement.getBool()).isEqualTo(0);
        assertThat(requirement.getSquare()).isEqualTo(1);

        final Segment segment = requirement.getSpaceReservation();
        assertThat(segment).isNotNull();
        assertThat(segment.isValid()).isTrue();
        assertThat(segment.size()).isEqualTo(4);

        final Definition intDef1 = segment.getDefinitions().get(0);
        assertThat(intDef1).isInstanceOf(DefinitionVariable.class);
        assertThat(intDef1.getIdentifier().toString()).isEqualTo("integer1");
        assertThat(((DefinitionVariable) intDef1).getTypeName())
              .isEqualTo(be.unamur.info.b314.compiler.nbc.keywords.Type.SuperType.INT.getType().getIdentifier());

        final Definition intDef2 = segment.getDefinitions().get(1);
        assertThat(intDef2).isInstanceOf(DefinitionVariable.class);
        assertThat(intDef2.getIdentifier().toString()).isEqualTo("integer2");
        assertThat(((DefinitionVariable) intDef2).getTypeName())
              .isEqualTo(be.unamur.info.b314.compiler.nbc.keywords.Type.SuperType.INT.getType().getIdentifier());

        final Definition intDef3 = segment.getDefinitions().get(2);
        assertThat(intDef3).isInstanceOf(DefinitionVariable.class);
        assertThat(intDef3.getIdentifier().toString()).isEqualTo("integer3");
        assertThat(((DefinitionVariable) intDef3).getTypeName())
              .isEqualTo(be.unamur.info.b314.compiler.nbc.keywords.Type.SuperType.INT.getType().getIdentifier());

        final Definition squareDef = segment.getDefinitions().get(3);
        assertThat(squareDef).isInstanceOf(DefinitionVariable.class);
        assertThat(squareDef.getIdentifier().toString()).isEqualTo("square1");
        assertThat(((DefinitionVariable) squareDef).getTypeName())
              .isEqualTo(be.unamur.info.b314.compiler.nbc.keywords.Type.SuperType.SQUARE.getType().getIdentifier());
    }
}