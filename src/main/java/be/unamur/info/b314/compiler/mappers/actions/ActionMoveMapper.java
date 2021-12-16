package be.unamur.info.b314.compiler.mappers.actions;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionMotor;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionOff;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionWait;
import be.unamur.info.b314.compiler.nbc.keywords.Command;
import be.unamur.info.b314.compiler.nbc.keywords.MotorConstant;
import be.unamur.info.b314.compiler.nbc.structures.StructureMove;
import be.unamur.info.b314.compiler.pils.actions.ActionMove;
import be.unamur.info.b314.compiler.pils.keywords.Direction;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author Anthony DI STASIO
 */
@Slf4j
public class ActionMoveMapper implements Function<ActionMove, StructureMove> {

    final Context context;

    public ActionMoveMapper(final Context context) {
        this.context = context;
    }

    @Override
    public StructureMove apply(final ActionMove actionMove) {
        log.trace("Processing action move");

        // Set default values in order to avoid NPEs
        Command command = Command.OFF;
        MotorConstant motorConstant = MotorConstant.OUT_BC;

        if (actionMove.getDirection().equals(Direction.NORTH)
                || actionMove.getDirection().equals(Direction.WEST)
                || actionMove.getDirection().equals(Direction.EAST)) {
            command = Command.ON_FWD;
        }

        if (actionMove.getDirection().equals(Direction.SOUTH)) {
            command = Command.ON_REV;
        }

        if (actionMove.getDirection().equals(Direction.WEST)) {
            motorConstant = MotorConstant.OUT_C;
        }

        if (actionMove.getDirection().equals(Direction.EAST)) {
            motorConstant = MotorConstant.OUT_B;
        }

        InstructionMotor instructionMotor = InstructionMotor.builder()
                .keyword(command)
                .motorConstant(motorConstant)
                .build();
        InstructionOff instructionOff = InstructionOff.builder()
                .motorConstant(motorConstant)
                .build();

        return StructureMove.builder()
                .motor(instructionMotor)
                .wait(InstructionWait.get())
                .off(instructionOff)
                .build();
    }
}