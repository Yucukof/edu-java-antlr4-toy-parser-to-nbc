package be.unamur.info.b314.compiler.pils.actions;


import be.unamur.info.b314.compiler.pils.keywords.Act;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Une action est une instruction faisant déplacer/interagir le personnage dans le jeu.
 *
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder(toBuilder = true)
public abstract class Action {

    /**
     * L'acte à réaliser.
     */
    private final Act act;

    /**
     * Un constructeur simple qui instancie l'action avec le mot-clé représentant l'acte à effectuer.
     *
     * @param act l'acte à effectuer.
     */
    public Action(final Act act) {
        this.act = act;
    }

    /**
     * Une méthode permettant de vérifier qu'une action est correcte d'un point de vue sémantique.
     *
     * @return vra si l'action est valide, faux sinon.
     */
    public boolean isValid() {
        return act != null;
    }

}
