package be.unamur.info.b314.compiler.mappers.program;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.Environment;
import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.nbc.declarations.Segment;
import be.unamur.info.b314.compiler.nbc.program.Program;
import be.unamur.info.b314.compiler.pils.program.Strategy;
import be.unamur.info.b314.compiler.pils.program.World;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
@Data
public class ProgramMapper implements Function<be.unamur.info.b314.compiler.pils.program.Program, Program> {

    private final Context context;

    @Override
    public Program apply(final be.unamur.info.b314.compiler.pils.program.Program source) {

        log.info("Instanciating new program...");
        Program program = Program.builder().build();

        log.info("Reserving environment variable space");
        final Segment variables = Environment.EnvironmentVariable.getSegment();
        program = program.toBuilder()
              .block(variables)
              .build();

        log.info("Reserving register space...");
        final SpaceRequirement requirement = source.getSpaceRequirement();
        if (requirement.size() > 0) {
            context.memory.allocate(requirement);
            final Segment reservation = requirement.getSpaceReservation();
            program = program.toBuilder()
                  .block(reservation)
                  .build();
        }

        if (source instanceof Strategy) {
            final Program strategy = context.mappers.getStrategyMapper().apply((Strategy) source);

            return program.toBuilder()
                  .inclusions(strategy.getInclusions())
                  .blocks(strategy.getBlocks())
                  .build();
        }

        if (source instanceof World) {
            final Program world = context.mappers.getWorldMapper().apply((World) source);

            return program.toBuilder()
                  .inclusions(world.getInclusions())
                  .blocks(world.getBlocks())
                  .build();
        }

        throw new UnsupportedOperationException(String.format("Cannot map program with type [%s]", program.getClass()));
    }
}
