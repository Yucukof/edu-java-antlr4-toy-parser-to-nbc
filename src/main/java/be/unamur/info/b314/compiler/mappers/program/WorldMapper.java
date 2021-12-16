package be.unamur.info.b314.compiler.mappers.program;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.clauses.ClauseDefaultMapper;
import be.unamur.info.b314.compiler.mappers.declarations.DeclarationMapper;
import be.unamur.info.b314.compiler.mappers.declarations.FunctionMapper;
import be.unamur.info.b314.compiler.mappers.statements.StatementMapper;
import be.unamur.info.b314.compiler.nbc.Callable;
import be.unamur.info.b314.compiler.nbc.Structure;
import be.unamur.info.b314.compiler.nbc.declarations.Segment;
import be.unamur.info.b314.compiler.nbc.definitions.Definition;
import be.unamur.info.b314.compiler.nbc.program.Program;
import be.unamur.info.b314.compiler.nbc.routines.Subroutine;
import be.unamur.info.b314.compiler.nbc.routines.Thread;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.program.World;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static be.unamur.info.b314.compiler.mappers.NameFactory.NameCategory.DEFAULT;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
@Data
public class WorldMapper implements Function<World, Program> {

    private final Context context;

    @Override
    public Program apply(final World world) {

        Program program = Program.builder().build();

        // STEP 1 - traiter les déclarations
        if (world.hasDeclarations()) {
            // Traiter les variables si nécessaire
            if (world.hasVariables()) {
                final Segment segment = processVariables(world);
                program = program.toBuilder()
                      .block(segment)
                      .build();
            }
            // Traiter les fonctions si nécessaire
            if (world.hasFunctions()) {
                final List<Subroutine> functions = processFunctions(world);
                program = program.toBuilder()
                      .blocks(functions)
                      .build();
            }
        }

        // STEP 2 - Traiter les instructions libres

        // STEP 2.1 - Récupérer le mapper de statements
        final StatementMapper statementMapper = context.mappers.getStatementMapper();

        // STEP 2.2 - Convertir les statements en structures
        final List<Structure> structures = world.getStatements().stream()
              .map(statementMapper)
              .collect(Collectors.toList());

        // STEP 2.3 - Ajouter l'instruction de sortie vers DEFAULT
        final Callable destination = context.names.createNew(DEFAULT);
        final Structure call = StructureSimple.CALL(destination);

        // STEP 2.4 - Organiser les structures dans un Thread
        final Thread thread = Thread.builder()
              .identifier(new Identifier("World"))
              .structures(structures)
              .structure(call)
              .build();

        // STEP 2.5 - Ajouter le thread au programme
        program = program.toBuilder()
              .block(thread)
              .build();

        // STEP 3 - Traiter la clause default

        // STEP 3.1 - Récupérer le mapper de clauses default
        final ClauseDefaultMapper clauseDefaultMapper = context.mappers.getClauseDefaultMapper();

        // STEP 3.2 - Convertir la clause default en subroutine
        final Subroutine subroutine = clauseDefaultMapper.apply(world.getClauseDefault());

        // STEP 3.3 - Ajouter la subroutine au programme et retourner ce dernier
        return program.toBuilder()
              .block(subroutine)
              .build();
    }

    private Segment processVariables(final World world) {

        final DeclarationMapper declarationMapper = context.mappers.getDeclarationMapper();

        final List<Variable> variables = world.getVariableDeclarations();
        final List<Definition> definitions = variables.stream()
              .map(declarationMapper)
              .collect(Collectors.toList());

        IntStream.range(0, variables.size())
                .forEach(i -> variables.get(i).associate(definitions.get(i)));

        return Segment.builder()
              .definitions(definitions)
              .build();
    }

    private List<Subroutine> processFunctions(final World world) {

        final FunctionMapper functionMapper = context.mappers.getFunctionMapper();

        return world.getFunctionDeclarations().stream()
              .map(functionMapper)
              .collect(Collectors.toList());
    }
}
