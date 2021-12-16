package be.unamur.info.b314.compiler.nbc.keywords;

import java.util.Arrays;

/**
 * @author Hadrien BAILLY
 */
public enum Scheduling implements Reserved, Keyword {

    ACQUIRE("acquire"),
    CALL("call"),
    EXIT("exit"),
    EXIT_TO("exitto"),
    FOLLOWS("follows"),
    PRECEDES("precedes"),
    PRIORITY("priority"),
    RELEASE("release"),
    RETURN("return"),
    START("start"),
    STOP_THREAD("stopthread"),
    SUB_CALL("subcall"),
    SUB_RET("subret");

    private final String token;

    Scheduling(final String token) {
        this.token = token;
    }

    public static Scheduling resolve(final String token) {
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
