package be.unamur.info.b314.compiler.mappers.imports;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.program.Include;
import be.unamur.info.b314.compiler.pils.imports.Import;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class ImportMapperTest {

    private ImportMapper mapper;

    @Before
    public void before(){
        mapper = new Context().mappers.getImportMapper();
    }

    @Test
    public void given_valid_import_when_mapper_apply_then_expect_include(){

        final Import i = Import.builder()
              .filename("test")
              .build();
        assertThat(i.isValid()).isTrue();

        final Include include = mapper.apply(i);
        assertThat(include).isNotNull();
        assertThat(include.isValid()).isTrue();
        assertThat(include.toString()).isEqualTo("#include \"test\"");
    }

}