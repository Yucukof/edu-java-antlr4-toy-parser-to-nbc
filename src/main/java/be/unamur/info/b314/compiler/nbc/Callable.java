package be.unamur.info.b314.compiler.nbc;

import be.unamur.info.b314.compiler.nbc.instructions.InstructionControlJMP;

/**
 * Un élément appelable (Callable) est un élément pouvant être référencé par une instruction de contrôle, telle qu'un
 * {@link InstructionControlJMP jump}. Il doit référencer un élément existant par ailleurs dans le programme.
 *
 * @author Hadrien BAILLY
 */
public interface Callable {

    /**
     * Retourne le nom de l'élément à appeler.
     *
     * @return un identifiant estimé valide au sein du programme.
     */
    String getName();

    /**
     * Vérifie si l'élément appelable est valide.
     *
     * @return vrai si l'élément est valide, faux sinon.
     */
    boolean isValid();
}
