package be.unamur.info.b314.compiler.mappers.statements;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.structures.StructureDisplay;
import be.unamur.info.b314.compiler.nbc.structures.StructureMove;
import be.unamur.info.b314.compiler.nbc.structures.StructureNext;
import be.unamur.info.b314.compiler.nbc.structures.StructureTone;
import be.unamur.info.b314.compiler.pils.actions.ActionIdle;
import be.unamur.info.b314.compiler.pils.actions.ActionMove;
import be.unamur.info.b314.compiler.pils.actions.ActionShoot;
import be.unamur.info.b314.compiler.pils.actions.ActionUse;
import be.unamur.info.b314.compiler.pils.keywords.Act;
import be.unamur.info.b314.compiler.pils.keywords.Direction;
import be.unamur.info.b314.compiler.pils.keywords.Item;
import be.unamur.info.b314.compiler.pils.statements.StatementNext;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Anthony DI STASIO
 */
public class StatementNextMapperTest {

    private Context context;
    private StatementNextMapper mapper;

    @Before
    public void before() {
        context = new Context();
        mapper = context.mappers.getStatementNextMapper();
    }

    @Test
    public void given_shoot_north_action_should_convert_structure_tone() {
        final ActionShoot actionShoot = ActionShoot.builder()
                .direction(Direction.NORTH)
                .act(Act.SHOOT)
                .build();

        final StatementNext statementNext = StatementNext.builder()
                .action(actionShoot)
                .build();

        final StructureTone structureTone = (StructureTone) mapper.apply(statementNext);

        assertThat(structureTone.toString()).isEqualTo("PlayTone(TONE_A3, 500)" + "\n" + "wait(500)");
    }

    @Test
    public void given_shoot_south_action_should_convert_to_structure_tone() {
        final ActionShoot actionShoot = ActionShoot.builder()
                .direction(Direction.SOUTH)
                .act(Act.SHOOT)
                .build();

        final StatementNext statementNext = StatementNext.builder()
                .action(actionShoot)
                .build();

        final StructureTone structureTone = (StructureTone) mapper.apply(statementNext);

        assertThat(structureTone.toString()).isEqualTo("PlayTone(TONE_B3, 500)" + "\n" + "wait(500)");
    }

    @Test
    public void given_shoot_west_action_should_convert_to_structure_tone() {
        final ActionShoot actionShoot = ActionShoot.builder()
                .direction(Direction.WEST)
                .act(Act.SHOOT)
                .build();

        final StatementNext statementNext = StatementNext.builder()
                .action(actionShoot)
                .build();

        final StructureTone structureTone = (StructureTone) mapper.apply(statementNext);

        assertThat(structureTone.toString()).isEqualTo("PlayTone(TONE_D3, 500)" + "\n" + "wait(500)");
    }

    @Test
    public void given_shoot_east_action_should_convert_to_structure_tone() {
        final ActionShoot actionShoot = ActionShoot.builder()
                .direction(Direction.EAST)
                .act(Act.SHOOT)
                .build();

        final StatementNext statementNext = StatementNext.builder()
                .action(actionShoot)
                .build();

        final StructureTone structureTone = (StructureTone) mapper.apply(statementNext);

        assertThat(structureTone.toString()).isEqualTo("PlayTone(TONE_C3, 500)" + "\n" + "wait(500)");
    }

    @Test
    public void given_next_use_radar_should_convert_to_structure_display() {
        final ActionUse actionUse = ActionUse.builder()
                .item(Item.RADAR)
                .act(Act.USE)
                .build();

        final StatementNext statementNext = StatementNext.builder()
                .action(actionUse)
                .build();

        final StructureDisplay structureDisplay = (StructureDisplay) mapper.apply(statementNext);

        assertThat(structureDisplay.toString()).isEqualTo("TextOut(0, 56, \"radar\")");
    }

    @Test
    public void given_next_move_north_should_convert_to_structure_move() {
        final ActionMove actionMove = ActionMove.builder()
                .direction(Direction.NORTH)
                .act(Act.MOVE)
                .build();

        final StatementNext statementNext = StatementNext.builder()
                .action(actionMove)
                .build();

        final StructureMove structureMove = (StructureMove) mapper.apply(statementNext);

        assertThat(structureMove.toString()).isEqualTo(
                "OnFwd(OUT_BC, 100)" + "\n" +
                "wait(500)" + "\n" +
                "Off(OUT_BC)"
        );
    }

    @Test
    public void given_next_move_south_should_convert_to_structure_move() {
        final ActionMove actionMove = ActionMove.builder()
                .direction(Direction.SOUTH)
                .act(Act.MOVE)
                .build();

        final StatementNext statementNext = StatementNext.builder()
                .action(actionMove)
                .build();

        final StructureMove structureMove = (StructureMove) mapper.apply(statementNext);

        assertThat(structureMove.toString()).isEqualTo(
                "OnRev(OUT_BC, 100)" + "\n" +
                "wait(500)" + "\n" +
                "Off(OUT_BC)"
        );
    }

    @Test
    public void given_next_move_west_should_convert_to_structure_move() {
        final ActionMove actionMove = ActionMove.builder()
                .direction(Direction.WEST)
                .act(Act.MOVE)
                .build();

        final StatementNext statementNext = StatementNext.builder()
                .action(actionMove)
                .build();

        final StructureMove structureMove = (StructureMove) mapper.apply(statementNext);

        assertThat(structureMove.toString()).isEqualTo(
                "OnFwd(OUT_C, 100)" + "\n" +
                "wait(500)" + "\n" +
                "Off(OUT_C)"
        );
    }

    @Test
    public void given_next_move_east_should_convert_to_structure_move() {
        final ActionMove actionMove = ActionMove.builder()
                .direction(Direction.EAST)
                .act(Act.MOVE)
                .build();

        final StatementNext statementNext = StatementNext.builder()
                .action(actionMove)
                .build();

        final StructureMove structureMove = (StructureMove) mapper.apply(statementNext);

        assertThat(structureMove.toString()).isEqualTo(
                "OnFwd(OUT_B, 100)" + "\n" +
                "wait(500)" + "\n" +
                "Off(OUT_B)"
        );
    }

    @Test
    public void given_idle_action_should_convert_to_structure_empty() {
        final ActionIdle actionIdle = ActionIdle.builder()
                .build();

        final StatementNext statementNext = StatementNext.builder()
                .action(actionIdle)
                .build();

        final StructureNext structureNext = mapper.apply(statementNext);

        assertThat(structureNext.toString()).isEqualTo("");
    }
}