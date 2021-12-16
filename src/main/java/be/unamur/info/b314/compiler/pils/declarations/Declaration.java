package be.unamur.info.b314.compiler.pils.declarations;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;

/**
 * Une déclaration est l'action d'ajouter une nouvelle entité conceptuelle au programme, qui peut contenir une valeur
 * et/ou effectuer des opérations, et qui peut être référencée par la suite à d'autres endroits du programme pour
 * récupérer cette valeur ou appliquer ces opérations.
 *
 * @author Hadrien BAILLY
 */
public interface Declaration {

    /**
     * Une déclaration possède toujours une dénomination.
     *
     * @return le nom de la déclaration.
     */
    String getName();

    /**
     * Une déclaration prend une forme propre à chaque type d'entité.
     *
     * @return la déclaration écrite de chaque entité.
     */
    String getDeclaration();

    /**
     * Une déclaration est nécessairement associée à un type.
     *
     * @return le type de la déclaration.
     */
    Type getType();

    /**
     * Une méthode permettant de s'assurer que la déclaration est valide d'un point de vue sémantique.
     *
     * @return vrai si la déclaration est correcte, faux sinon.
     */
    boolean isValid();

    /**
     * Retourne le type intrinsèque de la déclaration.
     *
     * @return le nom de la classe implémentant l'interface Déclaration.
     */
    default String getKind() {
        return getClass().getName();
    }

    /**
     * Une déclaration peut parfois nécessiter des registres (par ex. une fonction qui dispose de ses propres
     * variables).
     * <br>
     * Dans ce cas, elle peut exiger ses propres besoin en fonctionnement.
     *
     * @return Un prérequis en espace.
     */
    default SpaceRequirement getSpaceRequirement() {
        return SpaceRequirement.NONE;
    }
}
