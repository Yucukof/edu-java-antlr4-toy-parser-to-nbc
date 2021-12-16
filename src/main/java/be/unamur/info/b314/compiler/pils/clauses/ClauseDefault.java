package be.unamur.info.b314.compiler.pils.clauses;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Declaration;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.statements.Statement;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.visitors.StatementVisitor;
import be.unamur.info.b314.compiler.visitors.VariableVisitor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ClauseDefault extends Clause {

    public static ClauseDefault from(final GrammarParser.ClauseDefaultContext ctx, final Context symbols) {

        // STEP 1 - Récupérer la liste des déclarations locales
        final VariableVisitor variableVisitor = new VariableVisitor(symbols);
        final List<Variable> declarations = ctx.varDecl().stream().map(variableVisitor::visitVarDecl).collect(Collectors.toList());

        //STEP 2 - Récupérer la liste des instructions
        final StatementVisitor statementVisitor = new StatementVisitor(symbols);
        final List<Statement> statements = ctx.instruction().stream().map(statementVisitor::visitInstruction).collect(Collectors.toList());

        // STEP 3 - Construire et retourner la clause
        return ClauseDefault.builder()
              .declarations(declarations)
              .statements(statements)
              .build();
    }

    @Override
    public String toString() {
        return "by default\n"
              + super.toString();
    }
}
