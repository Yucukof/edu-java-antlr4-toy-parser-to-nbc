package be.unamur.info.b314.compiler.pils.program;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Declaration;
import be.unamur.info.b314.compiler.pils.statements.Statement;
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
public class World extends Program {

    @Builder.Default
    private List<Statement> statements = new ArrayList<>();

    @Override
    public String toString() {
        return "declare and retain"
              + (getDeclarations().size() == 0 ? "" : "\n\t" + getDeclarations().stream().map(Declaration::getDeclaration).collect(Collectors.joining("\n\t")))
              + (statements.size() == 0 ? "" : "\n\t" + statements.stream().map(Objects::toString).collect(Collectors.joining("\n\t")))
              + "\n" + getClauseDefault();
    }

    @Override
    public void add(final Statement statement) {
        this.statements.add(statement);
    }

    @Override
    public boolean isValid() {
        return super.isValid()
              && statements.stream().allMatch(Statement::isValid);
    }

    public SpaceRequirement getSpaceRequirement() {
        final SpaceRequirement statementsRequirement = statements.stream()
              .map(Statement::getSpaceRequirement)
              .reduce(SpaceRequirement::merge)
              .orElse(SpaceRequirement.NONE);
        final SpaceRequirement defaultRequirement = super.getSpaceRequirement();
        return SpaceRequirement.merge(statementsRequirement, defaultRequirement);
    }

    /**
     * Déclenche l'exécution du programme World.
     * <br>
     * Attention, cette méthode a pour effet de bord de modifier le contexte et les valeurs des variables.
     */
    public void run() {
        getStatements().forEach(Statement::run);
        getClauseDefault().run();
    }

}
