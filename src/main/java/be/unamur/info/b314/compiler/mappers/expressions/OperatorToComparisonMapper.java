package be.unamur.info.b314.compiler.mappers.expressions;

import be.unamur.info.b314.compiler.nbc.symbols.Comparator;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import lombok.NonNull;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 */
public class OperatorToComparisonMapper implements Function<Operator, Comparator> {

    @Override
    public Comparator apply(@NonNull final Operator operator) {
        if (operator.getType() == Type.BOOLEAN) {
            switch (operator) {
                case AND:
                case EQ:
                    return Comparator.EQ;
                case GRT:
                    return Comparator.GT;
                case LSS:
                    return Comparator.LT;
                default:
                    throw new IllegalArgumentException(String.format("Cannot map operator [%s] into a NBC comparator.", operator));
            }
        }
        throw new IllegalArgumentException(String.format("Cannot map %s operator [%s] into a NBC comparator", operator.getType(), operator));
    }
}
