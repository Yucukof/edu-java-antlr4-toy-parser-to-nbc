package be.unamur.info.b314.compiler.nbc.program;

import be.unamur.info.b314.compiler.nbc.Callable;
import be.unamur.info.b314.compiler.nbc.Statement;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import lombok.Builder;
import lombok.Data;

/**
 * @author Hadrien BAILLY
 */
@Data
@Builder
public class Label implements Statement, Callable {

    private final Identifier name;

    public Label(final Identifier name) {
        this.name = name;
    }

    public Label(final String name) {
        this.name = new Identifier(name);
    }

    @Override
    public String toString() {
        return "\n" + name + ":";
    }

    @Override
    public boolean isValid() {
        return name != null && name.isValid();
    }

    public String getName() {
        return name.getName();
    }

}
