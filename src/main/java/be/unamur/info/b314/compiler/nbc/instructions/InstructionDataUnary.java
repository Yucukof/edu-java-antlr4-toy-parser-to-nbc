package be.unamur.info.b314.compiler.nbc.instructions;

import be.unamur.info.b314.compiler.nbc.Argument;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class InstructionDataUnary extends InstructionData {

    private final Argument argument;

    @Override
    public String toString() {
        return getKeyword().getToken() + " " + getDestination() + ", " + argument;
    }
}
