package be.unamur.info.b314.compiler.nbc.symbols;

import be.unamur.info.b314.compiler.nbc.Argument;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Un identifiant indicé est un identifiant représentant un tableau et augmenté d'une suite d'indices permettant de
 * désigner une valeur au sein dudit tableau.
 *
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class IndexedIdentifier extends Identifier {

    /**
     * La liste des arguments permettant d'accéder au contenu du tableau représenté par l'identifiant.
     */
    @Singular
    public List<Argument> indexes;

    @Override
    public boolean isValid() {
        return super.isValid()
              && indexes.stream().allMatch(Argument::isValid);
    }

    @Override
    public String toString() {
        return getName()
              + (indexes.size() > 0 ? "[" + indexes.stream().map(Objects::toString).collect(Collectors.joining("][")) + "]" : "");
    }
}
