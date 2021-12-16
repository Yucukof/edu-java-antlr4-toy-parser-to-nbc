package be.unamur.info.b314.compiler.nbc.definitions;

import be.unamur.info.b314.compiler.nbc.keywords.Language;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Type definitions must be contained within a data segment. They are used to define new
 * type aliases or new aggregate types (i.e., structures). A type alias is defined using the
 * typedef keyword with the following syntax
 *
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DefinitionType extends Definition {

    @Getter(AccessLevel.NONE)
    private static final String TYPEDEF = Language.TYPEDEF.getToken();
    /**
     * Le type existant à renommer
     */
    // TODO 24/03/2021 [HBA]: handle arrays
    private final String existingType;

    @Override
    public String toString() {
        return getIdentifier() + " " + TYPEDEF + " " + existingType;
    }
}
