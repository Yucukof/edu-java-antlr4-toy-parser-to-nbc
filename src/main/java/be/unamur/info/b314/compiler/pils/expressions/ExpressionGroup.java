package be.unamur.info.b314.compiler.pils.expressions;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.pils.keywords.Square;
import be.unamur.info.b314.compiler.pils.values.Value;
import lombok.Builder;
import lombok.Data;

/**
 * Un groupe d'expression est une méthode de regroupement de sous-expression permettant d'outrepasser l'ordre de
 * priorité de résolution des opérations.
 * <br>
 * Toute l'opération contenue dans le groupe doit être résolue avant de pouvoir être utilisée dans une autre opération,
 * même si cette seconde opération semble avoir une plus haute priorité.
 *
 * @author Hadrien BAILLY
 */
@Data
@Builder(toBuilder = true)
public class ExpressionGroup implements Expression, Value {

    /**
     * L'expression interne du groupe.
     */
    private final Expression expression;

    @Override
    public String toString() {
        return "(" + expression + ")";
    }

    @Override
    public Type getType() {
        return expression.getType();
    }

    @Override
    public Value getValue() {
        return expression.getValue();
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean isValid() {
        return expression != null && expression.isValid();
    }

    @Override
    public boolean isBoolean() {
        return isValid() && expression.isBoolean();
    }

    @Override
    public boolean isInteger() {
        return isValid() && expression.isInteger();
    }

    @Override
    public boolean isSquare() {
        return isValid() && expression.isSquare();
    }

    @Override
    public Expression validate() {
        return expression.validate();
    }

    @Override
    public ExpressionNode append(final Expression expression, final Operator operation) {
        throw new UnsupportedOperationException(String.format("Cannot append expression [%s] to a group", expression));
    }

    @Override
    public boolean isVoid() {
        return expression.isVoid();
    }

    @Override
    public boolean isPrimitive() {
        return expression.isPrimitive();
    }

    @Override
    public boolean isConstant() {
        return expression.isConstant();
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        return expression.getSpaceRequirement();
    }

    @Override
    public boolean needsPreprocessing() {
        return expression.needsPreprocessing();
    }

    @Override
    public Integer getIntValue() {
        if (isInteger()) {
            return expression.getValue().getIntValue();
        }
        throw new InvalidDataTypeException(String.format("Cannot get %s value from %s expression.", Type.INTEGER, expression.getType()));
    }

    @Override
    public Boolean getBoolValue() {
        if (isBoolean()) {
            return expression.getValue().getBoolValue();
        }
        throw new InvalidDataTypeException(String.format("Cannot get %s value from %s expression.", Type.BOOLEAN, expression.getType()));
    }

    @Override
    public Square getSquareValue() {
        if (isSquare()) {
            return expression.getValue().getSquareValue();
        }
        throw new InvalidDataTypeException(String.format("Cannot get %s value from %s expression.", Type.SQUARE, expression.getType()));
    }
}
