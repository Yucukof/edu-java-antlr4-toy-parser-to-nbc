package be.unamur.info.b314.compiler.mappers.imports;

import be.unamur.info.b314.compiler.nbc.program.Include;
import be.unamur.info.b314.compiler.pils.imports.Import;
import lombok.NonNull;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 */
public class ImportMapper implements Function<Import, Include> {

    @Override
    public Include apply(@NonNull final Import i) {

        return Include.builder()
              .filename(i.getFilename())
              .build();
    }
}
