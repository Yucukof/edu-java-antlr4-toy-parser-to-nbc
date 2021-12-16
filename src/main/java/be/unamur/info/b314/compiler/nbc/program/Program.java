package be.unamur.info.b314.compiler.nbc.program;

import be.unamur.info.b314.compiler.nbc.Block;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Hadrien BAILLY
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Program {

    @Singular
    private final List<Include> inclusions;

    @Singular
    private final List<Block> blocks;

    @Override
    public String toString() {
        return (inclusions.size() > 0 ? inclusions.stream().map(Objects::toString).collect(Collectors.joining("\n")) + "\n\n" : "")
              + blocks.stream().map(Objects::toString).collect(Collectors.joining("\n\n"));
    }

    public boolean isValid() {
        return inclusions.stream().allMatch(Include::isValid)
              && blocks.stream().allMatch(Block::isValid);
    }
}
