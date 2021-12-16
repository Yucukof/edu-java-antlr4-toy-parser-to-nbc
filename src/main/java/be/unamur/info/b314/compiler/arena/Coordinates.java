package be.unamur.info.b314.compiler.arena;

import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.keywords.Direction;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static be.unamur.info.b314.compiler.pils.keywords.Direction.*;

/**
 * Des coordonnées sont un couple de valeurs indiquant la position d'un objet sur le plateau de jeu Arena.
 *
 * @author Hadrien BAILLY
 * @see Arena
 */
@Data
@RequiredArgsConstructor
public class Coordinates {

    /**
     * La coordonnée
     */
    final Integer longitude;
    /**
     * L'abscisse
     */
    final Integer latitude;

    /**
     * Un constructeur de coordonnées sur base d'une liste d'indices.
     *
     * @param list la liste d'indices concaténés à décomposer dans une coordonnées.
     * @see Variable#createKey(List)
     */
    public Coordinates(final String list) {
        final String[] indexes = list.split("\\|");
        this.longitude = Integer.parseInt(indexes[0]);
        this.latitude = Integer.parseInt(indexes[1]);
    }

    /**
     * Vérifie si les coordonnées
     *
     * @param arena l'arène à l'intérieur de laquelle doivent pointer les coordonnées.
     * @return vrai si les coordonnées sont comprises strictement entre les bornes dimensionnelles de l'arène, faux sinon.
     */
    public boolean isValid(final Arena arena) {
        return longitude >= 0 && longitude < arena.getSize()
              && latitude >= 0 && latitude < arena.getSize();
    }

    /**
     * Compares deux coordonnées sur le plan de la latitude.
     *
     * @param target les coordonnées à comparer avec les coordonnées courantes
     * @return SOUTH si la latitude des coordonnées courantes est strictement inférieure à celle des coordonnées-cibles,
     *       NORTH sinon.
     * @see Direction#NORTH
     * @see Direction#SOUTH
     */
    public Direction compareLatitude(final Coordinates target) {
        return latitude < target.latitude
              ? SOUTH
              : NORTH;
    }

    /**
     * Compares deux coordonnées sur le plan de la longitude.
     *
     * @param target les coordonnées à comparer avec les coordonnées courantes
     * @return EAST si la longitude des coordonnées courantes est strictement inférieure à celle des coordonnées-cibles,
     *       WEST sinon.
     * @see Direction#EAST
     * @see Direction#WEST
     */
    public Direction compareLongitude(final Coordinates target) {
        return longitude < target.longitude
              ? EAST
              : WEST;
    }
}
