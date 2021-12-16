package be.unamur.info.b314.compiler.nbc.instructions;

import be.unamur.info.b314.compiler.nbc.keywords.Command;
import be.unamur.info.b314.compiler.nbc.keywords.Keyword;
import be.unamur.info.b314.compiler.nbc.keywords.MotorConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Anthony DI STASIO, Hadrien Bailly
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class InstructionMotor extends Instruction {

    private final MotorConstant motorConstant;
    private final int power = 100; // TODO: 08/05/2021 [ADS]: could be generic in the future

    @Override
    public String toString() {
        return getKeyword().getToken() + "(" + motorConstant.getToken() + ", " + power + ")";
    }

    @Override
    public boolean isValid() {
        final Keyword keyword = getKeyword();
        return super.isValid()
                && keyword instanceof Command
                && motorConstant != null;
    }
}