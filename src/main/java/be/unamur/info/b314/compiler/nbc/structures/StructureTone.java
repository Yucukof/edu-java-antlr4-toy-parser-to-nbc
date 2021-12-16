package be.unamur.info.b314.compiler.nbc.structures;

import be.unamur.info.b314.compiler.nbc.Statement;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionTone;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionWait;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;

/**
 * @author Anthony DI STASIO
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class StructureTone extends StructureNext {

    private final InstructionTone playTone;
    private final InstructionWait wait;

    @Override
    public String toString() {
        return playTone.toString() + "\n" + wait.toString();
    }

    @Override
    public boolean isValid() {
        return playTone != null
                && playTone.isValid()
                && wait != null
                && wait.isValid();
    }

    @Override
    public List<Statement> getStatements() {
        return Arrays.asList(playTone, wait);
    }
}