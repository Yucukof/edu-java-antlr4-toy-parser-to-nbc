package be.unamur.info.b314.compiler.nbc.program;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class IncludeTest {

    @Test
    public void given_valid_include_when_toString_then_expect_include(){
        final Include include = Include.builder()
              .filename("test.h")
              .build();

        assertThat(include.toString()).isEqualTo("#include \"test.h\"");
    }
}