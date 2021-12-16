package be.unamur.info.b314.compiler.mappers.expressions;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.values.ValueMapper;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 * @see ExpressionPreprocessor
 */
@Slf4j
public class ExpressionMapper implements Function<Expression, Argument> {

    private final Context context;

    public ExpressionMapper(final Context context) {
        this.context = context;
    }

    @Override
    public Argument apply(final Expression expression) {

        log.trace("Processing Expression [{}]", expression);

        if (expression.isLeaf() || expression.isPrimitive()) {
            final ValueMapper valueMapper = context.mappers.getValueMapper();
            return valueMapper.apply(expression.getValue());
        }
        return context.memory.getDisposable(expression.getType());
    }
}
