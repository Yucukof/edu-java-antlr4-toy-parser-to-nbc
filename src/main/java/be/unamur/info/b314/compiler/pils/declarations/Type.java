package be.unamur.info.b314.compiler.pils.declarations;


import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.keywords.Reserved;
import be.unamur.info.b314.compiler.pils.values.Value;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveBoolean;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveInteger;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveSquare;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveVoid;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * @author Hadrien BAILLY
 */
public enum Type implements Reserved {

    //@formatter:off
    INTEGER ("integer", PrimitiveInteger::new, ExpressionLeaf.ZERO),
    BOOLEAN ("boolean", PrimitiveBoolean::new, ExpressionLeaf.FALSE),
    SQUARE  ("square",  PrimitiveSquare::new , ExpressionLeaf.DIRT),
    VOID    ("void",    PrimitiveVoid::new   , ExpressionLeaf.VOID);
    //@formatter:on

    private final String token;
    private final Supplier<Value> mapper;
    private final Expression expression;

    Type(final String token, final Supplier<Value> mapper, final Expression expression) {
        this.mapper = mapper;
        this.token = token;
        this.expression = expression;
    }

    public static Type resolve(final String value) {
        return Arrays.stream(values())
              .filter(type -> type.name().equalsIgnoreCase(value))
              .findAny()
              .orElseThrow(() -> new IllegalArgumentException("Cannot parse [" + value + "] into any known types."));
    }

    public Value get() {
        return mapper.get();
    }

    public Expression getDefault() {
        return expression;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public boolean matches(final String token) {
        return getToken().equalsIgnoreCase(token);
    }

}
