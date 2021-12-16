package be.unamur.info.b314.compiler.pils.program;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.clauses.ClauseDefault;
import be.unamur.info.b314.compiler.pils.clauses.ClauseWhen;
import be.unamur.info.b314.compiler.pils.declarations.Declaration;
import be.unamur.info.b314.compiler.pils.declarations.Function;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.imports.Import;
import be.unamur.info.b314.compiler.pils.statements.Statement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Un programme est une unité complète de code pouvant être exécuté par un moteur PILS.
 *
 * @author Hadrien BAILLY
 * @see Strategy#run()
 * @see World#run()
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class Program {

    @Builder.Default
    private final List<Declaration> declarations = new ArrayList<>();
    private ClauseDefault clauseDefault;

    public void add(final Declaration declaration) {
        this.declarations.add(declaration);
    }

    public void add(final Statement statement) {
        throw new UnsupportedOperationException();
    }

    public void add(final ClauseWhen clause) {
        throw new UnsupportedOperationException();
    }

    public void add(final ClauseDefault clause) {
        this.clauseDefault = clause;
    }

    public void add(final Import world) {
        throw new UnsupportedOperationException();
    }

    public boolean isValid() {
        return (declarations.stream().allMatch(Declaration::isValid))
              && clauseDefault != null && clauseDefault.isValid();
    }

    public boolean hasDeclarations() {
        return declarations.size() > 0;
    }

    public boolean hasVariables() {
        return getVariableDeclarations().size() > 0;
    }

    public List<Variable> getVariableDeclarations() {
        return declarations.stream()
              .filter(decl -> decl instanceof Variable)
              .map(declaration -> (Variable) declaration)
              .collect(Collectors.toList());
    }

    public boolean hasFunctions() {
        return getFunctionDeclarations().size() > 0;
    }

    public List<Function> getFunctionDeclarations() {
        return declarations.stream()
              .filter(decl -> decl instanceof Function)
              .map(declaration -> (Function) declaration)
              .collect(Collectors.toList());
    }

    public SpaceRequirement getSpaceRequirement() {
        final SpaceRequirement declarationRequirements = declarations.stream()
              .map(Declaration::getSpaceRequirement)
              .reduce(SpaceRequirement::merge)
              .orElse(SpaceRequirement.NONE);
        return SpaceRequirement.merge(declarationRequirements, clauseDefault.getSpaceRequirement());
    }
}

