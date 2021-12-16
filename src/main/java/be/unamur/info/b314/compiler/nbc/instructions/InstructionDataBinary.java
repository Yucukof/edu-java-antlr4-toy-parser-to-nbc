package be.unamur.info.b314.compiler.nbc.instructions;

import be.unamur.info.b314.compiler.nbc.Argument;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class InstructionDataBinary extends InstructionData {

    private final Argument argument1;
    private final Argument argument2;

    @Override
    public String toString() {
        return getKeyword().getToken() + " " + getDestination() + ", " + getArgument1() + ", " + getArgument2();
    }

    @Override
    public boolean isValid() {
        return super.isValid()
              && argument1 != null && argument1.isValid()
              && argument2 != null && argument2.isValid();
    }
}
