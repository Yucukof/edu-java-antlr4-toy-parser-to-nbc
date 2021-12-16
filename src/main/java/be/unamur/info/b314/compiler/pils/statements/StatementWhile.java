package be.unamur.info.b314.compiler.pils.statements;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.visitors.StatementVisitor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
@Data
@Builder
public class StatementWhile implements Statement {

    /**
     * La condition de garde de l'instruction WHILE
     */
    private final Expression guard;
    /**
     * Corps de l'instruction WHILE
     */
    @Builder.Default
    private final List<Statement> statements = new ArrayList<>();
    /**
     * Le niveau d'indentation.
     */
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private final int indentation = 0;

    public static StatementWhile from(final GrammarParser.WhileInstrContext ctx, final Context symbols) {
        return (StatementWhile) new StatementVisitor(symbols).visitWhileInstr(ctx);
    }

    @Override
    public String toString() {
        final String tab0 = StringUtils.repeat("\t", indentation) + "\t";
        final String tab1 = tab0 + "\t";

        return "while " + guard + " do"
              + "\n" + tab1 + getStatements().stream().map(Object::toString).collect(Collectors.joining("\n" + tab1))
              + "\n" + tab0 + "done";
    }

    @Override
    public boolean isValid() {
        return guard != null && guard.isValid() && guard.isBoolean()
              && statements.stream().allMatch(Statement::isValid);
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        final SpaceRequirement guardRequirement = guard.getSpaceRequirement();
        final SpaceRequirement bodyRequirement = statements.stream()
              .map(Statement::getSpaceRequirement)
              .reduce(SpaceRequirement::merge)
              .orElse(SpaceRequirement.NONE);
        return SpaceRequirement.combine(guardRequirement, bodyRequirement);
    }

    @Override
    public Optional<StatementNext> run() {
        log.trace("Running [{}]", this);
        while (guard.getValue().getBoolValue()) {
            log.trace("Processing body");
            final Optional<StatementNext> stmt = statements.stream()
                  .map(Statement::run)
                  .filter(Optional::isPresent)
                  .findFirst()
                  .orElse(Optional.empty());

            if (stmt.isPresent()) {
                return stmt;
            }
        }
        return Optional.empty();
    }
}
