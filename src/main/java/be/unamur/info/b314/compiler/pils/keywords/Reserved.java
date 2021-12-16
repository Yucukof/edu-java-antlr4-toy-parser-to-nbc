package be.unamur.info.b314.compiler.pils.keywords;

import be.unamur.info.b314.compiler.pils.exceptions.ReservedNameException;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Une réservation indique qu'un mot est déjà utilisé par le vocabulaire du langage PILS et qu'il ne peut dès lors pas
 * être affecté à d'autres utilisations que celles prescrites par le langage.
 *
 * @author Hadrien BAILLY
 */
public interface Reserved {

    /**
     * Liste des mots-clé réservés.
     */
    Set<String> keywords = getKeywords();

    /**
     * Vérifie si le mot-clé passé en paramètre est un mot réservé de la grammaire du jeu (insensible à la casse), ou
     * s'il peut être utilisé pour nommer une fonction ou variable.
     * <br> Parcourt toutes les classes implémentant {@link Reserved} et récupère automatiquement leur mots-clés.
     *
     * @param keyword le mot à vérifier
     * @return vrai si le mot est réservé, faux sinon.
     * @see ReservedNameException
     * @see Property
     * @see Square
     */
    static boolean isReserved(final String keyword) {
        return keywords.stream()
              .anyMatch(name -> name.equalsIgnoreCase(keyword));
    }

    /**
     * Récupère l'ensemble des mots-clé réservés par les classes implémentant l'interface {@link Reserved}.
     *
     * @return une liste de mots-clé.
     */
    static Set<String> getKeywords() {
        Reflections reflections = new Reflections("be.unamur.info.b314.compiler");
        Set<Class<? extends Reserved>> classes = reflections.getSubTypesOf(Reserved.class);

        return classes.stream()
              .map(Reserved::getReservedWords)
              .flatMap(List::stream)
              .collect(Collectors.toSet());
    }

    /**
     * Récupère la liste des mots-clé définis par l'une des classes implémentant {@link Reserved}.
     *
     * @param clazz la classe à parcourir
     * @return une liste de mots-clé
     */
    static List<String> getReservedWords(final Class<? extends Reserved> clazz) {
        return Arrays.stream(clazz.getEnumConstants())
              .map(Reserved::getToken)
              .collect(Collectors.toList());
    }

    /**
     * Retourne la représentation textuelle du mot réservé.
     *
     * @return une chaîne de caractère.
     */
    String getToken();

    /**
     * Vérifie si la représentation passée correspond au mot-clé réservé.
     *
     * @param token la représentation à tester.
     * @return vrai si elle correspond au mot-clé peu importe la casse, faux sinon.
     */
    boolean matches(final String token);

}
