package be.unamur.info.b314.compiler.pils.actions;

import be.unamur.info.b314.compiler.pils.keywords.Act;
import be.unamur.info.b314.compiler.pils.keywords.Item;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ActionUse extends Action {

    private final Item item;

    public ActionUse(final Item item) {
        super(Act.USE);
        this.item = item;
    }

    @Override
    public String toString() {
        return Act.USE.getToken() + " " + item.getToken();
    }

    @Override
    public boolean isValid() {
        return super.isValid() && getAct().equals(Act.USE) && item != null;
    }
}
