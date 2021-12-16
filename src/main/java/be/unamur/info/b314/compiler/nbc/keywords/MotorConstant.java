package be.unamur.info.b314.compiler.nbc.keywords;

import java.util.Arrays;

/**
 * @author Anthony DI STASIO
 */
public enum MotorConstant {
    OUT_A("OUT_A"),
    OUT_B("OUT_B"),
    OUT_C("OUT_C"),
    OUT_AB("OUT_AB"),
    OUT_AC("OUT_AC"),
    OUT_BC("OUT_BC"),
    OUT_ABC("OUT_ABC");

    private final String token;

    MotorConstant(final String token) {
        this.token = token;
    }

    public static MotorConstant resolve(final String token) {
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