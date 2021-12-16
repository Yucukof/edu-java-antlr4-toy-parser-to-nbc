package be.unamur.info.b314.compiler.pils.values.integers;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.keywords.Property;
import be.unamur.info.b314.compiler.pils.values.Value;
import lombok.Data;

/**
 * @author Hadrien BAILLY
 */
@Data
public class Life implements Value {

    private final Variable variable;

    @Override
    public Type getType() {
        return Type.INTEGER;
    }

    @Override
    public Integer getIntValue() {
        return variable.get().getIntValue();
    }

    @Override
    public boolean isInteger() {
        return true;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        return SpaceRequirement.NONE;
    }

    @Override
    public String toString() {
        return Property.LIFE.getToken();
    }
}
