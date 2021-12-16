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
 * @author Anthony DI STASIO
 */
@Data
@Builder(toBuilder = true)
public class StructureWhile implements Structure {

    private final Label start;
    private final Structure guard;
    private final Instruction jumpToEndIfFalse;
    private final Structure body;
    private final Instruction jumpToStartIfTrue;
    private final Label end;

    @Override
    public String toString() {
        return start.toString()
              + "\n" + jumpToEndIfFalse.toString()
              + "\n" + guard.toString()
              + "\n" + body.toString()
              + "\n" + jumpToStartIfTrue.toString()
              + "\n" + end.toString();
    }

    @Override
    public boolean isValid() {
        return start != null && start.isValid()
              && jumpToEndIfFalse != null && jumpToEndIfFalse.isValid()
              && guard != null && guard.isValid()
              && body != null && body.isValid()
              && jumpToStartIfTrue != null && jumpToStartIfTrue.isValid()
              && end != null && end.isValid();
    }

    @Override
    public List<Statement> getStatements() {
        final List<Statement> statements = new ArrayList<>();

        statements.add(start);
        statements.addAll(guard.getStatements());
        statements.add(jumpToEndIfFalse);
        statements.addAll(body.getStatements());
        statements.add(jumpToStartIfTrue);
        statements.add(end);

        return statements;
    }
}
