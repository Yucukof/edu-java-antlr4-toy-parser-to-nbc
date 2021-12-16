package be.unamur.info.b314.compiler.nbc.symbols;

import java.util.Arrays;

/**
 * @author Hadrien BAILLY
 */
public enum Operator {

    ADDITION("+", "addition"),
    SUBTRACTION("-", "subtraction"),
    MULTIPLICATION("*", "multiplication"),
    DIVISION("/", "division"),
    EXPONENT("^", "exponent"),
    MODULO("%", "modulo"),
    AND("&", "bitwise and"),
    OR("|", "bitwise or"),
    XOR("~", "bitwise xor"),
    LEFT("<<", "shift left"),
    RIGHT(">>", "shift right");

    private final String token;
    private final String description;

    Operator(final String token, final String description) {
        this.token = token;
        this.description = description;
    }

    public static Operator resolve(final String token) {
        return Arrays.stream(values())
              .filter(type -> type.matches(token))
              .findAny()
              .orElseThrow(() -> new IllegalArgumentException("Cannot resolve token [" + token + "] into any known type."));
    }

    public boolean matches(final String token) {
        return this.token.equalsIgnoreCase(token);
    }

    public String getToken() {
        return token;
    }

    public String getDescription() {
        return description;
    }
}
