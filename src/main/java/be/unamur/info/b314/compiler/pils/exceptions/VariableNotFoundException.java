package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class VariableNotFoundException extends VariableException {

    public VariableNotFoundException() {
        super();
    }

    public VariableNotFoundException(final String message) {
        super(message);
    }

    public VariableNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public VariableNotFoundException(final Throwable cause) {
        super(cause);
    }

    protected VariableNotFoundException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
