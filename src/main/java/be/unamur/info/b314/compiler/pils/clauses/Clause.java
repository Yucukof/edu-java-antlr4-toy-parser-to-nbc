package be.unamur.info.b314.compiler.pils.clauses;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Declaration;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.statements.Statement;
import be.unamur.info.b314.compiler.pils.statements.StatementNext;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Une clause est une portion de code au sein d'un programme, qui peut contenir un certain nombre de déclarations, des
 * instructions et 0, 1 ou plusieurs instructions NEXT. Elle forme une unité logique.
 *
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder(toBuilder = true)
public abstract class Clause {

    /**
     * Les déclarations locales de la clause.
     */
    @Singular
    private final List<Variable> declarations;
    /**
     * Les instructions de la clause.
     */
    @Singular
    private final List<Statement> statements;

    @Override
    public String toString() {
        return (declarations.size() > 0
              ? "declare local\n\t" + declarations.stream().map(Declaration::getDeclaration).collect(Collectors.joining("\n\t")) + "\n"
              : "")
              + "do\n"
              + (statements.size() > 0
              ? "\t" + statements.stream().map(Objects::toString).collect(Collectors.joining("\n\t")) + "\n"
              : "")
              + "done";
    }

    /**
     * Indique si la clause contient des déclarations.
     *
     * @return vrai si la clause contient au moins une déclaration, faux sinon.
     */
    public boolean hasDeclarations() {
        return declarations.size() > 0;
    }

    /**
     * Une méthode permettant de vérifier si la clause est correcte d'un point de vue sémantique.
     *
     * @return vrai si la clause est valide, faux sinon.
     */
    public boolean isValid() {
        return declarations.stream().allMatch(Declaration::isValid)
              && statements.stream().allMatch(Statement::isValid);
    }

    public SpaceRequirement getSpaceRequirement() {
        return statements.stream()
              .map(Statement::getSpaceRequirement)
              .reduce(SpaceRequirement::merge)
              .orElse(SpaceRequirement.NONE);
    }

    public Optional<StatementNext> run() {
        return statements.stream()
              .map(Statement::run)
              .filter(Optional::isPresent)
              .findFirst()
              .orElse(Optional.empty());
    }
}
