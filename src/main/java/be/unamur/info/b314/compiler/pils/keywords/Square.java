package be.unamur.info.b314.compiler.pils.keywords;

import be.unamur.info.b314.compiler.arena.Arena;

import java.util.Arrays;

/**
 * La liste des mots-clés réservés pour la désignation des cases du plateau de jeu.
 *
 * @author Hadrien BAILLY
 * @see Arena
 */
public enum Square implements Reserved {
    /**
     * Une case vide
     */
    DIRT("dirt", ""),
    /**
     * Une case occupée par un obstacle ROCK.
     */
    ROCK("rock", "X"),
    /**
     * Une case occupée par un obstacle VINE.
     */
    VINES("vines", "%"),
    /**
     * Une case occupée par un personnage non-jouable ZOMBIE.
     */
    ZOMBIE("zombie", "Z"),
    /**
     * La case occupée par le personnage du joueur.
     */
    PLAYER("player", "P"),
    /**
     * Une case occupée par un personnage d'un autre joueur.
     */
    ENEMY("ennemi", "E"),
    /**
     * Une case contenant un objet MAP.
     */
    MAP("map", "M"),
    /**
     * Une case contenant un objet RADAR.
     */
    RADAR("radar", "R"),
    /**
     * Une case contenant un objet RADIO.
     */
    RADIO("radio", "R"),
    /**
     * Une case contenant un objet AMMO.
     */
    AMMO("ammo", "A"),
    /**
     * Une case contenant un objet FRUITS.
     */
    FRUITS("fruits", "F"),
    /**
     * Une case contenant un objet SODA.
     */
    SODA("soda", "S"),
    /**
     * Une case contenant le GRAAL.
     */
    GRAAL("graal", "G");

    private final String token;
    private final String symbol;

    Square(final String token, final String symbol) {
        this.token = token;
        this.symbol = symbol;
    }

    /**
     * Retourne la case correspondante au mot-clé donné en paramètre si applicable (insensible à la casse).
     *
     * @param keyword le mot-clé à évaluer
     * @return une case du jeu.
     * @throws IllegalArgumentException si le mot-clé ne correspond à aucun type de case connu.
     */
    public static Square resolve(final String keyword) {
        return Arrays.stream(values())
              .filter(square -> square.matches(keyword))
              .findAny()
              .orElseThrow(() -> new IllegalArgumentException("Cannot parse [" + keyword + "] into any known square type."));
    }

    /**
     * Vérifie si le mot-clé correspond à un type de case du jeu (insensible à la casse).
     *
     * @param keyword le mot-clé à évaluer
     * @return vrai si le mot-clé est un type de case, faux sinon.
     */
    public boolean matches(final String keyword) {
        return token.equalsIgnoreCase(keyword);
    }

    /**
     * Retourne la valeur textuelle réelle du type de case.
     *
     * @return une chaîne de caractères.
     */
    @Override
    public String getToken() {
        return token;
    }

    /**
     * Retourne le mot-clé correspondant au type de case en minuscule.
     *
     * @return le mot-clé correspondant.
     */
    public String get() {
        return name().toLowerCase();
    }

    /**
     * Retourne un symbole pour l'affichage low-fi de l'arène.
     *
     * @return Un caractère unique représenant la case.
     */
    public String getSymbol() {
        return symbol;
    }
}
