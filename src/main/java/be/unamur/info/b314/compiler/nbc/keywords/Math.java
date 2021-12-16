package be.unamur.info.b314.compiler.nbc.keywords;

import java.util.Arrays;

/**
 * List of all reserved keywords in NBC Programming Language.
 *
 * @author Hadrien BAILLY
 */
public enum Math implements Reserved, Keyword {
    ABS("abs"),
    ACOS("acos"),
    ACOSD("acosd"),
    ADD("add"),
    ASIN("asin"),
    ASIND("asind"),
    ATAN("atan"),
    ATAN2("atan2"),
    ATAN2D("atan2d"),
    ATAND("atand"),
    COS("cos"),
    COSD("cosd"),
    COSH("cosh"),
    COSHD("coshd"),
    DIV("div"),
    EXP("exp"),
    FLOOR("floor"),
    FRAC("frac"),
    LOG("log"),
    LOG10("log10"),
    MOD("mod"),
    MUL("mul"),
    NEG("neg"),
    POW("pow"),
    SIGN("sign"),
    SIN("sin"),
    SIND("sind"),
    SINH("sinh"),
    SINHD("sinhd"),
    SQRT("sqrt"),
    SUB("sub"),
    TAN("tan"),
    TAND("tand"),
    TANH("tanh"),
    TANHD("tanhd"),
    CEIL("ceil"),
    MULDIV("muldiv"),
    TRUNC("trunc");

    private final String token;

    Math(final String token) {
        this.token = token;
    }

    public static Math resolve(final String token) {
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