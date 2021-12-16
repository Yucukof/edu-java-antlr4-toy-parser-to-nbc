package be.unamur.info.b314.compiler.nbc.instructions;

import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.symbols.Comparator;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.stream.Collectors;

import static be.unamur.info.b314.compiler.nbc.keywords.ControlFlow.BRCMP;
import static be.unamur.info.b314.compiler.nbc.keywords.ControlFlow.BRTST;

/**
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class InstructionDataCMP extends InstructionDataBinary {

    private final Comparator comparator;

    @Override
    public String toString() {
        return getKeyword().getToken() + " " + comparator.getToken() + ", " + getDestination() + ", " + getArgument1() + ", " + getArgument2();
    }

    @Override
    public boolean isValid() {
        return super.isValid() && comparator != null;
    }
}
