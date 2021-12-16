package be.unamur.info.b314.compiler.pils.actions;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ActionIdle extends Action {

    @Override
    public String toString() {
        return "do nothing";
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
