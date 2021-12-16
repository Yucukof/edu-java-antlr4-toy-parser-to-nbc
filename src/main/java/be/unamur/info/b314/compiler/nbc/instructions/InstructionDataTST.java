package be.unamur.info.b314.compiler.nbc.instructions;

import be.unamur.info.b314.compiler.nbc.symbols.Comparator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class InstructionDataTST extends InstructionDataUnary {

    private final Comparator comparator;

    @Override
    public String toString() {
        return getKeyword().getToken() + " " + comparator.getToken() + ", " + getDestination();
    }

    @Override
    public boolean isValid() {
        return super.isValid() && comparator != null;
    }
}
