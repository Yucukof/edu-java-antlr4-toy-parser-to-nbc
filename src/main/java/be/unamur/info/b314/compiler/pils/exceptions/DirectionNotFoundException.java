package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Anthony DI STASIO
 */
public class DirectionNotFoundException extends GrammarException {
    public DirectionNotFoundException() {
        super();
    }

    public DirectionNotFoundException(final String message) {
        super(message);
    }

    public DirectionNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DirectionNotFoundException(final Throwable cause) {
        super(cause);
    }

    protected DirectionNotFoundException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}