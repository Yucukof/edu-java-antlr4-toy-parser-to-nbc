package be.unamur.info.b314.compiler.nbc.definitions;

import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.nbc.keywords.Type;
import be.unamur.info.b314.compiler.nbc.keywords.Language;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Structure definitions must also be contained within a data segment. They are used
 * to define a type which aggregates or contains other native or user-defined types.
 *
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DefinitionStruct extends Definition {

    @Getter(AccessLevel.NONE)
    private static final String STRUCT = Language.STRUCT.getToken();
    @Getter(AccessLevel.NONE)
    private static final String ENDS = Language.ENDS.getToken();

    List<Pair<Identifier, Type>> definitions;

    @Override
    public String toString() {
        return getIdentifier() + " " + STRUCT + "\n"
              + getIdentifier() + " " + ENDS;
    }

}
