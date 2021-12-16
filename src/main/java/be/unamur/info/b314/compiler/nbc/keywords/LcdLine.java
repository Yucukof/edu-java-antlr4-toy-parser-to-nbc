package be.unamur.info.b314.compiler.nbc.keywords;

import java.util.Arrays;

/**
 * @author Anthony DI STASIO
 */
public enum LcdLine {
    // LCD_LINE1 represents the 1st line of the LCD screen, LCD_LINE2 the 2nd one, etc.
    LCD_LINE1("56"),
    LCD_LINE2("48"),
    LCD_LINE3("40"),
    LCD_LINE4("32"),
    LCD_LINE5("24"),
    LCD_LINE6("16"),
    LCD_LINE7("8"),
    LCD_LINE8("0");

    private final String token;

    LcdLine(final String token) {
        this.token = token;
    }

    public static LcdLine resolve(final String token) {
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