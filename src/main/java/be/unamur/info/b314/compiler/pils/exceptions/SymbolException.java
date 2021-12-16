package be.unamur.info.b314.compiler.pils.exceptions;

/**
 * @author Hadrien BAILLY
 */
public class SymbolException extends GrammarException {

    public SymbolException() {
        super();
    }

    public SymbolException(final String message) {
        super(message);
    }

    public SymbolException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SymbolException(final Throwable cause) {
        super(cause);
    }

    protected SymbolException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
