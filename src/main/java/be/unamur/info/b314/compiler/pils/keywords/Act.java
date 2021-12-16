package be.unamur.info.b314.compiler.pils.keywords;

/**
 * @author Hadrien BAILLY
 */
public enum Act implements Reserved {

    MOVE("move"),
    SHOOT("shoot"),
    USE("use");

    private final String token;

    Act(final String token) {
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
