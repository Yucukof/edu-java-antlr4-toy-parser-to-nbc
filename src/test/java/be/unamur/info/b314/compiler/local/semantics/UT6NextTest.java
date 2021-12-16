package be.unamur.info.b314.compiler.local.semantics;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.pils.actions.ActionIdle;
import be.unamur.info.b314.compiler.pils.actions.ActionMove;
import be.unamur.info.b314.compiler.pils.actions.ActionShoot;
import be.unamur.info.b314.compiler.pils.actions.ActionUse;
import be.unamur.info.b314.compiler.pils.exceptions.ActionNotFoundException;
import be.unamur.info.b314.compiler.pils.exceptions.DirectionNotFoundException;
import be.unamur.info.b314.compiler.pils.exceptions.ItemNotFoundException;
import be.unamur.info.b314.compiler.pils.keywords.Act;
import be.unamur.info.b314.compiler.pils.keywords.Direction;
import be.unamur.info.b314.compiler.pils.keywords.Item;
import be.unamur.info.b314.compiler.pils.statements.StatementNext;
import be.unamur.info.b314.compiler.listeners.Context;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Anthony DI STASIO
 */
public class UT6NextTest extends UnitTestTemplate {
    @Test
    public void from_statement_with_move_and_north() {
        String nextStatementAsString = "next move north";
        final GrammarParser parser = getParser(nextStatementAsString);

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();
        final StatementNext statementNext = StatementNext.from(ctx, new Context());

        ActionMove expectedActionMove = ActionMove.builder()
              .act(Act.MOVE)
              .direction(Direction.NORTH)
              .build();

        assertThat(statementNext.getAction()).isEqualTo(expectedActionMove);
        assertThat(statementNext.toString()).isEqualTo(nextStatementAsString);
    }

    @Test
    public void from_statement_with_move_and_south() {
        String nextStatementAsString = "next move south";
        final GrammarParser parser = getParser(nextStatementAsString);

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();
        final StatementNext statementNext = StatementNext.from(ctx, new Context());

        ActionMove expectedActionMove = ActionMove.builder()
              .act(Act.MOVE)
              .direction(Direction.SOUTH)
              .build();

        assertThat(statementNext.getAction()).isEqualTo(expectedActionMove);
        assertThat(statementNext.toString()).isEqualTo(nextStatementAsString);
    }

    @Test
    public void from_statement_with_move_and_west() {
        String nextStatementAsString = "next move west";
        final GrammarParser parser = getParser(nextStatementAsString);

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();
        final StatementNext statementNext = StatementNext.from(ctx, new Context());

        ActionMove expectedActionMove = ActionMove.builder()
              .act(Act.MOVE)
              .direction(Direction.WEST)
              .build();

        assertThat(statementNext.getAction()).isEqualTo(expectedActionMove);
        assertThat(statementNext.toString()).isEqualTo(nextStatementAsString);
    }

    @Test
    public void from_statement_with_move_and_east() {
        String nextStatementAsString = "next move east";
        final GrammarParser parser = getParser(nextStatementAsString);

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();
        final StatementNext statementNext = StatementNext.from(ctx, new Context());

        ActionMove expectedActionMove = ActionMove.builder()
              .act(Act.MOVE)
              .direction(Direction.EAST)
              .build();

        assertThat(statementNext.getAction()).isEqualTo(expectedActionMove);
        assertThat(statementNext.toString()).isEqualTo(nextStatementAsString);
    }

    @Test
    public void from_statement_with_move_and_missing_direction_then_exception() {
        final GrammarParser parser = getParser("next move ");

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();

        assertThatThrownBy(() -> StatementNext.from(ctx, new Context()))
              .isInstanceOf(DirectionNotFoundException.class);
    }

    @Test
    public void from_statement_with_shoot_and_north() {
        String nextStatementAsString = "next shoot north";
        final GrammarParser parser = getParser(nextStatementAsString);

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();
        final StatementNext statementNext = StatementNext.from(ctx, new Context());

        ActionShoot expectedActionShoot = ActionShoot.builder()
              .act(Act.SHOOT)
              .direction(Direction.NORTH)
              .build();

        assertThat(statementNext.getAction()).isEqualTo(expectedActionShoot);
        assertThat(statementNext.toString()).isEqualTo(nextStatementAsString);
    }

    @Test
    public void from_statement_with_shoot_and_south() {
        String nextStatementAsString = "next shoot south";
        final GrammarParser parser = getParser(nextStatementAsString);

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();
        final StatementNext statementNext = StatementNext.from(ctx, new Context());

        ActionShoot expectedActionShoot = ActionShoot.builder()
              .act(Act.SHOOT)
              .direction(Direction.SOUTH)
              .build();

        assertThat(statementNext.getAction()).isEqualTo(expectedActionShoot);
        assertThat(statementNext.toString()).isEqualTo(nextStatementAsString);
    }

    @Test
    public void from_statement_with_shoot_and_west() {
        String nextStatementAsString = "next shoot west";
        final GrammarParser parser = getParser(nextStatementAsString);

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();
        final StatementNext statementNext = StatementNext.from(ctx, new Context());

        ActionShoot expectedActionShoot = ActionShoot.builder()
              .act(Act.SHOOT)
              .direction(Direction.WEST)
              .build();

        assertThat(statementNext.getAction()).isEqualTo(expectedActionShoot);
        assertThat(statementNext.toString()).isEqualTo(nextStatementAsString);
    }

    @Test
    public void from_statement_with_shoot_and_east() {
        String nextStatementAsString = "next shoot east";
        final GrammarParser parser = getParser(nextStatementAsString);

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();
        final StatementNext statementNext = StatementNext.from(ctx, new Context());

        ActionShoot expectedActionShoot = ActionShoot.builder()
              .act(Act.SHOOT)
              .direction(Direction.EAST)
              .build();

        assertThat(statementNext.getAction()).isEqualTo(expectedActionShoot);
        assertThat(statementNext.toString()).isEqualTo(nextStatementAsString);
    }

    @Test
    public void from_statement_with_shoot_and_missing_direction_then_exception() {
        final GrammarParser parser = getParser("next shoot ");

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();

        assertThatThrownBy(() -> StatementNext.from(ctx, new Context()))
              .isInstanceOf(DirectionNotFoundException.class);
    }

    @Test
    public void from_statement_with_use_and_soda() {
        String nextStatementAsString = "next use soda";
        final GrammarParser parser = getParser(nextStatementAsString);

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();
        final StatementNext statementNext = StatementNext.from(ctx, new Context());

        ActionUse expectedActionUse = ActionUse.builder()
              .act(Act.USE)
              .item(Item.SODA)
              .build();

        assertThat(statementNext.getAction()).isEqualTo(expectedActionUse);
        assertThat(statementNext.toString()).isEqualTo(nextStatementAsString);
    }

    @Test
    public void from_statement_with_use_and_fruits() {
        String nextStatementAsString = "next use fruits";
        final GrammarParser parser = getParser(nextStatementAsString);

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();
        final StatementNext statementNext = StatementNext.from(ctx, new Context());

        ActionUse expectedActionUse = ActionUse.builder()
              .act(Act.USE)
              .item(Item.FRUITS)
              .build();

        assertThat(statementNext.getAction()).isEqualTo(expectedActionUse);
        assertThat(statementNext.toString()).isEqualTo(nextStatementAsString);
    }

    @Test
    public void from_statement_with_use_and_map() {
        String nextStatementAsString = "next use map";
        final GrammarParser parser = getParser(nextStatementAsString);

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();
        final StatementNext statementNext = StatementNext.from(ctx, new Context());

        ActionUse expectedActionUse = ActionUse.builder()
              .act(Act.USE)
              .item(Item.MAP)
              .build();

        assertThat(statementNext.getAction()).isEqualTo(expectedActionUse);
        assertThat(statementNext.toString()).isEqualTo(nextStatementAsString);
    }

    @Test
    public void from_statement_with_use_and_radio() {
        String nextStatementAsString = "next use radio";
        final GrammarParser parser = getParser(nextStatementAsString);

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();
        final StatementNext statementNext = StatementNext.from(ctx, new Context());

        ActionUse expectedActionUse = ActionUse.builder()
              .act(Act.USE)
              .item(Item.RADIO)
              .build();

        assertThat(statementNext.getAction()).isEqualTo(expectedActionUse);
        assertThat(statementNext.toString()).isEqualTo(nextStatementAsString);
    }

    @Test
    public void from_statement_with_use_and_missing_item_then_exception() {
        final GrammarParser parser = getParser("next use ");

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();

        assertThatThrownBy(() -> StatementNext.from(ctx, new Context()))
              .isInstanceOf(ItemNotFoundException.class);
    }

    @Test
    public void from_statement_with_do_nothing() {
        String nextStatementAsString = "next do nothing";
        final GrammarParser parser = getParser(nextStatementAsString);

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();
        final StatementNext statementNext = StatementNext.from(ctx, new Context());

        ActionIdle expectedActionIdle = ActionIdle.builder()
              .build();

        assertThat(statementNext.getAction()).isEqualTo(expectedActionIdle);
        assertThat(statementNext.toString()).isEqualTo(nextStatementAsString);
    }


    @Test
    public void from_statement_with_missing_action() {
        final GrammarParser parser = getParser("next ");

        final GrammarParser.NextInstrContext ctx = (GrammarParser.NextInstrContext) parser.instruction();

        assertThatThrownBy(() -> StatementNext.from(ctx, new Context()))
              .isInstanceOf(ActionNotFoundException.class);
    }
}