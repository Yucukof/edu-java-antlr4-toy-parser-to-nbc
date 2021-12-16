package be.unamur.info.b314.compiler.nbc.instructions;

import be.unamur.info.b314.compiler.nbc.keywords.Command;
import be.unamur.info.b314.compiler.nbc.keywords.Keyword;
import be.unamur.info.b314.compiler.nbc.keywords.MotorConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Anthony DI STASIO
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class InstructionOff extends InstructionAction {
    private final Keyword keyword = Command.OFF;
    private final MotorConstant motorConstant;

    @Override
    public String toString() {
        return keyword.getToken() + "(" + motorConstant.getToken() + ")";
    }

    @Override
    public boolean isValid() {
        return motorConstant != null ;
    }
}