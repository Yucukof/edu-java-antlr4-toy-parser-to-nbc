package be.unamur.info.b314.compiler.nbc.structures;

import be.unamur.info.b314.compiler.nbc.Statement;
import be.unamur.info.b314.compiler.nbc.Structure;
import be.unamur.info.b314.compiler.nbc.instructions.Instruction;
import be.unamur.info.b314.compiler.nbc.program.Label;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hadrien BAILLY
 */
@Data
@Builder
public class StructureIf implements Structure {

    private final Structure guard;
    @Builder.Default
    private final Structure branchTrue = Structure.EMPTY();
    private final Instruction jumpToEnd;
    private final Label elseLabel;
    @Builder.Default
    private final Structure branchFalse = Structure.EMPTY();
    private final Label endLabel;

    @Override
    public String toString() {
        return guard.toString()
              + "\n" + branchTrue.toString()
              + "\n" + jumpToEnd.toString()
              + "\n" + elseLabel.toString()
              + "\n" + branchFalse.toString()
              + "\n" + endLabel.toString();
    }

    @Override
    public boolean isValid() {
        return guard != null && guard.isValid()
              && branchTrue != null && branchTrue.isValid()
              && branchFalse != null && branchFalse.isValid()
              && jumpToEnd != null && jumpToEnd.isValid()
              && (elseLabel == null || elseLabel.isValid())
              && endLabel != null && endLabel.isValid();
    }

    @Override
    public List<Statement> getStatements() {
        final List<Statement> statements = new ArrayList<>();

        statements.addAll(guard.getStatements());
        statements.addAll(branchTrue.getStatements());
        statements.add(jumpToEnd);
        statements.add(elseLabel);
        statements.addAll(branchFalse.getStatements());
        statements.add(endLabel);

        return statements;
    }
}
