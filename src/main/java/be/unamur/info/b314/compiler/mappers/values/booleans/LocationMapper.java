package be.unamur.info.b314.compiler.mappers.values.booleans;

import be.unamur.info.b314.compiler.mappers.Environment;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.values.booleans.Location;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

import static be.unamur.info.b314.compiler.mappers.Environment.LocationVariable.*;
import static be.unamur.info.b314.compiler.pils.keywords.Target.GRAAL;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
public class LocationMapper implements Function<Location, Identifier> {

    @Override
    public Identifier apply(final Location location) {

        log.trace("Processing Location [{}]", location);
        final Environment.LocationVariable variable;

        switch (location.getDirection()) {
            case NORTH:
                variable = location.getTarget().equals(GRAAL) ? GRAAL_NORTH : ENEMY_NORTH;
                break;
            case SOUTH:
                variable = location.getTarget().equals(GRAAL) ? GRAAL_SOUTH : ENEMY_SOUTH;
                break;
            case EAST:
                variable = location.getTarget().equals(GRAAL) ? GRAAL_EAST : ENEMY_EAST;
                break;
            case WEST:
                variable = location.getTarget().equals(GRAAL) ? GRAAL_WEST : ENEMY_WEST;
                break;
            default:
                throw new RuntimeException("Should not happen");
        }
        return variable.getIdentifier();
    }
}
