package be.unamur.info.b314.compiler.mappers.values.squares;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.Environment;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionMapper;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionPreprocessor;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.constants.ConstantNumerical;
import be.unamur.info.b314.compiler.nbc.symbols.IndexedIdentifier;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.values.squares.Nearby;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 * @see NearbyPreprocessor
 */
@Slf4j
public class NearbyMapper implements Function<Nearby, Argument> {

    private final Context context;

    public NearbyMapper(final Context context) {
        this.context = context;
    }

    @Override
    public Argument apply(final Nearby nearby) {

        log.trace("Processing Nearby [{}]", nearby);

        final Expression leftCoordinate = nearby.getCoordinates().getLeft();
        final Argument left = process(leftCoordinate);
        if (!leftCoordinate.isConstant()) {
            context.memory.reserveAndGet(leftCoordinate.getType());
        }

        final Expression rightCoordinate = nearby.getCoordinates().getRight();
        final Argument right = process(rightCoordinate);

        if (!leftCoordinate.isConstant()) {
            context.memory.release(leftCoordinate.getType());
        }

        return IndexedIdentifier.builder()
              .name(Environment.NearbyVariable.NEARBY.getName())
              .index(left)
              .index(right)
              .build();
    }

    private Argument process(final Expression expression) {
        if (expression.isPrimitive()) {
            final int value = expression.getValue().getIntValue() - 1;
            return new ConstantNumerical(value);
        }
        final ExpressionMapper mapper = context.mappers.getExpressionMapper();
        return mapper.apply(expression);
    }
}
