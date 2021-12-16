package be.unamur.info.b314.compiler.pils.statements;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
@Data
@Builder
public class StatementSet implements Statement {

    /**
     * La variable à assigner.
     */
    private final VariableReference reference;
    /**
     * L'expression à évaluer.
     */
    private final Expression expression;



    @Override
    public String toString() {
        return "set " + reference.toString() + " to " + expression.toString();
    }

    @Override
    public boolean isValid() {
        return reference != null && reference.isValid()
              && expression != null && expression.isValid() && expression.getType().equals(reference.getType());
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        final SpaceRequirement referenceRequirement = reference.getSpaceRequirement();
        final SpaceRequirement expressionRequirement = expression.getSpaceRequirement();
        return SpaceRequirement.combine(referenceRequirement, expressionRequirement);
    }

    @Override
    public Optional<StatementNext> run() {
        log.trace("Running [{}]", this);
        final Variable variable = reference.getVariable();
        final List<Expression> indexes = reference.getIndexes();

        variable.setAt(expression.getValue(), indexes);
        return Optional.empty();
    }
}
