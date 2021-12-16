package be.unamur.info.b314.compiler.nbc.keywords;

import java.util.Arrays;

/**
 * @author Hadrien BAILLY
 */
public enum Text implements Reserved, Keyword {

    FLATTEN("flatten"),
    UNFLATTEN("unflatten"),
    NUMTOSTR("numtostr"),
    FMTNUM("fmtnum"),
    STRTONUM("strtonum"),
    STRSUBSET("strsubset"),
    STRCAT("strcat"),
    ARRTOSTR("arrtostr"),
    STRTOARR("strtoarr"),
    STRINDEX("strindex"),
    STRREPLACE("strreplace"),
    STRLEN("strlen");

    private final String token;

    Text(final String token) {
        this.token = token;
    }

    public static Text resolve(final String token) {
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
