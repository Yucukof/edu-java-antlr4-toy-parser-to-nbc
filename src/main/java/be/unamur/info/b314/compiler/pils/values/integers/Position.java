package be.unamur.info.b314.compiler.pils.values.integers;

import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.keywords.PositionParameter;
import be.unamur.info.b314.compiler.pils.keywords.Property;
import be.unamur.info.b314.compiler.pils.values.Value;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;

/**
 * Une position est un paramètre se référant à la taille de l'arène ou à la position du joueur.
 * Cette information est calculée depuis l'arène puis stockée dans une variable d'environnement dédiée.
 *
 * @author Hadrien BAILLY
 * @see Property#LONGITUDE
 * @see Property#LATITUDE
 * @see Property#GRIDSIZE
 */
@Data
@Builder()
@EqualsAndHashCode(callSuper = false)
public class Position implements Value {

    public static final Position LATITUDE = Position.builder().parameter(PositionParameter.LATITUDE).build();
    public static final Position LONGITUDE = Position.builder().parameter(PositionParameter.LONGITUDE).build();
    public static final Position GRID_SIZE = Position.builder().parameter(PositionParameter.GRID_SIZE).build();

    /**
     * Le paramètre du plateau de jeu à interpréter.
     */
    private final PositionParameter parameter;
    /**
     * Le contexte
     */
    private final Context context;

    @Override
    public Type getType() {
        return Type.INTEGER;
    }

    @Override
    public Integer getIntValue() {
        return Optional.ofNullable(context.getVariable(parameter.getToken()))
              .map(Variable::get)
              .map(Value::getIntValue)
              .orElse(1);
    }

    @Override
    public boolean isInteger() {
        return true;
    }

    @Override
    public boolean isValid() {
        return parameter != null;
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        return SpaceRequirement.NONE;
    }

    @Override
    public String toString() {
        return parameter.getToken();
    }

}
