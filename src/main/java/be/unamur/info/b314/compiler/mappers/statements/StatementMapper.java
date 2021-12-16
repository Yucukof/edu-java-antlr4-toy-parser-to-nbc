package be.unamur.info.b314.compiler.mappers.statements;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.Structure;
import be.unamur.info.b314.compiler.pils.statements.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
public class StatementMapper implements Function<Statement, Structure> {

    private final Context context;

    public StatementMapper(@NonNull final Context context) {
        this.context = context;
    }

    @Override
    public Structure apply(@NonNull final Statement statement) {

        log.trace("Processing statement switch");

        if (statement instanceof StatementIf) {
            final StatementIfMapper mapper = context.mappers.getStatementIfMapper();
            return mapper.apply((StatementIf) statement);
        }
        if (statement instanceof StatementCompute) {
            final StatementComputeMapper mapper = context.mappers.getStatementComputeMapper();
            return mapper.apply((StatementCompute) statement);
        }
        if (statement instanceof StatementSet) {
            final StatementSetMapper mapper = context.mappers.getStatementSetMapper();
            return mapper.apply((StatementSet) statement);
        }
        if (statement instanceof StatementSkip) {
            return Structure.EMPTY();
        }
        if (statement instanceof StatementWhile) {
            final StatementWhileMapper mapper = context.mappers.getStatementWhileMapper();
            return mapper.apply((StatementWhile) statement);
        }
        if (statement instanceof StatementNext) {
            final StatementNextMapper mapper = context.mappers.getStatementNextMapper();
            return mapper.apply((StatementNext) statement);
        }
        throw new UnsupportedOperationException();
    }
}
