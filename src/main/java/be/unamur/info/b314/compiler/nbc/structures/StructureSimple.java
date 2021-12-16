package be.unamur.info.b314.compiler.nbc.structures;

import be.unamur.info.b314.compiler.nbc.Callable;
import be.unamur.info.b314.compiler.nbc.Statement;
import be.unamur.info.b314.compiler.nbc.Structure;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionControl;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionControlJMP;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static be.unamur.info.b314.compiler.nbc.keywords.Scheduling.CALL;

/**
 * @author Hadrien BAILLY
 */
@Data
@SuperBuilder(toBuilder = true)
public class StructureSimple implements Structure {

    @Singular
    private final List<Statement> statements;

    public static StructureSimple EMPTY() {
        return StructureSimple.builder().build();
    }

    public static StructureSimple JUMP(final Callable destination) {

        // Créer l'instruction de saut
        final InstructionControlJMP jmp = InstructionControlJMP.builder()
              .destination(destination)
              .build();

        // Créer la structure simple contenant l'instruction de saut
        return StructureSimple.builder()
              .statement(jmp)
              .build();
    }

    public static StructureSimple CALL(final Callable destination) {

        // Créer l'instruction d'appel
        final InstructionControl call = InstructionControl.builder()
              .keyword(CALL)
              .destination(destination)
              .build();

        // Créer la structure simple contenant l'instruction d'appel
        return StructureSimple.builder()
              .statement(call)
              .build();
    }

    public void concat(final StructureSimple simple) {
        this.statements.addAll(simple.getStatements());
    }

    public void append(final Statement statement) {
        this.statements.add(statement);
    }

    @Override
    public String toString() {
        return getStatements().stream().map(Statement::toString).collect(Collectors.joining("\n"));
    }

    @Override
    public boolean isValid() {
        return statements.stream().allMatch(Statement::isValid);
    }
}
