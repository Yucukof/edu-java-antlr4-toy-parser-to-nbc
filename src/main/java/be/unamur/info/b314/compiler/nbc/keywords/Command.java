package be.unamur.info.b314.compiler.nbc.keywords;

/**
 * @author Hadrien BAILLY
 */
public enum Command implements Keyword {
    ON_FWD("OnFwd"),
    ON_REV("OnRev"),
    ROTATE("RotateMotor"),
    PLAY_TONE("PlayTone"),
    TEXT_OUT("TextOut"),
    OFF("Off");

    private final String token;

    Command(final String token) {
        this.token = token;
    }

    @Override
    public String getToken() {
        return token;
    }
}