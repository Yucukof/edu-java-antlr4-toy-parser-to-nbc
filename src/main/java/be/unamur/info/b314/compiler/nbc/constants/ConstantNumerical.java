package be.unamur.info.b314.compiler.nbc.constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Numerical constants may be written in either decimal or hexadecimal form. Decimal
 * constants consist of one or more decimal digits. Decimal constants may optionally
 * include a decimal point along with one or more decimal digits following the decimal
 * point. Hexadecimal constants start with 0x or 0X followed by one or more hexadecimal
 * digits.
 *
 * @author Hadrien BAILLY
 */
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConstantNumerical extends Constant {

    private final int value;

    public static ConstantNumerical TRUE() {
        return ONE();
    }

    public static ConstantNumerical ONE() {
        return ConstantNumerical.builder()
              .value(1)
              .build();
    }

    public static ConstantNumerical FALSE() {
        return ZERO();
    }

    public static ConstantNumerical ZERO() {
        return ConstantNumerical.builder()
              .value(0)
              .build();
    }

    @Override
    public String getString() {
        return String.valueOf(value);
    }

    @Override
    public Integer getInteger() {
        return value;
    }

    @Override
    public String getToken() {
        return toString();
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
