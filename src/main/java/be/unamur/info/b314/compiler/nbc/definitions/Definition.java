package be.unamur.info.b314.compiler.nbc.definitions;

import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Une définition est une déclaration permettant d'associer un identifiant à un élément contenant un certain type de
 * données et/ou d'instruction, identifiant qui peut ensuite être référencé à d'autres endroits du code pour manipuler
 * les données contenues au sein de cet élément.
 *
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder(toBuilder = true)
public abstract class Definition {

    /**
     * Le nouveau nom du type
     */
    private final Identifier identifier;

    /**
     * Une méthode permettant de s'assurer qu'une définition est correcte d'un point de vue sémantique
     *
     * @return vrai si la définition est correcte, faux sinon.
     */
    public boolean isValid() {
        return identifier.isValid();
    }

}
