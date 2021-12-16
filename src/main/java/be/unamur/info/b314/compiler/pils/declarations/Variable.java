package be.unamur.info.b314.compiler.pils.declarations;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.nbc.definitions.Definition;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.exceptions.IndexOutOfBoundsException;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidNumberOfIndexesException;
import be.unamur.info.b314.compiler.pils.exceptions.NegativeArrayIndexException;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.values.Value;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import be.unamur.info.b314.compiler.visitors.VariableVisitor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Hadrien BAILLY
 * @see VariableReference
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Variable extends Symbol implements Declaration {

    /**
     * Type effectif de la variable (doit être l'un des types énumérés).
     *
     * @see Type
     */
    private Type type;
    /**
     * Dimensions de la variable.<br>
     * Une variable à une dimension de taille 1 est scalaire, sinon c'est un tableau.
     *
     * @see #isScalar()
     */

    @Singular
    private List<Integer> dimensions;
    /**
     * Identifiant du registre correspondant dans le programme NBC
     */
    private Identifier identifier;

    /**
     * Valeur(s) de la variable.
     */
    @Builder.Default
    private Map<String, Value> values = new HashMap<>();

    /**
     * Crée une variable depuis un point de contexte.
     *
     * @param ctx      le contexte à partir duquel créer la variable.
     * @param symboles la table de symboles du programme.
     * @return une variable.
     */
    public static Variable from(final GrammarParser.VarDeclContext ctx, final Context symboles) {
        return new VariableVisitor(symboles).visitVarDecl(ctx);
    }

    /**
     * Retourne la valeur courante de la variable si disponible, sinon la valeur par défaut.
     *
     * @return Une valeur simple.
     * @see #getAt(List)
     */
    public Value get() {
        if (isScalar()) {
            return Optional.ofNullable(values)
                    .map(value -> getAt(Collections.emptyList()))
                    .orElseGet(() ->
                            Optional.ofNullable(type)
                                    .map(Type::get)
                                    .orElseThrow(() -> new IllegalArgumentException(
                                            String.format("Cannot get default value for variable [%s] (type is not declared).", getName())
                                    )));
        }
        throw new UnsupportedOperationException(String.format("Cannot get non-indexed value from array variable [%s]", getName()));
    }

    /**
     * Indique si la variable est de type scalaire ou tableau.
     *
     * @return vrai si la variable ne peut contenir qu'une seule valeur, faux sinon.
     */
    public boolean isScalar() {
        return dimensions.size() == 0;
    }

    /**
     * Renvoie la valeur aux indices précisés.
     *
     * @param indexes les indices/coordonnées de la valeur au sein du tableau.
     * @return Une valeur simple.
     * @throws InvalidNumberOfIndexesException si le nombre d'indices ne correspond pas au nombre de dimensions du
     *                                         tableau.
     * @throws NegativeArrayIndexException     si l'un des indices donnés est négatif.
     * @throws IndexOutOfBoundsException       si l'un des indices est supérieur à la taille déclarée du tableau.
     * @see #get()
     */
    public Value getAt(final List<Expression> indexes) throws
            InvalidNumberOfIndexesException, NegativeArrayIndexException, IndexOutOfBoundsException {
        // STEP 1 - Vérifier qu'il y a suffisamment d'indices par rapport aux dimensions.
        if (indexes.size() != dimensions.size()) {
            throw new InvalidNumberOfIndexesException("The number of indexes " + indexes.size() + " does not match the number of dimensions " + dimensions.size() + "");
        }
        // STEP 2 - Vérifier si les indices sont acceptables
        verifyIndex(indexes);
        // STEP 3 - Créer la clé vers la valeur dans la map
        final String key = createKey(indexes);
        // STEP 3 - Retourner la valeur
        return values.get(key);
    }

    /**
     * Vérifie si les indices données sont compatibles avec la variable courante.
     *
     * @param indexes les indices à contrôler
     * @throws NegativeArrayIndexException si un indice est négatif.
     * @throws IndexOutOfBoundsException   si un indice est supérieur ou égale à la dimension correspondante.
     */
    private void verifyIndex(final List<Expression> indexes) throws
            NegativeArrayIndexException, IndexOutOfBoundsException {
        // Vérifier que les indices sont contenus au sein des dimensions:
        if (containsNegativeIndex(indexes)) {
            throw new NegativeArrayIndexException(String.format("One of the indexes is negative: [%s]", indexes.stream().map(String::valueOf).collect(Collectors.joining(","))));
        }
        // Vérifier que les indices sont contenus au sein des dimensions:
        if (containsInvalidIndex(indexes)) {
            final String indexesAsString = indexes.stream().map(String::valueOf).collect(Collectors.joining(","));
            final String dimensionsAsString = dimensions.stream().map(String::valueOf).collect(Collectors.joining(","));
            throw new IndexOutOfBoundsException(String.format("One of the indexes in [%s] is greater that the declared dimensions [%s]", indexesAsString, dimensionsAsString));
        }
    }

    private String createKey(final List<Expression> indexes) {
        return indexes.stream()
                .map(Expression::getValue)
                .map(Value::getIntValue)
                .map(String::valueOf)
                .collect(Collectors.joining("|"));
    }

    /**
     * Vérifie si la liste des indices contient une valeur négative.
     *
     * @param indexes la liste des indices à contrôler.
     * @return vrai si au moins un des indices est négatif, faux sinon.
     */
    private boolean containsNegativeIndex(final List<Expression> indexes) {
        return indexes.stream()
                .filter(Expression::isPrimitive)
                .anyMatch(expr -> expr.getValue().getIntValue() < 0);
    }

    /**
     * Vérifie si l'un des indices dépasse la taille déclarée du tableau.
     *
     * @param indexes la liste des indices à contrôler.
     * @return vrai si au moins un des indices est supérieur ou égal à sa dimension déclarée, faux sinon.
     */
    private boolean containsInvalidIndex(final List<Expression> indexes) {
        return IntStream.range(0, indexes.size())
                .filter(i -> indexes.get(i).isPrimitive())
                .anyMatch(i -> indexes.get(i).getValue().getIntValue() > dimensions.get(i));
    }

    public void set(final Value value) {
        if (isScalar()) {
            setAt(value, Collections.emptyList());
        } else {
            throw new UnsupportedOperationException(String.format("Cannot assign value without index to array variable [%s]", getName()));
        }
    }

    /**
     * Modifie ou insère la valeur à l'emplacement désigné par les indices.
     *
     * @param value   la valeur à insérer dans le tableau
     * @param indexes la liste des coordonnées désignant l'emplacement.
     */
    public void setAt(final Value value, final List<Expression> indexes) {
        // STEP 1 - Vérifier si le type de la valeur correspond au type déclaré
        if (type != value.getType()) {
            // Si non, alors lever une exception de typage
            throw new InvalidDataTypeException(String.format("Cannot assign [%s] value to [%S] variable [%s]", value.getType(), type, getName()));
        }
        // STEP 2 - Vérifier si les indices sont acceptables
        verifyIndex(indexes);
        // STEP 3 - Calculer la clé
        final String key = createKey(indexes);
        // STEP 4 - Mettre à jour la valeur
        values.put(key, value);
    }

    public boolean isInteger() {
        return type.equals(Type.INTEGER);
    }

    public boolean isBoolean() {
        return type.equals(Type.BOOLEAN);
    }

    public boolean isSquare() {
        return type.equals(Type.SQUARE);
    }

    public boolean isArray() {
        return !isScalar();
    }

    @Override
    public String getDeclaration() {
        return getArgument() + ";";
    }

    public String getArgument() {
        return getName()
                + (isScalar() ? "" : "[" + getDimensions().stream().map(Objects::toString).collect(Collectors.joining(",")) + "]")
                + " as "
                + type.getToken();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && type != null && !type.equals(Type.VOID)
                && dimensions.stream().allMatch(dimension -> dimension >= 0);
    }

    public boolean isConstant() {
        return values.values().stream().allMatch(Value::isConstant);
    }

    public void associate(final Definition definition) {
        this.identifier = definition.getIdentifier();
    }

    public Identifier getIdentifier(){
        return Optional.ofNullable(identifier)
                .orElse(new Identifier(getName()));
    }
}
