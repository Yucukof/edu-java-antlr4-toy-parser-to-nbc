package be.unamur.info.b314.compiler.pils.keywords;

import java.util.Arrays;

/**
 * @author Hadrien BAILLY
 */
public enum Target implements Reserved {

    ENEMY("ENNEMI", Square.ENEMY),
    GRAAL("GRAAL", Square.GRAAL);

    private final String token;
    private final Square square;

    Target(final String token, final Square square) {
        this.token = token;
        this.square = square;
    }

    public static Target resolve(final String token) {
        return Arrays.stream(values())
              .filter(target -> target.matches(token))
              .findAny()
              .orElseThrow(() -> new IllegalArgumentException(String.format("Cannot resolve [%s] into any supported target", token)));
    }

    @Override
    public boolean matches(final String token) {
        return this.token.equalsIgnoreCase(token);
    }

    @Override
    public String getToken() {
        return token;
    }

    public Square getSquare() {
        return square;
    }
}
