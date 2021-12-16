package be.unamur.info.b314.compiler.nbc.instructions;

import be.unamur.info.b314.compiler.nbc.keywords.ControlFlow;
import be.unamur.info.b314.compiler.nbc.keywords.Keyword;
import be.unamur.info.b314.compiler.nbc.program.Label;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class InstructionControlJMP extends InstructionControl {

    private final Keyword keyword = ControlFlow.JMP;

    public static InstructionControlJMP to(Label label) {
        return InstructionControlJMP.builder()
                .destination(label)
                .build();
    }

    @Override
    public String toString() {
        return keyword.getToken() + " " + getDestination().getName();
    }

    @Override
    public boolean isValid() {
        return getDestination() != null && getDestination().isValid();
    }
}
