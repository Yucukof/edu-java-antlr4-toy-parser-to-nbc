package be.unamur.info.b314.compiler.nbc.keywords;

import java.util.Arrays;

/**
 * @author Hadrien BAILLY
 */
public enum Array implements Reserved, Keyword {

    INDEX("index"),
    REPLACE("replace"),
    ARR_SIZE("arrsize"),
    ARR_INIT("arrinit"),
    ARR_SUBSET("arrsubset"),
    ARR_BUILD("arrbuild"),
    ARR_OP("arrop");

    private final String token;

    Array(final String token) {
        this.token = token;
    }

    public static Array resolve(final String token) {
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
