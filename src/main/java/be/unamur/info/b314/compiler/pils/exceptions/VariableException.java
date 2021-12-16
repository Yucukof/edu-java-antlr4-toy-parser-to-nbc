package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class VariableException extends SymbolException {

    public VariableException() {
        super();
    }

    public VariableException(final String message) {
        super(message);
    }

    public VariableException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public VariableException(final Throwable cause) {
        super(cause);
    }

    protected VariableException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
