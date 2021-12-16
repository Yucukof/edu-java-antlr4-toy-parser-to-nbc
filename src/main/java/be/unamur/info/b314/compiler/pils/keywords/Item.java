package be.unamur.info.b314.compiler.pils.keywords;

/**
 * @author Hadrien BAILLY
 */
public enum Item implements Reserved {

    MAP("map"),
    RADIO("radio"),
    RADAR("radar"),
    AMMO("ammo"),
    FRUITS("fruits"),
    SODA("soda");

    private final String token;

    Item(final String token) {
        this.token = token;
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
