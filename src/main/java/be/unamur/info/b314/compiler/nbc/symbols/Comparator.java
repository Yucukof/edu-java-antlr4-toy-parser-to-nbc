package be.unamur.info.b314.compiler.nbc.symbols;

import java.util.Arrays;

/**
 * @author Hadrien BAILLY
 */
public enum Comparator {
    LT,
    GT,
    LTEQ,
    GTEQ,
    EQ,
    NEQ;

    public static Comparator resolve(final String token) {
        return Arrays.stream(values())
              .filter(keyword -> keyword.matches(token))
              .findAny()
              .orElseThrow(() -> new IllegalArgumentException(String.format("Cannot resolve [%s] into any known keyword", token)));
    }

    public boolean matches(final String token) {
        return this.name().equalsIgnoreCase(token);
    }

    public String getToken() {
        return this.name();
    }
}
