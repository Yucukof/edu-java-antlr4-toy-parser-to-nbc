package be.unamur.info.b314.compiler.nbc.instructions;

import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.Statement;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public abstract class InstructionData extends Instruction implements Statement {

    /**
     * L'identifiant indiquant l'endroit où le résultat de l'opération
     */
    // TODO 19/04/2021 [HBA]: Gérer les destinations avec indices
    private final Identifier destination;

    @Override
    public boolean isValid() {
        return super.isValid()
              && destination != null && destination.isValid();
    }
}
