package be.unamur.info.b314.compiler.nbc.keywords;

import java.util.Arrays;

/**
 * @author Hadrien BAILLY
 */
public enum ControlFlow implements Reserved, Keyword {

    JMP("jmp"),
    BRCMP("brcmp"),
    BRTST("brtst"),
    STOP("stop"),
    SYSCALL("syscall");

    private final String token;

    ControlFlow(final String token) {
        this.token = token;
    }

    public static ControlFlow resolve(final String token) {
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
