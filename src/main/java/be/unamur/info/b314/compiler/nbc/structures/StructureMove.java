package be.unamur.info.b314.compiler.nbc.structures;

import be.unamur.info.b314.compiler.nbc.Statement;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionMotor;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionOff;
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
public class StructureMove extends StructureNext {

    private final InstructionMotor motor;
    private final InstructionWait wait;
    private final InstructionOff off;

    @Override
    public String toString() {
        return motor.toString() + "\n" +
                wait.toString() + "\n" +
                off.toString();
    }

    @Override
    public boolean isValid() {
        return motor != null
                && motor.isValid()
                && wait != null
                && wait.isValid();
    }

    @Override
    public List<Statement> getStatements() {
        return Arrays.asList(motor, wait, off);
    }
}