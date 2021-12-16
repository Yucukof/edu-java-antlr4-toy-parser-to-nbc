package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class InvalidNumberOfIndexesException extends ArrayException {

    public InvalidNumberOfIndexesException() {
        super();
    }

    public InvalidNumberOfIndexesException(final String message) {
        super(message);
    }

    public InvalidNumberOfIndexesException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidNumberOfIndexesException(final Throwable cause) {
        super(cause);
    }

    protected InvalidNumberOfIndexesException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
