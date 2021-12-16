package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Anthony DI STASIO
 */
public class ActionNotFoundException extends GrammarException {
    public ActionNotFoundException() {
        super();
    }

    public ActionNotFoundException(final String message) {
        super(message);
    }

    public ActionNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ActionNotFoundException(final Throwable cause) {
        super(cause);
    }

    protected ActionNotFoundException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}