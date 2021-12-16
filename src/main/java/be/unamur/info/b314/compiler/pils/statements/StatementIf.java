package be.unamur.info.b314.compiler.pils.statements;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.values.Value;
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
public class StatementIf implements Statement {

    /**
     * La condition de garde de l'insruction IF.
     */
    private final Expression guard;
    /**
     * La branche à emprunter si la garde est vérifiée.
     */
    @Builder.Default
    private final List<Statement> branchTrue = new ArrayList<>();
    /**
     * La branche à emprunter si la garde est invalidée.
     */
    @Builder.Default
    private final List<Statement> branchFalse = new ArrayList<>();

    /**
     * Le niveau d'indentation.
     */
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private final int indentation = 0;

    public static StatementIf from(final GrammarParser.IfInstrContext ctx, final Context context) {
        return (StatementIf) new StatementVisitor(context).visitIfInstr(ctx);
    }

    @Override
    public String toString() {
        final String tab0 = StringUtils.repeat("\t", indentation) + "\t";
        final String tab1 = tab0 + "\t";

        return "if " + guard + " then"
              + "\n" + tab1 + getBranchTrue().stream().map(Object::toString).collect(Collectors.joining("\n" + tab1))
              + (getBranchFalse().size() != 0
              ? "\n" + tab0 + "else"
              + "\n" + tab1 + getBranchFalse().stream().map(Object::toString).collect(Collectors.joining("\n" + tab1))
              : "")
              + "\n" + tab0 + "done";

    }

    @Override
    public boolean isValid() {
        return guard != null && guard.isValid() && guard.isBoolean()
              && branchTrue.stream().allMatch(Statement::isValid)
              && branchFalse.stream().allMatch(Statement::isValid);
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        final SpaceRequirement guardRequirement = guard.getSpaceRequirement();

        final SpaceRequirement branchTrueRequirement = branchTrue.stream()
              .map(Statement::getSpaceRequirement)
              .reduce(SpaceRequirement::merge)
              .orElse(SpaceRequirement.NONE);

        final SpaceRequirement branchFalseRequirement = branchFalse.stream()
              .map(Statement::getSpaceRequirement)
              .reduce(SpaceRequirement::merge)
              .orElse(SpaceRequirement.NONE);

        return SpaceRequirement.merge(guardRequirement, branchTrueRequirement, branchFalseRequirement);
    }

    @Override
    public Optional<StatementNext> run() {
        log.trace("Running [{}]", this);
        final Value value = guard.getValue();
        if (value.getBoolValue()) {
            log.trace("Processing branch THEN");
            return branchTrue.stream()
                  .map(Statement::run)
                  .filter(Optional::isPresent)
                  .findAny()
                  .orElse(Optional.empty());
        } else {
            log.trace("Processing branch ELSE");
            return branchFalse.stream()
                  .map(Statement::run)
                  .findAny()
                  .orElse(Optional.empty());
        }
    }
}
