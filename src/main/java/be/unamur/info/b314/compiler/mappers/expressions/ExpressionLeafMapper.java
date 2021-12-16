package be.unamur.info.b314.compiler.mappers.expressions;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.primitives.PrimitiveBooleanMapper;
import be.unamur.info.b314.compiler.mappers.primitives.PrimitiveIntegerMapper;
import be.unamur.info.b314.compiler.mappers.primitives.PrimitiveSquareMapper;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveBoolean;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveInteger;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveSquare;
import lombok.NonNull;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 */
public class ExpressionLeafMapper implements Function<ExpressionLeaf, Argument> {

    private final Context context;

    public ExpressionLeafMapper(@NonNull final Context context) {
        this.context = context;
    }

    @Override
    public Argument apply(@NonNull final ExpressionLeaf expressionLeaf) {

        final PrimitiveBooleanMapper primitiveBooleanMapper = context.mappers.getPrimitiveBooleanMapper();
        final PrimitiveIntegerMapper primitiveIntegerMapper = context.mappers.getPrimitiveIntegerMapper();
        final PrimitiveSquareMapper primitiveSquareMapper = context.mappers.getPrimitiveSquareMapper();

        if (expressionLeaf.isBoolean()) {
            return primitiveBooleanMapper.apply((PrimitiveBoolean) (expressionLeaf.getValue()));
        }

        if (expressionLeaf.isInteger()) {
            return primitiveIntegerMapper.apply((PrimitiveInteger) (expressionLeaf.getValue()));
        }

        if (expressionLeaf.isSquare()) {
            return primitiveSquareMapper.apply((PrimitiveSquare) (expressionLeaf.getValue()));
        }

        throw new UnsupportedOperationException(String.valueOf(expressionLeaf.getClass()));
    }
}
