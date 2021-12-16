package be.unamur.info.b314.compiler.pils.values.references;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.routines.Subroutine;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.declarations.Declaration;
import be.unamur.info.b314.compiler.pils.declarations.Function;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.exceptions.IndexOutOfBoundsException;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidFunctionCallException;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.keywords.Square;
import be.unamur.info.b314.compiler.pils.values.Value;
import be.unamur.info.b314.compiler.visitors.ExpressionVisitor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Hadrien BAILLY
 */
@Data
@Builder
public class FunctionCall implements Value {

    /**
     * Le nom de la fonction appelée.
     */
    private final Function function;

    /**
     * La liste des arguments passés à la fonction.
     */
    @Singular
    private final List<Expression> arguments;

    /**
     * Fonction récupérant les données (nom, type et paramètres) de l'appel de fonction
     *
     * @param ctx     : @param ctx : l'instruction en cours de traitement.
     * @param symbols : les tables des symboles courantes
     * @return un objet "FunctionCall" contenant le type, les paramètres et le nom de l'appel de fonction
     */
    public static FunctionCall from(final GrammarParser.FunctionContext ctx, final Context symbols) {

        // STEP 1 - récupérer le nom de la fonction appelée
        final String name = ctx.ID().getText();

        // STEP 2 - Récupérer la fonction associée
        final Function function = symbols.getFunction(name);

        // STEP 3 - Récupérer les arguments passés à la fonction
        final ExpressionVisitor visitor = new ExpressionVisitor(symbols);
        final List<Expression> arguments = ctx.expression().stream()
              .map(visitor::visitExpression)
              .collect(Collectors.toList());

        // STEP 4 - Construire et valider l'objet appel de fonction
        return FunctionCall.builder()
              .function(function)
              .arguments(arguments)
              .build()
              .validate();
    }

    /**
     * Vérifie si l'appel de fonction est valide d'un point de vue sémantique.
     *
     * @return l'appel de fonction s'il est valide.
     * @throws InvalidFunctionCallException si le nombre d'arguments n'est pas correct, si l'un des arguments effectifs
     *                                      est invalide ou ne correspond pas au type de l'argument déclaré
     *                                      correspondant.
     * @throws AssertionError               si la fonction est nulle ou n'est pas valide, ou si la liste d'argument est
     *                                      nulle.
     */
    public FunctionCall validate() {

        assert function != null;
        assert arguments != null;

        // STEP 1 - Vérifier si le nombre d'arguments passés à la fonction correspond au nombre d'arguments déclarés.
        validateNumberOfArguments();

        // STEP 2 - Vérifier que tous les arguments sont valides stricto sensu.
        validateArguments();

        // STEP 3 - Vérifier si les arguments passés sont de bons
        validateTypeOfArguments();
        return this;
    }

    /**
     * Vérifie si le nombre d'arguments passé à la fonction correspond au nombre d'arguments déclarés.
     *
     * @return vrai si le nombre d'arguments correspond au nombre d'arguments déclarés dans la fonction.
     * @throws InvalidFunctionCallException si le nombre d'arguments ne correspond pas.
     * @throws AssertionError               si la fonction est nulle ou n'est pas valide, ou si la liste d'argument est
     *                                      nulle.
     */
    private boolean validateNumberOfArguments() {

        final List<Variable> formalArguments = function.getArguments();

        if (formalArguments.size() != arguments.size()) {
            throw new InvalidFunctionCallException(String.format("The number of arguments (%s) does not match function %s declaration (%s)", this.arguments.size(), function.getName(), formalArguments.size()));
        }
        return true;
    }

    /**
     * Vérifie si les arguments passés à la fonction sont valides en eux-mêmes, indépendamment de la fonction.
     *
     * @return vrai si tous les arguments sont valides.
     * @throws InvalidFunctionCallException si l'un des arguments est invalide.
     * @throws AssertionError               si la liste des arguments est nulle.
     */
    private boolean validateArguments() {

        final List<ImmutablePair<Integer, Expression>> invalidArguments = IntStream.range(0, this.arguments.size())
              .filter(i -> !arguments.get(i).isValid())
              .mapToObj(i -> new ImmutablePair<>(i, arguments.get(i)))
              .collect(Collectors.toList());

        if (!invalidArguments.isEmpty()) {
            final String invalidArgumentsAsString = invalidArguments.stream()
                  .map(triple -> String.format("(%s): %s is not valid", triple.getLeft(), triple.getRight()))
                  .collect(Collectors.joining("\n"));
            throw new InvalidFunctionCallException(String.format("One or more arguments are not valid:%s%s", invalidArguments.size() > 1 ? "\n" : "", invalidArgumentsAsString));
        }
        return true;
    }

    /**
     * Vérifie si les arguments passés à la fonction correspondent aux types des arguments déclarés dans la fonction.
     *
     * @return vrai si le nombre d'arguments correspond au nombre d'arguments déclarés et si les arguments déclarés et
     *       effectifs correspondants sont de même type.
     * @throws InvalidFunctionCallException si un ou plusieurs arguments effectifs ont un type différent de celui de
     *                                      l'argument déclaré correspondant.
     * @throws IndexOutOfBoundsException    si le nombre d'arguments effectif est supérieur à celui des arguments
     *                                      déclarés.
     * @throws AssertionError               si la fonction est nulle ou n'est pas valide, ou si la liste d'argument est
     *                                      nulle.
     */
    private boolean validateTypeOfArguments() {

        final List<Variable> formalArguments = function.getArguments();
        final List<ImmutableTriple<Integer, Variable, Expression>> wrongArguments = IntStream.range(0, this.arguments.size())
              .filter(i -> !formalArguments.get(i).getType().equals(arguments.get(i).getType()))
              .mapToObj(i -> new ImmutableTriple<>(i, formalArguments.get(i), arguments.get(i)))
              .collect(Collectors.toList());

        if (!wrongArguments.isEmpty()) {
            final String wrongArgumentsAsString = wrongArguments.stream()
                  .map(triple -> String.format("(%s): expected %s, found %s", triple.getLeft(), triple.getMiddle(), triple.getRight()))
                  .collect(Collectors.joining("\n"));
            throw new InvalidFunctionCallException(String.format("One or more arguments do not match function %s declaration:%s%s", function.getName(), wrongArguments.size() > 1 ? "\n" : "", wrongArgumentsAsString));
        }
        return true;
    }

    /**
     * Reconstitue l'appel de fonction sous forme d'un string
     *
     * @return un String contenant les données de la fonction courante.
     */
    @Override
    public String toString() {
        return function.getName()
              + "("
              + arguments.stream().map(Object::toString).collect(Collectors.joining(", ")) + ")";
    }

    /**
     * @return le nom de la fonction
     */
    public String getName() {
        return function.getName();
    }

    @Override
    public Type getType() {
        return function.getType();
    }

    @Override
    public boolean isInteger() {
        return getType().equals(Type.INTEGER);
    }

    @Override
    public boolean isBoolean() {
        return getType().equals(Type.BOOLEAN);
    }

    @Override
    public boolean isSquare() {
        return getType().equals(Type.SQUARE);
    }

    @Override
    public boolean isVoid() {
        return getType().equals(Type.VOID);
    }

    @Override
    public boolean isValid() {
        try {
            return function != null && arguments != null
                  && validateArguments() && validateNumberOfArguments() && validateTypeOfArguments();
        } catch (InvalidFunctionCallException e) {
            return false;
        }
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public Boolean getBoolValue() {
        return function.run(arguments).getBoolValue();
    }

    @Override
    public Integer getIntValue() {
        return function.run(arguments).getIntValue();
    }

    @Override
    public Square getSquareValue() {
        return function.run(arguments).getSquareValue();
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        return arguments.stream()
              .map(Expression::getSpaceRequirement)
              .reduce(SpaceRequirement::combine)
              .orElse(SpaceRequirement.NONE);
    }
}
