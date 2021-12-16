package be.unamur.info.b314.compiler.mappers.values;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.Environment;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionMapper;
import be.unamur.info.b314.compiler.mappers.values.booleans.LocationMapper;
import be.unamur.info.b314.compiler.mappers.values.booleans.NegationMapper;
import be.unamur.info.b314.compiler.mappers.values.integers.CountMapper;
import be.unamur.info.b314.compiler.mappers.values.integers.PositionMapper;
import be.unamur.info.b314.compiler.mappers.values.squares.NearbyMapper;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.constants.ConstantNumerical;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionGroup;
import be.unamur.info.b314.compiler.pils.values.booleans.Negation;
import be.unamur.info.b314.compiler.pils.values.references.FunctionCall;
import be.unamur.info.b314.compiler.pils.values.Value;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import be.unamur.info.b314.compiler.pils.values.booleans.Location;
import be.unamur.info.b314.compiler.pils.values.integers.Count;
import be.unamur.info.b314.compiler.pils.values.integers.Life;
import be.unamur.info.b314.compiler.pils.values.integers.Position;
import be.unamur.info.b314.compiler.pils.values.squares.Nearby;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
@Data
public class ValueMapper implements Function<Value, Argument> {

    private final Context context;

    public ValueMapper(final Context context) {
        this.context = context;
    }

    @Override
    public Argument apply(@NonNull final Value value) {

        if (value instanceof VariableReference) {
            final VariableReferenceMapper mapper = context.mappers.getVariableReferenceMapper();
            return mapper.apply((VariableReference) value);
        }

        if (value instanceof FunctionCall) {
            final FunctionCallMapper mapper = context.mappers.getFunctionCallMapper();
            return mapper.apply((FunctionCall) value);
        }

        if (value instanceof ExpressionGroup){
            final ExpressionMapper mapper = context.mappers.getExpressionMapper();
            return mapper.apply((((ExpressionGroup) value).getExpression()));
        }

        if (value instanceof Negation){
            final NegationMapper mapper = context.mappers.getNegationMapper();
            return mapper.apply((((Negation) value)));
        }

        if (value instanceof Location) {
            final LocationMapper mapper = context.mappers.getLocationMapper();
            return mapper.apply((Location) value);
        }

        if (value instanceof Count) {
            final CountMapper mapper = context.mappers.getCountMapper();
            return mapper.apply((Count) value);
        }

        if (value instanceof Position) {
            final PositionMapper mapper = context.mappers.getPositionMapper();
            return mapper.apply((Position) value);
        }

        if (value instanceof Life) {
            return Environment.LifeVariable.LIFE.getIdentifier();
        }

        if (value instanceof Nearby) {
            final NearbyMapper mapper = context.mappers.getNearbyMapper();
            return mapper.apply((Nearby) value);
        }

        if (value.isPrimitive()) {
            log.trace("Converting primitive value [{}] to argument", value);
            // Si oui, déterminer son type.
            final Type type = value.getType();
            // Puis retourner sa valeur telle qu'exprimée en langage NBC.
            switch (type) {
                case INTEGER:
                    return getIntegerValue(value);
                case BOOLEAN:
                    return getBooleanValue(value);
                case SQUARE:
                    return getSquareValue(value);
            }
        }

        throw new UnsupportedOperationException(String.format("Failed to process value [%s] of type [%s]", value, value.getClass()));
    }

    private ConstantNumerical getIntegerValue(final Value value) {
        return ConstantNumerical.builder()
              .value(value.getIntValue())
              .build();
    }

    private ConstantNumerical getBooleanValue(final Value value) {
        return value.getBoolValue()
              ? ConstantNumerical.TRUE()
              : ConstantNumerical.FALSE();
    }

    private ConstantNumerical getSquareValue(final Value value) {
        final int ordinal = value.getSquareValue().ordinal();
        return new ConstantNumerical(ordinal);
    }
}
