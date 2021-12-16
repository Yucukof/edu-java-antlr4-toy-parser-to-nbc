package be.unamur.info.b314.compiler.nbc.keywords;

import be.unamur.info.b314.compiler.pils.exceptions.ReservedNameException;
import be.unamur.info.b314.compiler.pils.keywords.Property;
import be.unamur.info.b314.compiler.pils.keywords.Square;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
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
        return Arrays.stream(clazz.getDeclaredFields())
              .filter(Field::isEnumConstant)
              .map(Field::getName)
              .collect(Collectors.toList());
    }
}
