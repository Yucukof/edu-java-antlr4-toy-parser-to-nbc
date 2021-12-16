package be.unamur.info.b314.compiler.nbc.symbols;

import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.Callable;
import be.unamur.info.b314.compiler.nbc.keywords.Keyword;
import be.unamur.info.b314.compiler.nbc.keywords.Reserved;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Identifiers are used for variable, task, function, and subroutine names. The first character of an identifier
 * must be an upper or lower case letter or the underscore (’_’). Remaining characters may be letters, numbers, and
 * underscores.
 * <br>
 * A number of tokens are reserved for use in the NBC language itself. These are called keywords and may not be used as
 * identifiers.
 *
 * @author Hadrien BAILLY
 * @see Keyword
 */
@Data
@SuperBuilder(toBuilder = true)
public class Identifier implements Argument, Callable {

    /**
     * Le pattern de validité d'un identifiant au sein du langage NBC.
     */
    private static final String Identifier_PATTERN = "[A-Za-z_]([A-Za-z_0-9]*)";
    /**
     * La valeur de l'identifiant.
     */
    private final String name;

    public Identifier(final String name) {
        this.name = name;
    }

    /**
     * Génère un identifiant main
     *
     * @return l'identifiant main
     */
    public static Identifier main() {
        return new Identifier("main");
    }

    @Override
    public String getToken() {
        return toString();
    }

    /**
     * Vérifie si l'identifiant courant est valide, c'est-à-dire qu'il
     * répond à la syntaxe et n'est pas un mot-clé réservé
     *
     * @return vrai si l'identifiant peut être utilisé, faux sinon.
     */
    public boolean isValid() {
        return hasValidSyntax() && !isReservedKeyword();
    }

    /**
     * Vérifie si l'identifiant courant répond aux règles de syntaxe définie par le language NBC.
     *
     * @return vrai si l'identifiant est valide, faux sinon
     */
    public boolean hasValidSyntax() {
        return name.matches(Identifier_PATTERN);
    }

    /**
     * Vérifie si l'identifiant courant est un mot-réservé au sein de la grammaire NBC
     *
     * @return vrai si l'identifiant peut être résolu dans un mot-clé,
     *       faux s'il déclenche une exception durant la résolution.
     */
    public boolean isReservedKeyword() {
        return Reserved.isReserved(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }
}
