package be.unamur.info.b314.compiler.mappers.actions;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionTone;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionWait;
import be.unamur.info.b314.compiler.nbc.keywords.Tone;
import be.unamur.info.b314.compiler.nbc.structures.StructureTone;
import be.unamur.info.b314.compiler.pils.actions.ActionShoot;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author Anthony DI STASIO
 */
@Slf4j
public class ActionShootMapper implements Function<ActionShoot, StructureTone> {

    final Context context;

    public ActionShootMapper(final Context context) {
        this.context = context;
    }

    @Override
    public StructureTone apply(final ActionShoot actionShoot) {
        log.trace("Processing action shoot");

        DirectionToToneMapper directionToToneMapper = context.mappers.getDirectionToToneMapper();
        Tone tone = directionToToneMapper.apply(actionShoot.getDirection());
        InstructionTone instructionTone = InstructionTone.builder()
                .tone(tone)
                .build();

        return StructureTone.builder()
                .playTone(instructionTone)
                .wait(InstructionWait.get())
                .build();
    }
}