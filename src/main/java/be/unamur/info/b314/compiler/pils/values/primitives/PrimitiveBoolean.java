package be.unamur.info.b314.compiler.pils.values.primitives;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.pils.values.Value;
import lombok.*;

/**
 * @author Hadrien BAILLY
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PrimitiveBoolean implements Value {

    public static final PrimitiveBoolean TRUE = PrimitiveBoolean.builder().value(true).build();
    public static final PrimitiveBoolean FALSE = PrimitiveBoolean.builder().value(false).build();
    /**
     * La valeur booléenne.
     */
    private Boolean value;

    @Override
    public Boolean getBoolValue() {
        return value;
    }

    @Override
    public Type getType() {
        return Type.BOOLEAN;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public boolean isValid() {
        return value != null;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        return SpaceRequirement.NONE;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
