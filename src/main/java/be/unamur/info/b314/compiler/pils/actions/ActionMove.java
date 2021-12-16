package be.unamur.info.b314.compiler.pils.actions;

import be.unamur.info.b314.compiler.pils.keywords.Act;
import be.unamur.info.b314.compiler.pils.keywords.Direction;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ActionMove extends Action {

    private final Direction direction;

    public ActionMove(final Direction direction) {
        super(Act.MOVE);
        this.direction = direction;
    }

    @Override
    public String toString() {
        return Act.MOVE.getToken() + " " + direction.getToken();
    }

    @Override
    public boolean isValid() {
        return super.isValid() && getAct().equals(Act.MOVE) && direction != null;
    }
}
