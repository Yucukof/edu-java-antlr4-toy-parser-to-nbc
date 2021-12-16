package be.unamur.info.b314.compiler.pils.program;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.clauses.Clause;
import be.unamur.info.b314.compiler.pils.clauses.ClauseWhen;
import be.unamur.info.b314.compiler.pils.declarations.Declaration;
import be.unamur.info.b314.compiler.pils.imports.Import;
import be.unamur.info.b314.compiler.pils.statements.StatementNext;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Hadrien BAILLY
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Strategy extends Program {

    @Builder.Default
    private final List<ClauseWhen> clauses = new ArrayList<>();
    /**
     * Le fichier du monde à importer.
     */
    private Import world;

    @Override
    public String toString() {
        return "declare and retain\n"
              + (world != null ? "\t" + world + "\n" : "")
              + (getDeclarations().size() == 0 ? "" : "\t" + getDeclarations().stream().map(Declaration::getDeclaration).collect(Collectors.joining("\n\t")) + "\n")
              + "when your turn\n"
              + (clauses.size() == 0 ? "" : clauses.stream().map(Objects::toString).collect(Collectors.joining("\n")) + "\n")
              + getClauseDefault();
    }

    @Override
    public void add(final ClauseWhen clause) {
        this.clauses.add(clause);
    }

    @Override
    public void add(final Import world) {
        this.world = world;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
              && (world == null || world.isValid())
              && clauses.stream().allMatch(ClauseWhen::isValid);
    }

    public SpaceRequirement getSpaceRequirement() {
        final SpaceRequirement clausesRequirement = clauses.stream()
              .map(Clause::getSpaceRequirement)
              .reduce(SpaceRequirement::merge)
              .orElse(SpaceRequirement.NONE);
        final SpaceRequirement defaultRequirement = super.getSpaceRequirement();
        return SpaceRequirement.merge(clausesRequirement, defaultRequirement);
    }

    public boolean hasImport() {
        return this.world != null && this.world.isValid();
    }

    public boolean hasClausesWhen() {
        return this.clauses != null && !this.clauses.isEmpty();
    }

    public StatementNext run() {
        return clauses.stream()
              .filter(ClauseWhen::isTriggered)
              .map(Clause::run)
              .findFirst()
              .orElse(getClauseDefault().run())
              .orElse(StatementNext.DO_NOTHING);

    }
}
