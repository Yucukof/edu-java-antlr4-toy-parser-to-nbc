package be.unamur.info.b314.compiler.pils.statements;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.values.Value;

import java.util.Optional;

/**
 * Une instruction est une commande du langage PILS permettant de manipuler des données au sein du programme et de faire
 * effectuer des actions au personnage.
 * <br>
 * Ces instructions peuvent être plus ou moins complexes et impliquer d'autres instructions par décoration.
 *
 * @author Hadrien BAILLY
 */
public interface Statement {

    /**
     * Une méthode permettant de vérifier si l'instruction est valide sur le plan sémantique.
     *
     * @return vrai si l'instruction est valide, faux sinon.
     */
    boolean isValid();

    /**
     * Indique l'espace-mémoire nécessaire à la réalisation du calcul de l'instruction.
     *
     * @return un compte des différents registres nécessaires simultanément.
     * @see Value#getSpaceRequirement()
     * @see Expression#getSpaceRequirement()
     */
    SpaceRequirement getSpaceRequirement();

    /**
     * Exécute le statement et retourne le statement Next à exécuter s'il y en a un.
     *
     * @return un statement NEXT si applicable.
     */
    Optional<StatementNext> run();
}
