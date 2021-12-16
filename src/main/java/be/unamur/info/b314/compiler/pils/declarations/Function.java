package be.unamur.info.b314.compiler.pils.declarations;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.statements.Statement;
import be.unamur.info.b314.compiler.pils.values.Value;
import be.unamur.info.b314.compiler.visitors.FunctionVisitor;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Function extends Symbol implements Declaration {

    /**
     * Type effectif de la fonction (doit être l'un des types énumérés).
     *
     * @see Type
     */
    private final Type type;
    /**
     * liste des arguments de la fonction.
     */
    @Singular
    private final List<Variable> arguments;
    /**
     * Liste des déclarations locales de la fonction.
     */
    @Singular
    private final List<Variable> declarations;
    /**
     * Liste des instructions composant la fonction.
     */
    @Builder.Default
    private final List<Statement> statements = new ArrayList<>();
    /**
     * Le niveau d'identation
     */
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private final int indentation = 0;
    /**
     * Expression de retour.
     */
    @Builder.Default
    private Expression output = ExpressionLeaf.VOID;
    /**
     * Identifiant en nbc du registre de retour
     */
    private Identifier outputIdentifier;

    /**
     * Fonction récuparant les données de la fonction courante
     *
     * @param ctx     : l'instruction en cours de traitement.
     * @param symbols : les tables des symboles courantes
     * @return : un objet "Function" contenant le nom, le type, les paramètres, et le corps de la fonction.
     */
    public static Function from(final GrammarParser.FctDeclContext ctx, final Context symbols) {
        return new FunctionVisitor(symbols).visitFctDecl(ctx);
    }

    /**
     * Reconstitue la fonction courante sous forme d'un string
     *
     * @return un String contenant les données de la fonction courante.
     */
    public String getDeclaration() {
        final String tab0 = StringUtils.repeat("\t", indentation) + "\t";
        final String tab1 = tab0 + "\t";

        return getName() + " as function (" + arguments.stream().map(Variable::getArgument).collect(Collectors.joining(", ")) + "): " + type
              + (declarations.size() != 0
              ? "\n" + tab0 + "declare local"
              + "\n" + tab1 + declarations.stream().map(Variable::getDeclaration).collect(Collectors.joining("\n" + tab1)) : "")
              + "\n" + tab0 + "do"
              + "\n" + (statements.size() != 0 ? tab1 + statements.stream().map(Object::toString).collect(Collectors.joining("\n" + tab1)) : "")
              + (isVoid() ? "" : "\n" + tab1 + "return " + output.toString())
              + "\n" + tab0 + "done";
    }

    /**
     * Vérifie si la fonction courante est de type VOID.
     *
     * @return vrai si le type déclaré est VOID, faux sinon.
     */
    public boolean isVoid() {
        return type.equals(Type.VOID);
    }

    public SpaceRequirement getSpaceRequirement() {

        final SpaceRequirement statementsRequirement = statements.stream()
              .map(Statement::getSpaceRequirement)
              .reduce(SpaceRequirement::merge)
              .orElse(SpaceRequirement.NONE);

        final SpaceRequirement outputRequirement = output.getSpaceRequirement();
        return SpaceRequirement.merge(statementsRequirement, outputRequirement);
    }

    /**
     * Ajoute un statement au corps de la fonction.
     *
     * @param statement le statement à rajouter.
     */
    public void add(final Statement statement) {
        this.statements.add(statement);
    }

    /**
     * Indique si la fonction contient des déclarations.
     *
     * @return vrai s'il y a au moins une déclaration, faux sinon.
     */
    public boolean hasDeclarations() {
        return declarations.size() > 0;
    }

    /**
     * Indique si la fonction contient des arguments formels.
     *
     * @return vrai si la fonction a au moins un argument, faux sinon.
     */
    public boolean hasArguments() {
        return arguments.size() > 0;
    }

    public Value run(final List<Expression> arguments) {

        log.trace("Running function [{}] with arguments [{}]", getName(), arguments.stream().map(Expression::getValue).collect(Collectors.toList()));

        IntStream.range(0, arguments.size())
              .forEach(i -> this.arguments.get(i).set(arguments.get(i).getValue()));

        statements.forEach(Statement::run);

        return output.getValue();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean isValid() {
        return super.isValid()
              && type != null
              && ((isVoid() && (output == null || output.isVoid()))
              ^ (!isVoid() && output != null && output.isValid() && output.getType().equals(type)))
              && arguments.stream().allMatch(Variable::isValid)
              && statements.stream().allMatch(Statement::isValid);
    }
}
