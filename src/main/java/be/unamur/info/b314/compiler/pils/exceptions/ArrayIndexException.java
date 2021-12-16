package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class ArrayIndexException extends ArrayException {

    public ArrayIndexException() {
        super();
    }

    public ArrayIndexException(final String message) {
        super(message);
    }

    public ArrayIndexException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ArrayIndexException(final Throwable cause) {
        super(cause);
    }

    protected ArrayIndexException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
