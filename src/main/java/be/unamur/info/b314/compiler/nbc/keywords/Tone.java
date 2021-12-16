package be.unamur.info.b314.compiler.nbc.keywords;

import java.util.Arrays;

/**
 * @author Anthony DI STASIO
 */
public enum Tone {
    TONE_C3("TONE_C3"),
    TONE_D3("TONE_D3"),
    TONE_E3("TONE_E3"),
    TONE_F3("TONE_F3"),
    TONE_A3("TONE_A3"),
    TONE_B3("TONE_B3"),
    TONE_C4("TONE_C4"),
    TONE_D4("TONE_D4"),
    TONE_E4("TONE_E4"),
    TONE_F4("TONE_F4"),
    TONE_G4("TONE_G4"),
    TONE_A4("TONE_A4"),
    TONE_B4("TONE_B4"),
    TONE_C5("TONE_C5"),
    TONE_D5("TONE_D5"),
    TONE_E5("TONE_E5"),
    TONE_F5("TONE_F5"),
    TONE_G5("TONE_G5"),
    TONE_A5("TONE_A5"),
    TONE_B5("TONE_B5"),
    TONE_C6("TONE_C6"),
    TONE_D6("TONE_D6"),
    TONE_E6("TONE_E6"),
    TONE_F6("TONE_F6"),
    TONE_G6("TONE_G6"),
    TONE_A6("TONE_A6"),
    TONE_B6("TONE_B6"),
    TONE_C7("TONE_C7"),
    TONE_D7("TONE_D7"),
    TONE_E7("TONE_E7"),
    TONE_F7("TONE_F7"),
    TONE_G7("TONE_G7"),
    TONE_A7("TONE_A7"),
    TONE_B7("TONE_B7");

    private final String token;

    Tone(final String token) {
        this.token = token;
    }

    public static Tone resolve(final String token) {
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