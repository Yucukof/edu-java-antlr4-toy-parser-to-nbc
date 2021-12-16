package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class GrammarException extends RuntimeException {
    public GrammarException() {
        super();
    }

    public GrammarException(final String message) {
        super(message);
    }

    public GrammarException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public GrammarException(final Throwable cause) {
        super(cause);
    }

    protected GrammarException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
