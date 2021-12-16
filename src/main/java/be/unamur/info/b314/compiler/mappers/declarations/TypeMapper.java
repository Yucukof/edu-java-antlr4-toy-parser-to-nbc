package be.unamur.info.b314.compiler.mappers.declarations;

import be.unamur.info.b314.compiler.nbc.keywords.Type;
import lombok.NonNull;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 */
public class TypeMapper implements Function<be.unamur.info.b314.compiler.pils.declarations.Type, Type.SuperType> {

    @Override
    public Type.SuperType apply(@NonNull final be.unamur.info.b314.compiler.pils.declarations.Type type) {
        switch (type) {
            case BOOLEAN:
                return Type.SuperType.BOOL;
            case INTEGER:
                return Type.SuperType.INT;
            case SQUARE:
                return Type.SuperType.SQUARE;
            default:
                throw new UnsupportedOperationException("Cannot find the super type");
        }
    }
}
