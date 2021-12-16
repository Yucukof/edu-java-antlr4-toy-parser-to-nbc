package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class InvalidExpressionException extends ExpressionException {

    public InvalidExpressionException() {
        super();
    }

    public InvalidExpressionException(final String message) {
        super(message);
    }

    public InvalidExpressionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidExpressionException(final Throwable cause) {
        super(cause);
    }

    protected InvalidExpressionException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
