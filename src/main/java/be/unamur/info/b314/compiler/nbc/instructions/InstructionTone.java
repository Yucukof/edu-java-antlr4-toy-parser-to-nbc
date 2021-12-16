package be.unamur.info.b314.compiler.nbc.instructions;

import be.unamur.info.b314.compiler.nbc.keywords.Command;
import be.unamur.info.b314.compiler.nbc.keywords.Keyword;
import be.unamur.info.b314.compiler.nbc.keywords.Tone;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Anthony DI STASIO
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class InstructionTone extends InstructionAction {
    private final Keyword keyword = Command.PLAY_TONE;
    private final Tone tone;
    private final int frequency = 500;

    @Override
    public String toString() {
        return Command.PLAY_TONE.getToken() + "(" + tone.getToken() + ", " + frequency + ")";
    }

    @Override
    public boolean isValid() {
        return tone != null;
    }
}