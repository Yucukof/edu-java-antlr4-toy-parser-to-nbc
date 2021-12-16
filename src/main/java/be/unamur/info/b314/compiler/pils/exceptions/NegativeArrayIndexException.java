package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class NegativeArrayIndexException extends ArrayIndexException {

    public NegativeArrayIndexException() {
        super();
    }

    public NegativeArrayIndexException(final String message) {
        super(message);
    }

    public NegativeArrayIndexException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NegativeArrayIndexException(final Throwable cause) {
        super(cause);
    }

    protected NegativeArrayIndexException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
