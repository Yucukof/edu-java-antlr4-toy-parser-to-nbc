package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class IndexOutOfBoundsException extends ArrayIndexException {

    public IndexOutOfBoundsException() {
        super();
    }

    public IndexOutOfBoundsException(final String message) {
        super(message);
    }

    public IndexOutOfBoundsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public IndexOutOfBoundsException(final Throwable cause) {
        super(cause);
    }

    protected IndexOutOfBoundsException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
