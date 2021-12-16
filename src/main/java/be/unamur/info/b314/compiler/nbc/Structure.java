package be.unamur.info.b314.compiler.nbc;

import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;

import java.util.List;

/**
 * Une structure représente un pattern récurrent d'éléments NBC pour formuler un traitement.
 *
 * @author Hadrien BAILLY
 */
public interface Structure {

    /**
     * Un raccourci pour construire une structure vide d'éléments.
     *
     * @return une structure simple sans instructions.
     */
    static Structure EMPTY() {
        return StructureSimple.builder().build();
    }

    /**
     * Une méthode permettant de vérifier que la structure est valide d'un point de vue sémantique.
     *
     * @return vrai si la structure est valide, faux sinon.
     */
    boolean isValid();

    /**
     * Retourne la liste des instructions et labels contenus dans la structure s'il y en a.
     *
     * @return Une liste d'éléments de code NBC.
     */
    List<Statement> getStatements();
}
