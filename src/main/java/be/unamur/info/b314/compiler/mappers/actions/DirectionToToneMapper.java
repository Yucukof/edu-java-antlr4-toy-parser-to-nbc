package be.unamur.info.b314.compiler.mappers.actions;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.keywords.Tone;
import be.unamur.info.b314.compiler.pils.exceptions.DirectionNotFoundException;
import be.unamur.info.b314.compiler.pils.keywords.Direction;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author Anthony DI STASIO
 */
@Slf4j
public class DirectionToToneMapper implements Function<Direction, Tone> {
    final Context context;

    public DirectionToToneMapper(final Context context) {
        this.context = context;
    }

    @Override
    public Tone apply(final Direction direction) {
        log.trace("Processing direction");

        return this.getTone(direction);
    }

    private Tone getTone(final Direction direction) {
        switch (direction) {
            case NORTH:
                return Tone.TONE_A3;
            case SOUTH:
                return Tone.TONE_B3;
            case EAST:
                return Tone.TONE_C3;
            case WEST:
                return Tone.TONE_D3;
            default:
                throw new DirectionNotFoundException(String.format("Cannot process direction [%s]", direction));
        }
    }
}
