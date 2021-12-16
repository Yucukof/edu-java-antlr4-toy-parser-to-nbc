package be.unamur.info.b314.compiler.nbc.structures;

import be.unamur.info.b314.compiler.nbc.Statement;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionTextOut;
import edu.emory.mathcs.backport.java.util.Collections;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Une structure Display est une structure de code permettant l'affichage d'un élément à l'écran.
 *
 * @author Anthony DI STASIO
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class StructureDisplay extends StructureNext {

    /**
     * L'instruction faisant afficher un texte à l'écran.
     */
    private final InstructionTextOut textOut;

    @Override
    public boolean isValid() {
        return textOut != null && textOut.isValid();
    }

    @Override
    public List<Statement> getStatements() {
        //noinspection unchecked
        return Collections.singletonList(textOut);
    }

    @Override
    public String toString() {
        return textOut.toString();
    }
}