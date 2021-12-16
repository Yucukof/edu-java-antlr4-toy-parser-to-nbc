package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class InvalidFunctionCallException extends FunctionException {

    public InvalidFunctionCallException() {
        super();
    }

    public InvalidFunctionCallException(final String message) {
        super(message);
    }

    public InvalidFunctionCallException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidFunctionCallException(final Throwable cause) {
        super(cause);
    }

    protected InvalidFunctionCallException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
