package be.unamur.info.b314.compiler.mappers.statements;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.actions.ActionMoveMapper;
import be.unamur.info.b314.compiler.mappers.actions.ActionShootMapper;
import be.unamur.info.b314.compiler.mappers.actions.ActionUseMapper;
import be.unamur.info.b314.compiler.nbc.structures.StructureNext;
import be.unamur.info.b314.compiler.pils.actions.ActionIdle;
import be.unamur.info.b314.compiler.pils.actions.ActionMove;
import be.unamur.info.b314.compiler.pils.actions.ActionShoot;
import be.unamur.info.b314.compiler.pils.actions.ActionUse;
import be.unamur.info.b314.compiler.pils.statements.StatementNext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author Anthony DI STASIO
 */
@Slf4j
public class StatementNextMapper implements Function<StatementNext, StructureNext> {

    private final Context context;

    public StatementNextMapper(@NonNull final Context context) {
        this.context = context;
    }

    @Override
    public StructureNext apply(final StatementNext statement) {
        log.trace("Processing statement NEXT");

        if (statement.getAction() instanceof ActionShoot) {
            final ActionShootMapper mapper = context.mappers.getActionShootMapper();

            return mapper.apply((ActionShoot) statement.getAction());
        }

        if (statement.getAction() instanceof ActionMove) {
            final ActionMoveMapper mapper = context.mappers.getActionMoveMapper();

            return mapper.apply((ActionMove) statement.getAction());
        }

        if (statement.getAction() instanceof ActionUse) {
            final ActionUseMapper mapper = context.mappers.getActionUseMapper();

            return mapper.apply((ActionUse) statement.getAction());
        }

        if (statement.getAction() instanceof ActionIdle) {
            return StructureNext.EMPTY();
        }

        throw new IllegalStateException("Unexpected value: " + statement.getAction().getAct().getToken());
    }
}
