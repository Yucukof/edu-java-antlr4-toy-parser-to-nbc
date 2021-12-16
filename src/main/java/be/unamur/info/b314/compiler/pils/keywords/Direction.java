package be.unamur.info.b314.compiler.pils.keywords;

/**
 * @author Hadrien BAILLY
 */
public enum Direction implements Reserved {

    NORTH("north", true),
    SOUTH("south", true),
    EAST("east", false),
    WEST("west", false);

    private final String token;
    private final boolean longitudinal;

    Direction(final String token, final boolean longitudinal) {
        this.token = token;
        this.longitudinal = longitudinal;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public boolean matches(final String token) {
        return getToken().equalsIgnoreCase(token);
    }

    /**
     * Indique si le mot-clé concerne la longitude.
     */
    public boolean isLongitudinal() {
        return longitudinal;
    }

    /**
     * Indique si le mot-clé concerne la latitude.
     */
    public boolean isLatitudinal() {
        return !longitudinal;
    }
}
