package be.unamur.info.b314.compiler.nbc.declarations;

import be.unamur.info.b314.compiler.nbc.definitions.Definition;
import be.unamur.info.b314.compiler.nbc.keywords.Language;
import be.unamur.info.b314.compiler.nbc.Statement;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data segment blocks are used to define types and to declare variables. An NBC program
 * can have zero or more data segments, which can be placed either outside of a
 * code block or within a code block. Regardless of the location of the data segment, all
 * variables in an NBC program are global
 *
 * @author Hadrien BAILLY
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Segment extends Declaration implements Statement {

    @Getter(AccessLevel.NONE)
    private static final String DSEG = Language.DSEG.getToken();
    @Getter(AccessLevel.NONE)
    private static final String SEGMENT = Language.SEGMENT.getToken();
    @Getter(AccessLevel.NONE)
    private static final String ENDS = Language.ENDS.getToken();

    @Singular
    private final List<Definition> definitions;

    @Override
    public String toString() {
        return DSEG + " " + SEGMENT
              + (definitions.isEmpty() ? "" : ("\n\t" + definitions.stream().map(Definition::toString).collect(Collectors.joining("\n\t"))))
              + "\n" + DSEG + " " + ENDS;
    }

    @Override
    public boolean isValid() {
        return definitions.stream().allMatch(Definition::isValid);
    }

    public int size() {
        return definitions.size();
    }
}
