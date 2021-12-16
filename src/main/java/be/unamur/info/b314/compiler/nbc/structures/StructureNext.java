package be.unamur.info.b314.compiler.nbc.structures;

import be.unamur.info.b314.compiler.nbc.Statement;
import be.unamur.info.b314.compiler.nbc.Structure;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * @author Hadrien BAILLY, Anthony DI STASIO
 */
@Data
@NoArgsConstructor
public class StructureNext implements Structure {
    public static StructureNext EMPTY() {
        return new StructureNext();
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public List<Statement> getStatements() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "";
    }
}