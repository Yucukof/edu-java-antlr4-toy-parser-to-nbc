package be.unamur.info.b314.compiler.nbc.declarations;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Hadrien BAILLY
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Macro extends Declaration {

    @Override
    public boolean isValid() {
        return false;
    }
}
