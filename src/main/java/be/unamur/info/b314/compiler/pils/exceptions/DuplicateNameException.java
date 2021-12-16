package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class DuplicateNameException extends SymbolException {

    public DuplicateNameException() {
        super();
    }

    public DuplicateNameException(final String message) {
        super(message);
    }

    public DuplicateNameException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DuplicateNameException(final Throwable cause) {
        super(cause);
    }

    protected DuplicateNameException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
