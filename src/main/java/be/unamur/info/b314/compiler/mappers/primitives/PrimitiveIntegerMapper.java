package be.unamur.info.b314.compiler.mappers.primitives;

import be.unamur.info.b314.compiler.nbc.constants.ConstantNumerical;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveInteger;
import lombok.NonNull;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 */
public class PrimitiveIntegerMapper implements Function<PrimitiveInteger, ConstantNumerical> {

    @Override
    public ConstantNumerical apply(@NonNull final PrimitiveInteger primitiveInteger) {
        return ConstantNumerical.builder()
              .value(primitiveInteger.getIntValue())
              .build();
    }
}
