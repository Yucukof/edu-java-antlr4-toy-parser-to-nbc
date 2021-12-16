package be.unamur.info.b314.compiler.pils.values.references;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.exceptions.IndexOutOfBoundsException;
import be.unamur.info.b314.compiler.pils.exceptions.*;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.keywords.Square;
import be.unamur.info.b314.compiler.pils.values.Value;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Hadrien BAILLY
 * @see Variable
 */
@Slf4j
@Data
@Builder
public class VariableReference implements Value {

    /**
     * Le tableau de valeurs.
     */
    private final Variable variable;
    /**
     * Les indices de la valeur à rechercher dans le tableau
     */
    @Singular
    private final List<Expression> indexes;

    /**
     * @return Le nom de la variable.
     */
    public String getName() {
        return variable.getName();
    }

    @Override
    public Boolean getBoolValue() {
        if (isValid()) {
            if (isBoolean()) {
                final Optional<Value> value;
                if (isScalar()) {
                    value = Optional.ofNullable(variable.get());
                } else {
                    value = Optional.ofNullable(variable.getAt(indexes));
                }
                return value.map(Value::getBoolValue)
                      .orElse(Type.BOOLEAN.getDefault().getBoolValue());
            } else {
                throw new InvalidDataTypeException(String.format("Cannot get [%s] value from [%s] variable", Type.BOOLEAN, getType()));
            }
        } else {
            throw new ArrayException();
        }
    }

    @Override
    public Type getType() {
        return variable.getType();
    }

    @Override
    public Integer getIntValue() {
        if (isValid()) {
            if (isInteger()) {
                final Optional<Value> value;
                if (isScalar()) {
                    value = Optional.ofNullable(variable.get());
                } else {
                    value = Optional.ofNullable(variable.getAt(indexes));
                }
                return value.map(Value::getIntValue)
                      .orElse(Type.INTEGER.getDefault().getIntValue());
            } else {
                throw new InvalidDataTypeException(String.format("Cannot get [%s] value from [%s] variable", Type.INTEGER, getType()));
            }
        } else {
            throw new ArrayException();
        }
    }

    @Override
    public Square getSquareValue() {
        if (isValid()) {
            if (isSquare()) {
                final Optional<Value> value;
                if (isScalar()) {
                    value = Optional.ofNullable(variable.get());
                } else {
                    value = Optional.ofNullable(variable.getAt(indexes));
                }
                return value.map(Value::getSquareValue)
                      .orElse(Type.SQUARE.getDefault().getSquareValue());
            } else {
                throw new InvalidDataTypeException(String.format("Cannot get [%s] value from [%s] variable", Type.SQUARE, getType()));
            }
        } else {
            throw new ArrayException();
        }
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
    public boolean isArray() {
        return variable.isArray();
    }

    @Override
    public boolean isValid() {
        return variable != null && variable.isValid()
              && indexes != null
              && validateScalar() && validateNumberOfIndexes() && validateNegativeIndexes() && validateIndexOutOfBound();
    }

    @Override
    public boolean isConstant() {
        return indexes.stream().allMatch(Expression::isPrimitive);
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        return indexes.stream()
              .map(Expression::getSpaceRequirement)
              .reduce(SpaceRequirement::combine)
              .orElse(SpaceRequirement.NONE);
    }

    public boolean isScalar() {
        return variable.isScalar();
    }

    /**
     * Vérifie si la référence ne porte pas une variable scalaire si elle présente des indices.
     *
     * @return vrai si la variable est scalaire et n'a aucun indice.
     * @throws InvalidNumberOfIndexesException si elle est scalaire et qu'elle présente au moins un indice.
     */
    private boolean validateScalar() {
        if (variable.isScalar() && indexes.size() > 0) {
            throw new InvalidNumberOfIndexesException(String.format("Cannot get indexed value from scalar variable [%s]", variable.getName()));
        }
        return true;
    }

    /**
     * Vérifie si le nombre d'indices correspond au nombre de dimensions déclarées par la variable.
     *
     * @return vrai si le nombre d'indices est égal au nombre de dimensions.
     * @throws InvalidNumberOfIndexesException si le nombre d'indices est strictement plus petit ou plus grand que le
     *                                         nombre de dimensions déclarées.
     */
    private boolean validateNumberOfIndexes() {
        if (variable.getDimensions().size() != indexes.size()) {
            throw new InvalidNumberOfIndexesException(String.format("The number of indexes (%s) does not match variable [%s] dimensions (%s)", indexes.size(), variable.getName(), variable.getDimensions().size()));
        }
        return true;
    }

    /**
     * Vérifie si les indices calculables ne sont pas négatifs.
     * <br> Un indice est calculable si sa valeur est constante (càd non dépendante d'une variable).
     *
     * @return vrai si aucun indice calculable n'est strictement inférieur à zéro.
     * @throws NegativeArrayIndexException si l'un des indices est strictement négatif.
     */
    private boolean validateNegativeIndexes() {
        if (indexes.stream().filter(Expression::isPrimitive).map(Expression::getValue).map(Value::getIntValue).anyMatch(i -> i < 0)) {
            throw new NegativeArrayIndexException(String.format("One of the indexes in [%s] is negative", indexes.stream().map(Objects::toString).collect(Collectors.joining(","))));
        }
        return true;
    }

    /**
     * Vérifie si l'un des indices calculables n'est pas supérieur ou égale à la dimension déclarée correspondante.
     *
     * @return vrai si aucun indice calculable n'est supérieur ou égal à la dimension déclarée correspondante.
     * @throws IndexOutOfBoundsException si au moins un des indices est supérieur ou égal à la dimension déclarée
     *                                   correspondante.
     */
    private boolean validateIndexOutOfBound() {
        if (IntStream.range(0, variable.getDimensions().size()).anyMatch(i -> indexes.get(i).isPrimitive() && variable.getDimensions().get(i) <= indexes.get(i).getValue().getIntValue())) {
            final String indexesAsString = indexes.stream().map(Objects::toString).collect(Collectors.joining(","));
            final String dimensionsAsString = variable.getDimensions().stream().map(Objects::toString).collect(Collectors.joining(","));
            throw new IndexOutOfBoundsException(String.format("One of the indexes in [%s] is greater that the corresponding dimension [%s]", indexesAsString, dimensionsAsString));
        }
        return true;
    }

    public Value get() {
        if (accept(indexes)) {
            log.trace("Retrieved value from [{}]", this);
            return variable.getAt(indexes);
        } else {
            // TODO 30/03/2021 [HBA]: fournir une exception appropriée aux cas "pas assez d'indices" et "indices hors dimensions".
            throw new ArrayException("Indexes are not valid.");
        }
    }

    public boolean accept(@NonNull final List<Expression> indexes) {
        return variable.getDimensions().size() == indexes.size()
              && IntStream.range(0, variable.getDimensions().size())
              .filter(i -> indexes.get(i).isPrimitive())
              .allMatch(i -> variable.getDimensions().get(i) > indexes.get(i).getValue().getIntValue());
    }

    public void set(@NonNull final Value value) {
        if (accept(indexes)) {
            log.trace("Assigned value [{}] to [{}]", value, this);
            variable.setAt(value, indexes);
        } else {
            // TODO 30/03/2021 [HBA]: fournir une exception appropriée aux cas "pas assez d'indices" et "indices hors dimensions".
            throw new ArrayException("Indexes are not valid.");
        }
    }

    /**
     * Vérifie si la référence courante est valide.
     *
     * @return la référence courante si elle est valide
     * @throws InvalidNumberOfIndexesException si la variable est scalair et des indices son présents ou si le nombre
     *                                         d'indices ne correspond pas au nombre de dimensions de la variable.
     * @throws NegativeArrayIndexException     si l'un des indices calculables est inférieur à zéro.
     * @throws IndexOutOfBoundsException       si l'un des indices calculable est supérieur ou égal à la dimension
     *                                         correspondante.
     */
    public VariableReference validate() {
        assert isValid();
        return this;
    }

    @Override
    public String toString() {
        return variable.getName()
              + (indexes.size() > 0 ? "[" + indexes.stream().map(Objects::toString).collect(Collectors.joining(",")) + "]" : "");
    }
}
