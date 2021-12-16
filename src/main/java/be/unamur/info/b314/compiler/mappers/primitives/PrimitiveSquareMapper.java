package be.unamur.info.b314.compiler.mappers.primitives;

import be.unamur.info.b314.compiler.nbc.constants.ConstantString;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveSquare;
import lombok.NonNull;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 */
public class PrimitiveSquareMapper implements Function<PrimitiveSquare, ConstantString> {

    @Override
    public ConstantString apply(@NonNull final PrimitiveSquare primitiveBoolean) {
        return new ConstantString(primitiveBoolean.getSquareValue().name());
    }
}
