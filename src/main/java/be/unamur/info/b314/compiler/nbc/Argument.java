package be.unamur.info.b314.compiler.nbc;

import be.unamur.info.b314.compiler.nbc.instructions.Instruction;

/**
 * Un argument est un élément pouvant intervenir au sein d'une instruction NBC.
 * <br>
 * Tout argument doit pouvoir être transposé en texte
 *
 * @author Hadrien BAILLY
 * @see Instruction
 */
public interface Argument {

    /**
     * Retourne l'expression textuelle de l'argument.
     *
     * @return une chaîne de caractères représentant l'argument.
     */
    String getToken();

    /**
     * Une méthode permettant de s'assurer que l'argument est valide d'un point de vue sémantique.
     *
     * @return vrai si l'argument est valide, faux sinon.
     */
    boolean isValid();
}
