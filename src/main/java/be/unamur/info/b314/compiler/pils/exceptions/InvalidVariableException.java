package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class InvalidVariableException extends VariableException {

    public InvalidVariableException() {
        super();
    }

    public InvalidVariableException(final String message) {
        super(message);
    }

    public InvalidVariableException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidVariableException(final Throwable cause) {
        super(cause);
    }

    protected InvalidVariableException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
