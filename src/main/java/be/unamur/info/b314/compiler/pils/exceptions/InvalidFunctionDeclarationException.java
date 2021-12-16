package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class InvalidFunctionDeclarationException extends FunctionException {

    public InvalidFunctionDeclarationException() {
        super();
    }

    public InvalidFunctionDeclarationException(final String message) {
        super(message);
    }

    public InvalidFunctionDeclarationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidFunctionDeclarationException(final Throwable cause) {
        super(cause);
    }

    protected InvalidFunctionDeclarationException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
