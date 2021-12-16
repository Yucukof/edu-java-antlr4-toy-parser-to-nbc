package be.unamur.info.b314.compiler.pils.keywords;

import java.util.Arrays;

/**
 * @author Hadrien BAILLY
 */
public enum PositionParameter implements Reserved {

    LATITUDE("latitude"),
    LONGITUDE("longitude"),
    GRID_SIZE("grid size");

    private final String token;

    PositionParameter(final String token) {
        this.token = token;
    }

    public static PositionParameter resolve(final String token) {
        return Arrays.stream(values())
              .filter(parameter -> parameter.matches(token))
              .findAny()
              .orElseThrow(() -> new IllegalArgumentException(String.format("Cannot resolve [%s] into any know position parameter.", token)));
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public boolean matches(final String token) {
        return getToken().equalsIgnoreCase(token);
    }

}
