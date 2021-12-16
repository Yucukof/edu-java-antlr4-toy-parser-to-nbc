package be.unamur.info.b314.compiler.pils.values.booleans;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.values.Value;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Hadrien BAILLY
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class Negation implements Value {

    /**
     * L'expression à nier.
     */
    private final Expression expression;
    /**
     * Une valeur mémorisée.
     */
    private boolean cachedValue;

    @Override
    public String toString() {
        return "not (" + expression.toString() + ")";
    }

    @Override
    public Boolean getBoolValue() {
        return !expression.getValue().getBoolValue();
    }

    @Override
    public Type getType() {
        return Type.BOOLEAN;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    public boolean isValid() {
        return expression != null && expression.isValid() && expression.isBoolean();
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
        if (expression.isPrimitive()) {
            return SpaceRequirement.NONE;
        }
        final SpaceRequirement negationRequirement = new SpaceRequirement(0, 1, 0);
        return SpaceRequirement.merge(expression.getSpaceRequirement(), negationRequirement);
    }
}
