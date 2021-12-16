package be.unamur.info.b314.compiler.nbc.keywords;

import java.util.Arrays;

/**
 * @author Hadrien BAILLY
 */
public enum Bit implements Reserved, Keyword {

    SHR("shr"),
    SHL("shl"),
    ASR("asr"),
    ASL("asl"),
    LSR("lsr"),
    LSL("lsl"),
    ROTR("rotr"),
    ROTL("rotl"),
    CMNT("cmnt");

    private final String token;

    Bit(final String token) {
        this.token = token;
    }

    public static Bit resolve(final String token) {
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
