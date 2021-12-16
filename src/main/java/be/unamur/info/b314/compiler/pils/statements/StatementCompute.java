package be.unamur.info.b314.compiler.pils.statements;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.values.Value;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
@Data
@Builder
public class StatementCompute implements Statement {

    /**
     * L'expression à résoudre durant l'opération compute et dont le résultat est ignoré.
     */
    private final Expression expression;

    @Override
    public String toString() {
        return "compute " + expression;
    }

    @Override
    public boolean isValid() {
        return expression != null && expression.isValid();
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        return expression.getSpaceRequirement();
    }

    @Override
    public Optional<StatementNext> run() {
        log.trace("Running [{}]", this);
        final Value value = expression.getValue();

        if (value.isBoolean()) {
            value.getBoolValue();
        }
        if (value.isInteger()) {
            value.getIntValue();
        }
        if (value.isSquare()) {
            value.getSquareValue();
        }

        return Optional.empty();
    }
}
