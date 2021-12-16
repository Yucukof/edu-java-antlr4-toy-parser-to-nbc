package be.unamur.info.b314.compiler.mappers.program;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.clauses.ClauseDefaultMapper;
import be.unamur.info.b314.compiler.mappers.clauses.ClauseWhenMapper;
import be.unamur.info.b314.compiler.mappers.declarations.DeclarationMapper;
import be.unamur.info.b314.compiler.mappers.declarations.FunctionMapper;
import be.unamur.info.b314.compiler.mappers.imports.ImportMapper;
import be.unamur.info.b314.compiler.nbc.Callable;
import be.unamur.info.b314.compiler.nbc.Structure;
import be.unamur.info.b314.compiler.nbc.declarations.Segment;
import be.unamur.info.b314.compiler.nbc.definitions.Definition;
import be.unamur.info.b314.compiler.nbc.program.Include;
import be.unamur.info.b314.compiler.nbc.program.Program;
import be.unamur.info.b314.compiler.nbc.routines.Subroutine;
import be.unamur.info.b314.compiler.nbc.routines.Thread;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.nbc.structures.StructureWhen;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.program.Strategy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static be.unamur.info.b314.compiler.mappers.NameFactory.NameCategory.DEFAULT;

/**
 * @author Hadrien BAILLY, Anthony DI STASIO
 */
@Slf4j
@Data
public class StrategyMapper implements Function<Strategy, Program> {

    private final Context context;

    @Override
    public Program apply(final Strategy strategy) {
        Program program = Program.builder().build();

        // STEP 1 - Traiter l'import
        // TODO 04/05/2021 [HBA]: Puisqu'on compile nous-même le fichier world,
        //  il n'est pas nécessaire de convertir l'import

        // STEP 2  Traiter les déclarations
        if (strategy.hasDeclarations()) {
            // Traiter les variables
            if (strategy.hasVariables()) {
                program = program.toBuilder()
                        .block(getDefinitionsSegment(strategy))
                        .build();
            }

            // Traiter les fonctions
            if (strategy.hasFunctions()) {
                program = program.toBuilder()
                        .blocks(getSubroutines(strategy))
                        .build();
            }
        }

        // STEP 3 - Traiter les clauses when
        Thread main = Thread.main();
        if (strategy.hasClausesWhen()) {
            // Ajouter le thread au programme
            main = getClausesWhenThread(strategy);
        }
        // STEP 2.3 - Ajouter l'instruction de sortie vers DEFAULT
        final Callable destination = context.names.createNew(DEFAULT);
        final Structure call = StructureSimple.CALL(destination);

        main = main.toBuilder()
                .structure(call)
                .build();

        program = program.toBuilder()
                .block(main)
                .build();

        // STEP 4 - Traiter la clause default
        return program.toBuilder()
                .block(getClauseDefaultSubroutine(strategy))
                .build();
    }

    private Segment getDefinitionsSegment(final Strategy strategy) {
        // Récupérer le mapper de declarations
        final DeclarationMapper declarationMapper = context.mappers.getDeclarationMapper();

        // Convertir les déclarations de variables en definitions
        final List<Variable> variables = strategy.getVariableDeclarations();
        final List<Definition> definitions = variables.stream()
                .map(declarationMapper)
                .collect(Collectors.toList());

        IntStream.range(0, variables.size())
                .forEach(i -> variables.get(i).associate(definitions.get(i)));

        // Créer un segment contenant les definitions
        return Segment.builder()
                .definitions(definitions)
                .build();
    }

    private List<Subroutine> getSubroutines(final Strategy strategy) {
        // Récupérer le mapper de fonctions
        final FunctionMapper functionMapper = context.mappers.getFunctionMapper();

        // Convertir les fonctions en subroutines
        return strategy.getFunctionDeclarations().stream()
                .map(functionMapper)
                .collect(Collectors.toList());
    }

    private Thread getClausesWhenThread(Strategy strategy) {
        // Récupérer le mapper de clause when
        final ClauseWhenMapper clauseWhenMapper = context.mappers.getClauseWhenMapper();

        // Convertir la clause default en subroutine
        final List<StructureWhen> structuresWhen = strategy.getClauses().stream()
                .map(clauseWhenMapper)
                .collect(Collectors.toList());

        // Organiser les clauses when dans le thread main
        return Thread.main().toBuilder()
                .structures(structuresWhen)
                .build();
    }

    private Subroutine getClauseDefaultSubroutine(Strategy strategy) {
        // Récupérer le mapper de clauses default
        final ClauseDefaultMapper clauseDefaultMapper = context.mappers.getClauseDefaultMapper();

        // Convertir la clause default en subroutine
        return clauseDefaultMapper.apply(strategy.getClauseDefault());
    }

    private Include getInclude(Strategy strategy) {
        // Récupérer le mapper d'import
        final ImportMapper importMapper = context.mappers.getImportMapper();

        // Convertir l'import en include
        return importMapper.apply(strategy.getWorld());
    }

}
