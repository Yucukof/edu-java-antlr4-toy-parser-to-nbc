package be.unamur.info.b314.compiler.nbc.instructions;

import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.keywords.ControlFlow;
import be.unamur.info.b314.compiler.nbc.keywords.Keyword;
import be.unamur.info.b314.compiler.nbc.symbols.Comparator;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * @author Hadrien BAILLY
 */
@Getter
@SuperBuilder(toBuilder = true)
public class InstructionControlTST extends InstructionControl {

    private final Keyword keyword = ControlFlow.BRTST;
    private final Comparator comparator;
    private final Argument argument;

    @Override
    public String toString() {
        return keyword.getToken() + " " + getComparator() + ", " + getDestination().getName() + ", " + getArgument();
    }

    @Override
    public boolean isValid() {
        return comparator != null
              && getDestination() != null && getDestination().isValid()
              && argument != null && argument.isValid();
    }
}
