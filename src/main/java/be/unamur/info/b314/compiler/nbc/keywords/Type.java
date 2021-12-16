package be.unamur.info.b314.compiler.nbc.keywords;

import be.unamur.info.b314.compiler.nbc.symbols.Identifier;

import java.util.Arrays;

/**
 * @author Hadrien BAILLY
 */
public enum Type implements Reserved, Keyword {

    BYTE("byte", 8, false),
    DB("db", 8, false),
    DD("dd", 32, false),
    DW("dw", 16, false),
    DWORD("dword", 32, false),
    FLOAT("float", 32, true),
    LONG("long", 32, false),
    MUTEX("mutex", 32, false),
    SBYTE("sbyte", 8, true),
    SDWORD("sdword", 32, true),
    SLONG("slong", 32, true),
    SWORD("sword", 16, true),
    UBYTE("ubyte", 8, false),
    UDWORD("udword", 32, false),
    ULONG("ulong", 32, false),
    UWORD("uword", 16, false),
    WORD("word", 16, false);

    private final String token;
    private final int length;
    private final boolean signed;

    Type(final String token, final int length, final boolean signed) {
        this.token = token;
        this.length = length;
        this.signed = signed;
    }

    public static Type resolve(final String token) {
        return Arrays.stream(values())
              .filter(type -> type.matches(token))
              .findAny()
              .orElseThrow(() -> new IllegalArgumentException("Cannot resolve token [" + token + "] into any known type."));
    }

    public boolean matches(final String token) {
        return this.token.equalsIgnoreCase(token);
    }

    public String getToken() {
        return token;
    }

    public int getLength() {
        return length;
    }

    public boolean isSigned() {
        return signed;
    }

    public Identifier getIdentifier() {
        return new Identifier(token);
    }

    @Override
    public String toString() {
        return token;
    }

    public enum SuperType {
        INT(SDWORD),
        SQUARE(SBYTE),
        BOOL(BYTE);

        private final Type type;

        SuperType(final Type type) {
            this.type = type;
        }

        public static SuperType resolve(final String token) {
            return Arrays.stream(values())
                  .filter(type -> type.matches(token))
                  .findAny()
                  .orElseThrow(() -> new IllegalArgumentException(String.format("Cannot resolve token [%s] into any known super type.", token)));
        }

        public boolean matches(final String token) {
            return this.type.matches(token);
        }

        public Type getType() {
            return type;
        }

        public Identifier getTypeIdentifier() {
            return type.getIdentifier();
        }

        @Override
        public String toString() {
            return name();
        }
    }
}
