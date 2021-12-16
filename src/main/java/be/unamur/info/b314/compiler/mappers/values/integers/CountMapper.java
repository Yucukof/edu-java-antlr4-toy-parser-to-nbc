package be.unamur.info.b314.compiler.mappers.values.integers;

import be.unamur.info.b314.compiler.mappers.Environment;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.values.integers.Count;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
public class CountMapper implements Function<Count, Identifier> {

    @Override
    public Identifier apply(final Count count) {

        log.trace("Processing Count [{}]", count);
        switch (count.getItem()) {
            case MAP:
                return Environment.ItemVariable.MAP.getIdentifier();
            case AMMO:
                return Environment.ItemVariable.AMMO.getIdentifier();
            case SODA:
                return Environment.ItemVariable.SODA.getIdentifier();
            case FRUITS:
                return Environment.ItemVariable.FRUITS.getIdentifier();
            case RADIO:
                return Environment.ItemVariable.RADIO.getIdentifier();
            case RADAR:
                return Environment.ItemVariable.RADAR.getIdentifier();
            default:
                throw new RuntimeException("Should not happen");
        }
    }
}
