package be.unamur.info.b314.compiler.pils.statements;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.actions.Action;
import be.unamur.info.b314.compiler.pils.actions.ActionIdle;
import be.unamur.info.b314.compiler.visitors.StatementVisitor;
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
public class StatementNext implements Statement {

    public static final StatementNext DO_NOTHING = StatementNext.builder()
          .action(ActionIdle.builder().build())
          .build();

    private final Action action;

    public static StatementNext from(final GrammarParser.NextInstrContext ctx, final Context context) {
        return (StatementNext) new StatementVisitor(context).visitNextInstr(ctx);
    }

    @Override
    public String toString() {
        return "next " + action;
    }

    public boolean isValid() {
        return action != null && action.isValid();
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        return SpaceRequirement.NONE;
    }

    @Override
    public Optional<StatementNext> run() {
        log.trace("Running [{}]", this);
        return Optional.of(this);
    }
}
