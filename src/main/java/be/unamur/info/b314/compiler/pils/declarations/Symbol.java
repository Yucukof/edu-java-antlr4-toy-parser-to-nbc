package be.unamur.info.b314.compiler.pils.declarations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

/**
 * Un symbole est un élément nominatif du langage PILS dont la signification, la valeur, peut changer au cours de
 * l'exécution.
 *
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class Symbol {

    /**
     * Numéro unique identifiant un symbole dans le programme en cours de traitement.
     */
    private int id;
    /**
     * Nom usuel du symbole tel qu'utilisé dans le programme en cours de traitement.
     */
    private String name;
    /**
     * Le niveau de profondeur auquel le symbole est défini.
     */
    private int level;

    @Override
    public String toString() {
        return getName();
    }

    public boolean isValid() {
        return StringUtils.isNotEmpty(name)
              && level >= 0;
    }
}
