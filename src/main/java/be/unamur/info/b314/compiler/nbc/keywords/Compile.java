package be.unamur.info.b314.compiler.nbc.keywords;

import java.util.Arrays;

/**
 * @author Hadrien BAILLY
 */
public enum Compile implements Reserved, Keyword {

    COMPCHK("compchk"),
    COMPCHKTYPE("compchktype"),
    COMPELSE("compelse"),
    COMPEND("compend"),
    COMPIF("compif"),
    ISCONST("isconst"),
    SIZEOF("sizeof"),
    VALUEOF("valueof");

    private final String token;

    Compile(final String token) {
        this.token = token;
    }

    public static Compile resolve(final String token) {
        return Arrays.stream(values())
              .filter(keyword -> keyword.matches(token))
              .findAny()
              .orElseThrow(() -> new IllegalArgumentException(String.format("Cannot resolve [%s] into any known keyword", token)));
    }

    public boolean matches(final String token) {
        return this.token.equalsIgnoreCase(token);
    }

    public String getToken() {
        return token;
    }
}
