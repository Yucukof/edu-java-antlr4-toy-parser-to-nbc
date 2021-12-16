package be.unamur.info.b314.compiler.mappers.statements;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionPreprocessor;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.pils.statements.StatementCompute;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
public class StatementComputeMapper implements Function<StatementCompute, StructureSimple> {

    private final Context context;

    public StatementComputeMapper(@NonNull final Context context) {
        this.context = context;
    }

    @Override
    public StructureSimple apply(@NonNull final StatementCompute statement) {

        log.trace("Processing Statement COMPUTE [{}]", statement);
        final ExpressionPreprocessor expressionPreprocessor = context.mappers.getExpressionPreprocessor();
        return expressionPreprocessor.apply(statement.getExpression());
    }
}
