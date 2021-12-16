package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class InvalidVariableReferenceException extends VariableException {

    public InvalidVariableReferenceException() {
        super();
    }

    public InvalidVariableReferenceException(final String message) {
        super(message);
    }

    public InvalidVariableReferenceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidVariableReferenceException(final Throwable cause) {
        super(cause);
    }

    protected InvalidVariableReferenceException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
