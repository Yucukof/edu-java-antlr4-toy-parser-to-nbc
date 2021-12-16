package be.unamur.info.b314.compiler.nbc.constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * String constants in NBC are delimited with either single or double quote characters.
 * NBC represents a string as an array of bytes, with the last byte in the array being a
 * zero. The final zero byte is generally referred to as the null terminator.
 *
 * @author Hadrien BAILLY
 */
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConstantString extends Constant {

    private final String value;

    @Override
    public String getString() {
        return value;
    }

    @Override
    public Integer getInteger() {
        throw new IllegalArgumentException("Cannot return integers value for String constant.");
    }

    @Override
    public String getToken() {
        return toString();
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotEmpty(value);
    }

    @Override
    public String toString() {
        return "'"
              + value
              .replace("\\", "\\\\")
              .replace("'", "\\'")
              + "'";
    }
}
