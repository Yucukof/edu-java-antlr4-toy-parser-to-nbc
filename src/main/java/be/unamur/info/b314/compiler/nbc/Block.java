package be.unamur.info.b314.compiler.nbc;

/**
 * Un block est un élément général et unitaire de code en NBC, qui fait partie d'un programme.
 *
 * @author Hadrien BAILLY
 */
public interface Block {

    /**
     * Une méthode permettant de s'assurer que le bloc est valide d'un point de vue sémantique.
     *
     * @return vrai si le bloc est valide, faux sinon.
     */
    boolean isValid();
}
