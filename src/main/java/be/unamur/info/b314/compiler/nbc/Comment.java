package be.unamur.info.b314.compiler.nbc;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Hadrien BAILLY
 */
@Data
public class Comment implements Statement {

    private final String text;

    @Override
    public String toString() {
        return text;
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotEmpty(text);
    }
}
