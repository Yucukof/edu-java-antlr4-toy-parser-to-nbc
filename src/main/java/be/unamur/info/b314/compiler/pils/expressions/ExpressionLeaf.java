package be.unamur.info.b314.compiler.pils.expressions;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidExpressionException;
import be.unamur.info.b314.compiler.pils.keywords.Square;
import be.unamur.info.b314.compiler.pils.values.Value;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveBoolean;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveInteger;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveSquare;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveVoid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Un opérande est une expression à un seul terme, dont on peut immédiatement obtenir le type ou la valeur
 *
 * @author Hadrien BAILLY
 */
@Data
@Builder
@AllArgsConstructor
public class ExpressionLeaf implements Expression, Value {

    public static final ExpressionLeaf VOID = new ExpressionLeaf(PrimitiveVoid.VOID);
    public static final ExpressionLeaf ZERO = new ExpressionLeaf(PrimitiveInteger.ZERO);
    public static final ExpressionLeaf ONE = new ExpressionLeaf(PrimitiveInteger.ONE);
    public static final ExpressionLeaf TRUE = new ExpressionLeaf(PrimitiveBoolean.TRUE);
    public static final ExpressionLeaf FALSE = new ExpressionLeaf(PrimitiveBoolean.FALSE);
    public static final ExpressionLeaf DIRT = new ExpressionLeaf(PrimitiveSquare.DIRT);
    public static final ExpressionLeaf ROCK = new ExpressionLeaf(PrimitiveSquare.ROCK);

    /**
     * Valeur simple effective de l'opérande.
     */
    private Value value;

    public ExpressionLeaf(final int integer) {
        this.value = new PrimitiveInteger(integer);
    }

    public ExpressionLeaf(final Square square) {
        this.value = new PrimitiveSquare(square);
    }

    public ExpressionLeaf(final boolean bool) {
        this.value = new PrimitiveBoolean(bool);
    }

    @Override
    public Boolean getBoolValue() {
        if (isBoolean()) {
            return value.getBoolValue();
        }
        throw new InvalidDataTypeException(String.format("Cannot get [%s] value from [%s] leaf.", Type.BOOLEAN, value.getType()));
    }

    @Override
    public Integer getIntValue() {
        if (isInteger()) {
            return value.getIntValue();
        }
        throw new InvalidDataTypeException(String.format("Cannot get [%s] value from [%s] leaf.", Type.INTEGER, value.getType()));
    }

    @Override
    public Square getSquareValue() {
        if (isSquare()) {
            return value.getSquareValue();
        }
        throw new InvalidDataTypeException(String.format("Cannot get [%s] value from [%s] leaf.", Type.SQUARE, value.getType()));
    }

    @Override
    public Type getType() {
        return value.getType();
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean isValid() {
        return value != null && value.isValid();
    }

    @Override
    public boolean isBoolean() {
        return isValid() && value.isBoolean();
    }

    @Override
    public boolean isInteger() {
        return isValid() && value.isInteger();
    }

    @Override
    public boolean isSquare() {
        return isValid() && value.isSquare();
    }

    @Override
    public Expression validate() {
        if (this.isValid()) {
            return this;
        }
        throw new InvalidExpressionException(this.toString());
    }

    @Override
    public ExpressionNode append(final Expression rightmost, final Operator operator) {
        // Pas de conflit de précédence à traiter ici : l'ajout d'une opération consiste simplement à créer un Node.
        return ExpressionNode.builder()
              .left(this)
              .right(rightmost)
              .operator(operator)
              .build();
    }

    @Override
    public boolean isVoid() {
        return isValid() && value.isVoid();
    }

    @Override
    public boolean isPrimitive() {
        return value.isPrimitive();
    }

    @Override
    public boolean isConstant() {
        return value.isConstant();
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        return value.getSpaceRequirement();
    }

    @Override
    public boolean needsPreprocessing() {
        return !value.isPrimitive() && !value.isConstant();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
