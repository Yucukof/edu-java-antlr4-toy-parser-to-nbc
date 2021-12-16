package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class ExpressionException extends GrammarException {

    public ExpressionException() {
        super();
    }

    public ExpressionException(final String message) {
        super(message);
    }

    public ExpressionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ExpressionException(final Throwable cause) {
        super(cause);
    }

    protected ExpressionException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
