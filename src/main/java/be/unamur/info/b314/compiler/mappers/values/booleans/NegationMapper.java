package be.unamur.info.b314.compiler.mappers.values.booleans;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.values.ValueMapper;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.values.Value;
import be.unamur.info.b314.compiler.pils.values.booleans.Negation;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveBoolean;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 * @see NegationPreprocessor
 */
@Slf4j
public class NegationMapper implements Function<Negation, Argument> {

    private final Context context;

    public NegationMapper(final Context context) {
        this.context = context;
    }

    @Override
    public Argument apply(final Negation negation) {

        log.trace("Processing Negation [{}]", negation);
        final Expression expression = negation.getExpression();
        if (expression.isPrimitive()) {
            log.trace("Using constant expression [{}]", expression);
            final ValueMapper mapper = context.mappers.getValueMapper();
            final boolean bool = expression.getValue().getBoolValue();
            final Value value = new PrimitiveBoolean(!bool);
            log.trace("Value: [{}]", value);
            return mapper.apply(value);
        } else {
            final Identifier name = context.memory.getDisposable(expression.getType());
            log.trace("Using preprocessed results in [{}]", name);
            return name;
        }

    }
}
