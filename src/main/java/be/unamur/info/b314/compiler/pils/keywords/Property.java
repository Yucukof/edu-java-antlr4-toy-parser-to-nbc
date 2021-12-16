package be.unamur.info.b314.compiler.pils.keywords;

import be.unamur.info.b314.compiler.pils.declarations.Type;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Reserved words for Game Properties
 *
 * @author Hadrien BAILLY
 */
public enum Property implements Reserved {
    /**
     * Le plateau de jeu.
     */
    ARENA("arena", Type.SQUARE, Arrays.asList(1, 1)),
    /**
     * Le compteur d'objets de type MAP.
     */
    MAP("map", Type.INTEGER, Collections.emptyList()),
    /**
     * Le compteur d'objets de type RADIO.
     */
    RADIO("radio", Type.INTEGER, Collections.emptyList()),
    /**
     * Le compteur d'objets de type RADIO.
     */
    RADAR("radar", Type.INTEGER, Collections.emptyList()),
    /**
     * Le compteur d'objets de type AMMO.
     */
    AMMO("ammo", Type.INTEGER, Collections.emptyList()),
    /**
     * Le compteur d'objets de type FRUITS.
     */
    FRUITS("fruits", Type.INTEGER, Collections.emptyList()),
    /**
     * Le compteur d'objets de type SODA.
     */
    SODA("soda", Type.INTEGER, Collections.emptyList()),
    /**
     * Le compteur de points de vie.
     */
    LIFE("life", Type.INTEGER, Collections.emptyList()),
    /**
     * L'indicateur de position horizontal du personnage.
     */
    LATITUDE("latitude", Type.INTEGER, Collections.emptyList()),
    /**
     * L'indicateur de position verticale du personnage.
     */
    LONGITUDE("longitude", Type.INTEGER, Collections.emptyList()),
    /**
     * La taille du plateau de jeu.
     */
    GRIDSIZE("grid", Type.INTEGER, Collections.emptyList());

    private final String token;
    private final Type type;
    private final List<Integer> dimensions;

    Property(final String token, final Type type, final List<Integer> dimensions) {
        this.token = token;
        this.type = type;
        this.dimensions = dimensions;
    }

    /**
     * Retourne la propriété correspondante au mot-clé donné en paramètre si applicable (insensible à la casse).
     *
     * @param keyword le mot-clé à évaluer
     * @return une propriété du jeu.
     * @throws IllegalArgumentException si le mot-clé ne correspond à aucune propriété connue.
     */
    public static Property resolve(final String keyword) {
        return Arrays.stream(values())
              .filter(property -> property.matches(keyword))
              .findAny()
              .orElseThrow(() -> new IllegalArgumentException("Cannot find corresponding reserved keyword for [" + keyword + "]"));
    }

    /**
     * Retourne le mot-clé correspondant à la propriété en minuscule.
     *
     * @return le mot-clé correspondant.
     */
    public String get() {
        return name().toLowerCase();
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public boolean matches(final String token) {
        return getToken().equalsIgnoreCase(token);
    }

    public Type getType() {
        return type;
    }

    public List<Integer> getDimensions() {
        return dimensions;
    }
}
