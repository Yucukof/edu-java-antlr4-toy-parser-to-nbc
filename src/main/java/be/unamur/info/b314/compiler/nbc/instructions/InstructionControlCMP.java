package be.unamur.info.b314.compiler.nbc.instructions;

import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.keywords.ControlFlow;
import be.unamur.info.b314.compiler.nbc.keywords.Keyword;
import be.unamur.info.b314.compiler.nbc.symbols.Comparator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class InstructionControlCMP extends InstructionControl {

    private final Keyword keyword = ControlFlow.BRCMP;
    private final Comparator comparator;
    private final Argument argument1;
    private final Argument argument2;

    @Override
    public String toString() {
        return keyword.getToken() + " " + comparator.getToken() + ", " + getDestination().getName() + ", " + argument1.getToken() + ", " + argument2.getToken();
    }

    @Override
    public boolean isValid() {
        return comparator != null
              && getDestination() != null && getDestination().isValid()
              && argument1 != null && argument1.isValid()
              && argument2 != null && argument2.isValid();
    }
}
