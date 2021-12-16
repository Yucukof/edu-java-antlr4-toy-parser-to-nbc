package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class InvalidFunctionException extends FunctionException {

    public InvalidFunctionException() {
        super();
    }

    public InvalidFunctionException(final String message) {
        super(message);
    }

    public InvalidFunctionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidFunctionException(final Throwable cause) {
        super(cause);
    }

    protected InvalidFunctionException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
