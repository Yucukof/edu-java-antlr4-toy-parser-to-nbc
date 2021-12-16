package be.unamur.info.b314.compiler.pils.values.primitives;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.values.Value;

/**
 * @author Hadrien BAILLY
 */
public class PrimitiveVoid implements Value {

    public static final PrimitiveVoid VOID = new PrimitiveVoid();

    @Override
    public Type getType() {
        return Type.VOID;
    }

    @Override
    public boolean isVoid() {
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
        return Type.VOID.getToken();
    }
}
