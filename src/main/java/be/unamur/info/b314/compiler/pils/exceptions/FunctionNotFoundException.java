package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Benoît DUVIVIER
 */
public class FunctionNotFoundException extends FunctionException {

    public FunctionNotFoundException() {
        super();
    }

    public FunctionNotFoundException(final String message) {
        super(message);
    }

    public FunctionNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FunctionNotFoundException(final Throwable cause) {
        super(cause);
    }

    protected FunctionNotFoundException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
