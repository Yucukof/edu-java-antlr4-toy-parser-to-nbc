package be.unamur.info.b314.compiler.nbc.instructions;

import be.unamur.info.b314.compiler.nbc.keywords.Command;
import be.unamur.info.b314.compiler.nbc.keywords.Keyword;
import be.unamur.info.b314.compiler.nbc.keywords.LcdLine;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Anthony DI STASIO
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class InstructionTextOut extends InstructionAction {
    private final Keyword keyword = Command.TEXT_OUT;
    private final LcdLine line;
    private final String text;

    @Override
    public String toString() {
        return keyword.getToken() + "(0, " + line.getToken() + ", \"" + text + "\")";
    }

    @Override
    public boolean isValid() {
        return line != null && text != null;
    }
}