package be.unamur.info.b314.compiler.nbc.definitions;

import be.unamur.info.b314.compiler.nbc.constants.Constant;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The NXT firmware supports several different types of variables which are grouped into
 * two categories: scalar types and aggregate types. Scalar types are a single integers value
 * which may be signed or unsigned and occupy one, two, or four bytes of memory.
 * <br>
 * Aggregate variables are either structures or arrays of some other type (either scalar
 * or aggregate).
 *
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class DefinitionVariable extends Definition {

    /**
     * The type name must be a type or type
     * alias already known by the compiler.
     */
    private final Identifier typeName;
    @Builder.Default
    private final List<Integer> dimensions = new ArrayList<>();
    /**
     * Byte arrays may be initialized either by using braces containing a list of numeric values
     * ({val1, val2, ..., valN}) or by using a string constant delimited with single-quote
     * characters (’Testing’).
     */
    private final Constant value;

    @Override
    public String toString() {
        return getIdentifier() + " "
              + typeName + StringUtils.repeat("[]", dimensions.size())
              + (value != null ? " = " + value.getToken() : "");
    }

    public boolean isValid() {

        return super.isValid()
              && typeName != null && (typeName.isValid() || typeName.isReservedKeyword())
              && dimensions != null;
    }
}
