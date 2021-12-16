package be.unamur.info.b314.compiler.pils.values.integers;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.keywords.Item;
import be.unamur.info.b314.compiler.pils.values.Value;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Hadrien BAILLY
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class Count implements Value {

    /**
     * L'objet dont on doit obtenir le compte depuis l'inventaire.
     */
    private final Item item;
    /**
     * Une valeur mémorisée.
     */
    private Variable variable;

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
        return item != null;
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        return SpaceRequirement.NONE;
    }

    @Override
    public String toString() {
        return item.getToken() + " count";
    }
}
