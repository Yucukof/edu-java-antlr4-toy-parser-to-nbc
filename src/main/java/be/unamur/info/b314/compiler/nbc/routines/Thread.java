package be.unamur.info.b314.compiler.nbc.routines;

import be.unamur.info.b314.compiler.nbc.Statement;
import be.unamur.info.b314.compiler.nbc.Structure;
import be.unamur.info.b314.compiler.nbc.declarations.Segment;
import be.unamur.info.b314.compiler.nbc.keywords.Language;
import be.unamur.info.b314.compiler.nbc.keywords.Scheduling;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The NXT implicitly supports multi-threading, thus an NBC thread directly corresponds
 * to an NXT thread.
 * <br>
 * The name of the thread may be any legal identifier. A program must always have at
 * least one thread. If there is a thread named "main" then that thread will be the thread
 * that is started whenever the program is run. If none of the threads are named "main"
 * then the very first thread that the compiler encounters in the source code will be the
 * main thread. The maximum number of threads supported by the NXT is 256.
 *
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Thread extends Routine {

    @Getter(AccessLevel.NONE)
    private static final String THREAD = Language.THREAD.getToken();
    @Getter(AccessLevel.NONE)
    private static final String PRECEDES = Scheduling.PRECEDES.getToken();
    @Getter(AccessLevel.NONE)
    private static final String FOLLOWS = Scheduling.FOLLOWS.getToken();
    @Getter(AccessLevel.NONE)
    private static final String ENDT = Language.ENDT.getToken();

    /**
     * The follows statement causes the compiler to mark the current thread as a dependant
     * of the threads listed in the statement. The current thread will be scheduled to execute if
     * all of the threads that precede it have exited and scheduled it for execution.
     */
    @Builder.Default
    private final List<Identifier> follows = new ArrayList<>();
    /**
     * he precedes statement causes the compiler to mark the threads listed in the statement
     * as dependants of the current thread. A subset of these threads will begin executing once
     * the current thread exits, depending on the form of the exit statement used at the end of
     * the current thread
     */
    @Builder.Default
    private final List<Identifier> precedes = new ArrayList<>();

    /**
     * Génère un thread main
     *
     * @return le thread main
     */
    public static Thread main() {
        return Thread.builder()
              .identifier(Identifier.main())
              .build();
    }

    @Override
    public String toString() {
        return THREAD + " " + getIdentifier() + "\n"
              + (getSegments().size() > 0 ? "\t" + getSegments().stream().map(Segment::toString).collect(Collectors.joining("\n\t")) + "\n" : "")
              + (follows.size() > 0 ? "\t" + FOLLOWS + String.join(", ", follows.stream().map(Identifier::getName).toArray(String[]::new)) + "\n" : "")
              + (precedes.size() > 0 ? "\t" + PRECEDES + precedes.stream().map(Identifier::getName).collect(Collectors.joining(", ")) + "\n" : "")
              + ("\t" + getStructures().stream().map(Structure::getStatements).flatMap(List::stream).map(Statement::toString).collect(Collectors.joining("\n\t")) + "\n")
              + ENDT;
    }
}
