package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class InvalidDataTypeException extends GrammarException {

    public InvalidDataTypeException() {
    }

    public InvalidDataTypeException(final String message) {
        super(message);
    }

    public InvalidDataTypeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidDataTypeException(final Throwable cause) {
        super(cause);
    }

    public InvalidDataTypeException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
