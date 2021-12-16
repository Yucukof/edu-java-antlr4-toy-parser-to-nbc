package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Anthony DI STASIO
 */
public class ItemNotFoundException extends GrammarException {
    public ItemNotFoundException() {
        super();
    }

    public ItemNotFoundException(final String message) {
        super(message);
    }

    public ItemNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ItemNotFoundException(final Throwable cause) {
        super(cause);
    }

    protected ItemNotFoundException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}