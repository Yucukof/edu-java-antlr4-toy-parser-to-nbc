package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class InvalidOperationException extends ExpressionException {

    public InvalidOperationException() {
        super();
    }

    public InvalidOperationException(final String message) {
        super(message);
    }

    public InvalidOperationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidOperationException(final Throwable cause) {
        super(cause);
    }

    protected InvalidOperationException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
