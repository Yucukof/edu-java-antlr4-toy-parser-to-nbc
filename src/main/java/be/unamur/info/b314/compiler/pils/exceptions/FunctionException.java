package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class FunctionException extends SymbolException {

    public FunctionException() {
        super();
    }

    public FunctionException(final String message) {
        super(message);
    }

    public FunctionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FunctionException(final Throwable cause) {
        super(cause);
    }

    protected FunctionException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
