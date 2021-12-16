package be.unamur.info.b314.compiler.nbc.instructions;

import be.unamur.info.b314.compiler.nbc.keywords.Keyword;
import be.unamur.info.b314.compiler.nbc.keywords.Wait;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Anthony DI STASIO
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class InstructionWait extends Instruction {
    private final Keyword keyword = Wait.WAIT;
    private final int time = 500;

    @Override
    public String toString() {
        return Wait.WAIT.getToken() + "(" + time + ")";
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public static InstructionWait get() {
        return InstructionWait.builder().build();
    }
}