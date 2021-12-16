package be.unamur.info.b314.compiler.nbc.instructions;

import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.Statement;
import be.unamur.info.b314.compiler.nbc.keywords.Keyword;
import be.unamur.info.b314.compiler.nbc.symbols.Comparator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Une instruction est l'élément de code permettant de manipuler la mémoire ou l'exécution du code du programme NBC.
 * <br>
 * Elle comprend nécessairement un mot-clé exprimant son intention.
 *
 * @author Hadrien BAILLY
 * @see Keyword
 */
@Data
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Instruction implements Statement {

    /**
     * Le mot-clé définissant le type d'instruction à exécuter.
     */
    private final Keyword keyword;

    @Override
    public boolean isValid() {
        return keyword != null;
    }

    @Override
    public String toString() {
        return keyword.getToken();
    }

    /**
     * Une condition est une composante optionnelle d'une instruction qui détermine l'exécution, le résultat de cette
     * operation.
     */
    @Data
    @Builder
    public static class Condition {
        /**
         * L'opérateur de comparaison.
         */
        private final Comparator comparator;
        /**
         * Le membre gauche de la comparaison.
         */
        private final Argument left;
        /**
         * Le membre droit de la comparaison.
         */
        private final Argument right;

        /**
         * Une méthode permettant de s'assurer qu'une condition est correct d'un point de vue sémantique.
         *
         * @return vrai si la condition est valide, faux sinon.
         */
        public boolean isValid() {
            return comparator != null
                  && left != null && left.isValid()
                  && right != null && right.isValid();
        }

        @Override
        public String toString() {
            return comparator + " " + left + ", " + right;
        }
    }

}
