package be.unamur.info.b314.compiler.pils.values.squares;

import be.unamur.info.b314.compiler.arena.Arena;
import be.unamur.info.b314.compiler.arena.Coordinates;
import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.keywords.Square;
import be.unamur.info.b314.compiler.pils.values.Value;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Une inspection Nearby consiste à observer le plateau de jeu à partir de la position du joueur et de déterminer la
 * valeur d'une case dans un rayon de 9x9 : on indique une position relative au joueur et on détermine le type d'élément
 * contenu dans cette position.
 *
 * @author Hadrien BAILLY
 * @see Arena
 * @see Square
 */
@Data
@Builder(toBuilder = true)
public class Nearby implements Value {

    /**
     * Les coordonnées renseignées dans l'instruction NEARBY.
     */
    private final Pair<Expression, Expression> coordinates;
    /**
     * Le plateau de jeu au sein duquel effectuer l'inspection NEARBY.
     */
    private final Arena arena;

    @Override
    public String toString() {
        return String.format("nearby[%s, %s]", coordinates.getLeft().toString(), coordinates.getRight().toString());
    }

    @Override
    public Type getType() {
        return Type.SQUARE;
    }

    @Override
    public Square getSquareValue() {
        if (isValid()) {
            final Integer longitudeOffset = coordinates.getLeft().getIntValue() - 4;
            final Integer latitudeOffset = coordinates.getRight().getIntValue() - 4;

            final Coordinates player = arena.getPlayerCoordinates();

            final int targetLongitude = player.getLongitude() + longitudeOffset;
            final int targetLatitude = player.getLatitude() + latitudeOffset;
            final Coordinates target = new Coordinates(targetLongitude, targetLatitude);

            if (target.isValid(arena)) {
                return arena.get(target);
            } else {
                return Square.DIRT;
            }
        }
        throw new RuntimeException();
    }

    @Override
    public boolean isSquare() {
        return true;
    }

    @Override
    public boolean isValid() {
        return coordinates != null
              && isValidCoordinate(coordinates.getLeft())
              && isValidCoordinate(coordinates.getRight());
    }

    @Override
    public boolean isConstant() {
        return coordinates.getLeft().isPrimitive()
              && coordinates.getRight().isPrimitive();
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        final SpaceRequirement base = new SpaceRequirement(2, 0, 0);
        final SpaceRequirement r1 = coordinates.getLeft().getSpaceRequirement();
        final SpaceRequirement r2 = coordinates.getRight().getSpaceRequirement();
        final SpaceRequirement expressions = SpaceRequirement.combine(r1, r2);
        return SpaceRequirement.merge(base, expressions);
    }

    private static boolean isValidCoordinate(final Expression expression) {
        return expression != null && expression.isValid() && expression.isInteger()
              && expression.getIntValue() >= 0 && expression.getIntValue() < 9;
    }
}
