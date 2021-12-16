package be.unamur.info.b314.compiler.nbc;

/**
 * Un statement est un élément de code NBC formant une entité atomique exprimant une portion de code.
 *
 * @author Hadrien BAILLY
 */
public interface Statement {

    /**
     * Une méthode permettant de s'assurer qu'un statement est correct d'un point de vue sémantique.
     *
     * @return vrai si le statement est valide, faux sion.
     */
    boolean isValid();

}
