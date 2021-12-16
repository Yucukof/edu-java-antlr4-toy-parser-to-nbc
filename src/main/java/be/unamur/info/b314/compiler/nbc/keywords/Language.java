package be.unamur.info.b314.compiler.nbc.keywords;

import java.util.Arrays;

/**
 * List of all reserved keywords in NBC Programming Language.
 *
 * @author Hadrien BAILLY
 */
public enum Language implements Reserved, Keyword {

    DEFINE("define"),
    DOWNLOAD("download"),
    DSEG("dseg"),
    ENDS("ends"),
    ENDT("endt"),
    IMPORT("import"),
    INCLUDE("include"),
    SEGMENT("segment"),
    STRUCT("struct"),
    SUBROUTINE("subroutine"),
    THREAD("thread"),
    TYPEDEF("typedef"),
    VOID("void");



    private final String token;

    Language(final String token) {
        this.token = token;
    }

    public static Language resolve(final String token) {
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
