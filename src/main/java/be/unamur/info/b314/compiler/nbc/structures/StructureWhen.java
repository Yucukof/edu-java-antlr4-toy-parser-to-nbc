package be.unamur.info.b314.compiler.nbc.structures;

import be.unamur.info.b314.compiler.nbc.Statement;
import be.unamur.info.b314.compiler.nbc.Structure;
import be.unamur.info.b314.compiler.nbc.declarations.Segment;
import be.unamur.info.b314.compiler.nbc.program.Label;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hadrien BAILLY
 */
@Data
@Builder(toBuilder = true)
public class    StructureWhen implements Structure {

    // TODO: 26/04/2021 [ADS]: add jumpToEndIfFalse attribute 
    private final Label start;
    private final Structure guard;
    private final Segment segment;
    private final Structure body;
    private final Label end;

    @Override
    public String toString() {
        return start.toString()
              + "\n" + guard.toString()
              + (segment != null && segment.size() > 0 ? "\n" + segment : "")
              + "\n" + body.toString()
              + "\n" + end.toString();
    }

    @Override
    public boolean isValid() {
        return start != null && start.isValid()
              && guard != null && guard.isValid()
              && (segment == null || segment.isValid())
              && body != null && body.isValid()
              && end != null && end.isValid();
    }

    @Override
    public List<Statement> getStatements() {
        final List<Statement> statements = new ArrayList<>();

        statements.add(start);
        statements.addAll(guard.getStatements());
        statements.add(segment);
        statements.addAll(body.getStatements());
        statements.add(end);

        return statements;
    }
}
