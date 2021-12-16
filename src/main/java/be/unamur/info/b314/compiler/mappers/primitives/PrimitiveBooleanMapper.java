package be.unamur.info.b314.compiler.mappers.primitives;

import be.unamur.info.b314.compiler.nbc.constants.ConstantNumerical;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveBoolean;
import lombok.NonNull;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 */
public class PrimitiveBooleanMapper implements Function<PrimitiveBoolean, ConstantNumerical> {

    @Override
    public ConstantNumerical apply(@NonNull final PrimitiveBoolean primitiveBoolean) {
        return primitiveBoolean.getBoolValue()
              ? ConstantNumerical.TRUE()
              : ConstantNumerical.FALSE();
    }
}
