package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class ReservedNameException extends SymbolException {

    public ReservedNameException() {
        super();
    }

    public ReservedNameException(final String message) {
        super(message);
    }

    public ReservedNameException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ReservedNameException(final Throwable cause) {
        super(cause);
    }

    protected ReservedNameException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
