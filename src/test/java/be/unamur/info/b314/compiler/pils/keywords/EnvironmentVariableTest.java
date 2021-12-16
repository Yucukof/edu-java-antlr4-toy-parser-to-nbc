package be.unamur.info.b314.compiler.pils.keywords;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class EnvironmentVariableTest {

    @Test
    public void isReserved() {
        assertThat(Reserved.isReserved("TEST")).isFalse();
        assertThat(Reserved.isReserved("ARENA")).isTrue();
    }
}