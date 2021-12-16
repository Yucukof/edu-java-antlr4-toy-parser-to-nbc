package be.unamur.info.b314.compiler.nbc.keywords;

import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.instructions.Instruction;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;

/**
 * Un mot-clé est un composant exprimant l'opération à effectuer lors d'une instruction NBC.
 * Il est nécessairement associé à 0, 1 ou plusieurs arguments et à 0 ou 1 élement de destination.
 *
 * @author Hadrien BAILLY
 * @see Argument
 * @see Identifier
 * @see Instruction
 */
public interface Keyword {

    /**
     * Retourne la représentation textuelle du mot-clé.
     *
     * @return une chaîne de caractères représentant le mot-clé.
     */
    String getToken();

}
