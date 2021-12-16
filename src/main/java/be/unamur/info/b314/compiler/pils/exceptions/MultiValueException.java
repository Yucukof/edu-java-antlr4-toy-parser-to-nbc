package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class MultiValueException extends GrammarException {

    public MultiValueException() {
        super();
    }

    public MultiValueException(final String message) {
        super(message);
    }

    public MultiValueException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MultiValueException(final Throwable cause) {
        super(cause);
    }

    protected MultiValueException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
