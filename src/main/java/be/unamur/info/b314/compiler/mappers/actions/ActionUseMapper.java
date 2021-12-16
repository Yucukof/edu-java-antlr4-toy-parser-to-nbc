package be.unamur.info.b314.compiler.mappers.actions;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionTextOut;
import be.unamur.info.b314.compiler.nbc.keywords.LcdLine;
import be.unamur.info.b314.compiler.nbc.structures.StructureDisplay;
import be.unamur.info.b314.compiler.pils.actions.ActionUse;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author Anthony DI STASIO
 */
@Slf4j
public class ActionUseMapper implements Function<ActionUse, StructureDisplay> {

    final Context context;

    public ActionUseMapper(final Context context) {
        this.context = context;
    }

    @Override
    public StructureDisplay apply(final ActionUse actionUse) {
        log.trace("Processing action use");

        InstructionTextOut instructionTextOut = InstructionTextOut.builder()
                .line(LcdLine.LCD_LINE1) // TODO: 08/05/2021 [ADS]: could be generic in the future 
                .text(actionUse.getItem().getToken())
                .build();

        return StructureDisplay.builder()
                .textOut(instructionTextOut)
                .build();
    }
}