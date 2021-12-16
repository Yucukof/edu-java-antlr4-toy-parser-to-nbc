package be.unamur.info.b314.compiler.pils.clauses;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Declaration;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.statements.Statement;
import be.unamur.info.b314.compiler.visitors.ExpressionVisitor;
import be.unamur.info.b314.compiler.visitors.StatementVisitor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder()
@EqualsAndHashCode(callSuper = true)
public class ClauseWhen extends Clause {

    /**
     * L'expression gardant l'exécution du corps de la clause.
     */
    private final Expression guard;

    public static ClauseWhen from(final GrammarParser.ClauseWhenContext ctx, final Context Tables) {
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(Tables);
        final Expression guard = expressionVisitor.visitExpression(ctx.expression());

        final StatementVisitor statementVisitor = new StatementVisitor(Tables);
        final List<Statement> statements = ctx.instruction().stream()
              .map(statementVisitor::visitInstruction)
              .collect(Collectors.toList());

        return ClauseWhen.builder()
              .guard(guard)
              .statements(statements)
              .build();
    }

    @Override
    public String toString() {
        return "\twhen " + guard + "\n"
              + (getDeclarations().size() > 0
              ? "\t\tdeclare local\n\t\t" + getDeclarations().stream().map(Declaration::getDeclaration).collect(Collectors.joining("\n\t\t")) + "\n"
              : "")
              + "\t\tdo\n\t\t\t"
              + (getStatements().size() > 0
              ? getStatements().stream().map(Objects::toString).collect(Collectors.joining("\n\t\t\t")) + "\n"
              : "")
              + "\t\tdone";
    }

    @Override
    public boolean isValid() {
        return super.isValid() && guard != null && guard.isValid() && guard.isBoolean();
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        return SpaceRequirement.merge(super.getSpaceRequirement(), guard.getSpaceRequirement());
    }

    public boolean isTriggered() {
        return guard.getValue().getBoolValue();
    }
}
