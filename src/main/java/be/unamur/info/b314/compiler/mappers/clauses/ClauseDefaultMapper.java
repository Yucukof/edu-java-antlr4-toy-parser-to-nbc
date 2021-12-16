package be.unamur.info.b314.compiler.mappers.clauses;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.declarations.DeclarationMapper;
import be.unamur.info.b314.compiler.mappers.statements.StatementMapper;
import be.unamur.info.b314.compiler.nbc.Structure;
import be.unamur.info.b314.compiler.nbc.declarations.Segment;
import be.unamur.info.b314.compiler.nbc.definitions.Definition;
import be.unamur.info.b314.compiler.nbc.routines.Subroutine;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.clauses.ClauseDefault;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import lombok.NonNull;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static be.unamur.info.b314.compiler.mappers.NameFactory.NameCategory.DEFAULT;

/**
 * @author Hadrien BAILLY
 */
public class ClauseDefaultMapper implements Function<ClauseDefault, Subroutine> {

    private final Context context;

    public ClauseDefaultMapper(@NonNull final Context context) {
        this.context = context;
    }

    @Override
    public Subroutine apply(@NonNull final ClauseDefault clauseDefault) {

        context.names.resetLocal();
        Subroutine routine = Subroutine.builder().build();

        final Identifier identifier = (Identifier) context.names.getCurrent(DEFAULT);
        routine = routine.toBuilder()
              .identifier(identifier)
              .build();

        if (clauseDefault.hasDeclarations()) {
            final Segment segment = getDefinitionsSegment(clauseDefault);
            routine = routine.toBuilder()
                  .segment(segment)
                  .build();
        }
        final List<Structure> structures = getStatementsAsStructure(clauseDefault);
        routine = routine.toBuilder()
              .structures(structures)
              .build();

        return routine;
    }

    private Segment getDefinitionsSegment(ClauseDefault clauseDefault) {
        // Récupérer le mapper de déclarations
        final DeclarationMapper declarationMapper = context.mappers.getDeclarationMapper();

        final List<Variable> declarations = clauseDefault.getDeclarations();
        // Convertir les déclarations en definitions
        final List<Definition> definitions = declarations.stream()
              .map(declarationMapper)
              .collect(Collectors.toList());

        IntStream.range(0, declarations.size())
                .forEach(i -> declarations.get(i).associate(definitions.get(i)));

        // Créer un segment avec les definitions
        return Segment.builder()
              .definitions(definitions)
              .build();
    }

    private List<Structure> getStatementsAsStructure(ClauseDefault clauseDefault) {
        // Récupérer le mapper de statements
        final StatementMapper statementMapper = context.mappers.getStatementMapper();

        // Convertir les statements en structures
        return clauseDefault.getStatements().stream()
              .map(statementMapper)
              .collect(Collectors.toList());
    }
}
