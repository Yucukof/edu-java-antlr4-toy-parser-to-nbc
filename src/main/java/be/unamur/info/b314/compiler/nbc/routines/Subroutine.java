package be.unamur.info.b314.compiler.nbc.routines;

import be.unamur.info.b314.compiler.nbc.Structure;
import be.unamur.info.b314.compiler.nbc.declarations.Segment;
import be.unamur.info.b314.compiler.nbc.keywords.Language;
import be.unamur.info.b314.compiler.nbc.keywords.Scheduling;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Subroutines allow a single copy of some code to be shared between several different
 * callers. This makes subroutines much more space efficient than macro functions.
 * <br>
 * A subroutine is just a special type of thread that is designed to be called explicitly by
 * other threads or subroutines. Its name can be any legal identifier. Subroutines are not
 * scheduled to run via the same mechanism that is used with threads. Instead, subroutines
 * and threads execute other subroutines by using the call statement (described in the
 * Statements section).
 *
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Subroutine extends Routine {

    @Getter(AccessLevel.NONE)
    private static final String SUBROUTINE = Language.SUBROUTINE.getToken();
    @Getter(AccessLevel.NONE)
    private static final String RETURN = Scheduling.RETURN.getToken();
    @Getter(AccessLevel.NONE)
    private static final String ENDS = Language.ENDS.getToken();

    @Override
    public String toString() {
        return SUBROUTINE + " " + getIdentifier().getToken()
              + (getSegments().size() > 0 ? "\n\t" + getSegments().stream().map(Segment::toString).collect(Collectors.joining("\n\t")) : "")
                + (getStructures().size() > 0 ? "\n\t" + getStructures().stream()
                    .map(Structure::getStatements)
                    .flatMap(List::stream)
                    .map(Objects::toString)
                    .collect(Collectors.joining("\n\t")) : "")
              + "\n\t" + RETURN
              + "\n" + ENDS;
    }
}
