package be.unamur.info.b314.compiler.nbc.keywords;

import java.util.Arrays;

/**
 * @author Hadrien BAILLY
 */
public enum Wait implements Reserved, Keyword {

    WAIT("wait"),
    WAITV("waitv"),
    WAIT2("wait2"),
    GETTICK("gettick");

    private final String token;

    Wait(final String token) {
        this.token = token;
    }

    public static Wait resolve(final String token) {
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
