package be.unamur.info.b314.compiler.pils.expressions;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.pils.keywords.Square;
import be.unamur.info.b314.compiler.pils.values.Value;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Une opération est une expression à deux opérandes et un opérateur dont on va pouvoir calculer un résultat.
 * <br>Une opération peut être composées de plusieurs sous-opérations, qui seront évaluées avant le calcul principal.
 *
 * @author Hadrien BAILLY
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExpressionNode implements Expression, Value {

    /**
     * L'opérateur de l'expression d'opération.
     */
    private Operator operator;
    /**
     * L'opérande gauche.
     */
    private Expression left;
    /**
     * L'opérande droit.
     */
    private Expression right;

    @Override
    public Boolean getBoolValue() {
        if (isBoolean()) {
            return getValue().getBoolValue();
        } else {
            throw new InvalidDataTypeException(String.format("Cannot get [%s] value from [%s] expression", Type.INTEGER, getType()));
        }
    }

    @Override
    public Integer getIntValue() {
        if (isInteger()) {
            return getValue().getIntValue();
        } else {
            throw new InvalidDataTypeException(String.format("Cannot get [%s] value from [%s] expression", Type.INTEGER, getType()));
        }
    }

    @Override
    public Square getSquareValue() {
        if (isSquare()) {
            return getValue().getSquareValue();
        } else {
            throw new InvalidDataTypeException(String.format("Cannot get [%s] value from [%s] expression", Type.INTEGER, getType()));
        }
    }

    @Override
    public Type getType() {
        return operator.getType();
    }

    @Override
    public Value getValue() {
        return operator.apply(left.getValue(), right.getValue());
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public boolean isTerminal() {
        return (left.isLeaf() || left.isPrimitive())
              && (right.isLeaf() || right.isPrimitive());
    }

    @Override
    public boolean isValid() {
        return operator != null
              && left != null && left.isValid()
              && right != null && right.isValid()
              && left.getType() == right.getType()
              && operator.accept(left) && operator.accept(right);
    }

    @Override
    public boolean isBoolean() {
        return operator.isBoolean();
    }

    @Override
    public boolean isInteger() {
        return operator.isInteger();
    }

    @Override
    public boolean isSquare() {
        return operator.isSquare();
    }

    @Override
    public Expression validate() {
        // Vérifier si les deux sous-expressions sont compatibles avec l'opérateur
        if (!(operator.accept(left) && operator.accept(right))) {
            throw new InvalidDataTypeException(String.format("One of the operands [%s,%s] is not acceptable for the operator %s.", left, right, operator.getToken()));
        }
        // Vérifier si les types des deux sous-expressions sont cohérents
        if (left.getType() != right.getType()) {
            throw new InvalidDataTypeException(String.format("The operands do not have the same type [%s,%s]", left.getType(), right.getType()));
        }
        return this;
    }

    @Override
    public ExpressionNode append(final Expression expr, final Operator op) {
        // Au moment d'ajouter l'expression supplémentaire, vérifier si elle change l'ordre de précédence.
        if (operator.hasPrecedence(op)) {
            // Si l'opération courante a une précédence supérieure ou égale, on accumule simplement les résultats.
            return ExpressionNode.builder()
                  .left(this)
                  .right(expr)
                  .operator(op)
                  .build();
        } else {
            // Sinon, il faut effectuer une rotation à droite et continuer le contrôle de précédence récursivement.
            return this.toBuilder()
                  .right(right.append(expr, op))
                  .build();
        }
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public boolean isPrimitive() {
        return left.isPrimitive() && right.isPrimitive();
    }

    @Override
    public boolean isConstant() {
        return left.isPrimitive() && right.isPrimitive();
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {

        if (isPrimitive()) {
            return SpaceRequirement.NONE;
        }
        final SpaceRequirement requirement;
        if ((left.isLeaf() && left.isConstant()) || (right.isLeaf() && right.isConstant())) {
            requirement = SpaceRequirement.merge(left.getSpaceRequirement(), right.getSpaceRequirement());
        } else {
            requirement = SpaceRequirement.combine(left.getSpaceRequirement(), right.getSpaceRequirement());
        }

        if (isTerminal() || !left.getType().equals(getType())) {
            return requirement.add(getType());
        }
        return requirement;
    }

    @Override
    public boolean needsPreprocessing() {
        return !isPrimitive();
    }

    @Override
    public String toString() {
        if (right != null) {
            return left.toString() + " " + operator.getToken() + " " + right.toString();
        }
        return left.toString();
    }
}
