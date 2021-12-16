package be.unamur.info.b314.compiler.nbc.routines;

import be.unamur.info.b314.compiler.nbc.Block;
import be.unamur.info.b314.compiler.nbc.Structure;
import be.unamur.info.b314.compiler.nbc.declarations.Segment;
import be.unamur.info.b314.compiler.nbc.definitions.Definition;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.declarations.Declaration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * There are two primary types of code blocks: thread and subroutines. Each of these
 * types of code blocks has its own unique features and restrictions, but they share a
 * common structure.
 *
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Routine extends Definition implements Block {

    /**
     * Data segments contain all type definitions and variable definitions.
     */
    @Singular
    private final List<Segment> segments;
    /**
     * The body of a thread consists of a list of statements and optional data segments.
     */
    @Singular
    private final List<Structure> structures;


    public void add(final Segment block) {
        segments.add(block);
    }

    public void add(final List<Segment> blocks) {
        segments.addAll(blocks);
    }

    public void append(final Structure block) {
        structures.add(block);
    }

    public void append(final List<Structure> blocks) {
        structures.addAll(blocks);
    }

    @Override
    public String toString() {
        return (segments.size() == 0 ? "" : segments.stream().map(Objects::toString).collect(Collectors.joining("\n")) + "\n")
              + structures.stream().map(Structure::getStatements).map(Objects::toString).collect(Collectors.joining("\n"));
    }

    @Override
    public boolean isValid() {
        return !structures.isEmpty();
    }


}
