package be.unamur.info.b314.compiler.pils.values.booleans;

import be.unamur.info.b314.compiler.arena.Arena;
import be.unamur.info.b314.compiler.arena.Coordinates;
import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.keywords.Direction;
import be.unamur.info.b314.compiler.pils.keywords.Target;
import be.unamur.info.b314.compiler.pils.values.Value;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static be.unamur.info.b314.compiler.pils.keywords.Target.ENEMY;
import static be.unamur.info.b314.compiler.pils.keywords.Target.GRAAL;

/**
 * @author Hadrien BAILLY
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class Location implements Value {

    /**
     * Le type de cible recherchée.
     */
    private final Target target;
    /**
     * La direction vers laquelle observer.
     */
    private final Direction direction;
    /**
     * Le plateau de jeu à inspecter.
     */
    private Arena arena;

    @Override
    public String toString() {
        return target.getToken() + " is " + direction.getToken();
    }

    @Override
    public Boolean getBoolValue() {
        final Coordinates origin = arena.getPlayerCoordinates();

        final Coordinates target = this.target.equals(ENEMY)
              ? arena.getCoordinates(ENEMY).orElseThrow(Arena.InvalidArenaException::new)
              : arena.getCoordinates(GRAAL).orElseThrow(Arena.InvalidArenaException::new);

        return direction.isLongitudinal()
              ? origin.compareLongitude(target).equals(direction)
              : origin.compareLatitude(target).equals(direction);
    }

    @Override
    public Type getType() {
        return Type.BOOLEAN;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public boolean isValid() {
        return target != null
              && direction != null;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        return SpaceRequirement.NONE;
    }
}
