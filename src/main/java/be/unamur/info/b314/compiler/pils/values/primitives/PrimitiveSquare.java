package be.unamur.info.b314.compiler.pils.values.primitives;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.pils.keywords.Square;
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
public class PrimitiveSquare implements Value {

    public static final PrimitiveSquare DIRT = PrimitiveSquare.builder().value(Square.DIRT).build();
    public static final PrimitiveSquare ROCK = PrimitiveSquare.builder().value(Square.ROCK).build();
    /**
     * La valeur directe.
     */
    private Square value;

    @Override
    public String toString() {
        return value.getToken();
    }

    @Override
    public Type getType() {
        return Type.SQUARE;
    }

    @Override
    public Square getSquareValue() {
        return value;
    }

    @Override
    public boolean isSquare() {
        return true;
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
    public boolean isValid() {
        return value != null;
    }

}
