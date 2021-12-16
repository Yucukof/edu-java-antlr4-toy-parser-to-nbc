package be.unamur.info.b314.compiler.mappers.values.integers;

import be.unamur.info.b314.compiler.mappers.Environment;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.values.integers.Position;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
public class PositionMapper implements Function<Position, Identifier> {

    @Override
    public Identifier apply(final Position position) {

        log.trace("Processing Position [{}]", position);
        switch (position.getParameter()) {
            case LATITUDE:
                return Environment.PositionVariable.LATITUDE.getIdentifier();
            case LONGITUDE:
                return Environment.PositionVariable.LONGITUDE.getIdentifier();
            case GRID_SIZE:
                return Environment.PositionVariable.GRID_SIZE.getIdentifier();
            default:
                throw new RuntimeException("Should not happen");
        }
    }
}
