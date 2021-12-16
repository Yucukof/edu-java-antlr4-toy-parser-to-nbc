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
public class PrimitiveInteger implements Value {

    public static final PrimitiveInteger ZERO = PrimitiveInteger.builder().value(0).build();
    public static final PrimitiveInteger ONE = PrimitiveInteger.builder().value(1).build();

    private Integer value;

    @Override
    public Type getType() {
        return Type.INTEGER;
    }

    @Override
    public Integer getIntValue() {
        return value;
    }

    @Override
    public boolean isInteger() {
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
