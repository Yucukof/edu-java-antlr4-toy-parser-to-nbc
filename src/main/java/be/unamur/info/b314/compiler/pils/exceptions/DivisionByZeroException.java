package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class DivisionByZeroException extends ExpressionException {

    public DivisionByZeroException() {
        super();
    }

    public DivisionByZeroException(final String message) {
        super(message);
    }

    public DivisionByZeroException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DivisionByZeroException(final Throwable cause) {
        super(cause);
    }

    protected DivisionByZeroException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
