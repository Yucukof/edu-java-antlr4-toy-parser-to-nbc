package be.unamur.info.b314.compiler.mappers.clauses;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.declarations.DeclarationMapper;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionMapper;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionPreprocessor;
import be.unamur.info.b314.compiler.mappers.statements.StatementMapper;
import be.unamur.info.b314.compiler.mappers.values.ValueMapper;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.Callable;
import be.unamur.info.b314.compiler.nbc.Statement;
import be.unamur.info.b314.compiler.nbc.Structure;
import be.unamur.info.b314.compiler.nbc.declarations.Segment;
import be.unamur.info.b314.compiler.nbc.definitions.Definition;
import be.unamur.info.b314.compiler.nbc.instructions.Instruction;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionControlTST;
import be.unamur.info.b314.compiler.nbc.program.Label;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.nbc.structures.StructureWhen;
import be.unamur.info.b314.compiler.nbc.symbols.Comparator;
import be.unamur.info.b314.compiler.pils.clauses.ClauseWhen;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import lombok.NonNull;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static be.unamur.info.b314.compiler.mappers.NameFactory.NameCategory.WHEN;

/**
 * @author Hadrien BAILLY
 */
public class ClauseWhenMapper implements Function<ClauseWhen, StructureWhen> {

    private final Context context;

    public ClauseWhenMapper(@NonNull final Context context) {
        this.context = context;
    }

    @Override
    public StructureWhen apply(@NonNull final ClauseWhen clauseWhen) {

        final Callable baseLabel = context.names.createNew(WHEN);
        final Label start = new Label("start_" + baseLabel.getName());
        final Label end = new Label("end_" + baseLabel.getName());
        final StructureSimple guardWithJump = getGuardWithJump(clauseWhen, end);
        final StructureSimple body = getBodyAsStructureSimple(clauseWhen);
        final Segment segment = getDefinitionsSegment(clauseWhen);

        // TODO: 29/04/2021: handle jumps when it is compared to 0 or 1
        return StructureWhen.builder()
              .start(start)
              .guard(guardWithJump)
              .segment(segment)
              .body(body)
              .end(end)
              .build();
    }

    private Segment getDefinitionsSegment(ClauseWhen clauseWhen) {
        // Récupérer les déclarations
        final List<Definition> definitions = getDeclarationsAsDefinitions(clauseWhen);

        // Créer un segment avec les definitions
        return Segment.builder()
                .definitions(definitions)
                .build();
    }

    private List<Definition> getDeclarationsAsDefinitions(ClauseWhen clauseWhen) {
        // Récupérer le mapper de statements
        final DeclarationMapper declarationMapper = context.mappers.getDeclarationMapper();

        final List<Variable> declarations = clauseWhen.getDeclarations();
        // Convertir les déclarations en definitions
        final List<Definition> definitions = declarations.stream()
                .map(declarationMapper)
                .collect(Collectors.toList());

        IntStream.range(0, declarations.size())
                .forEach(i -> declarations.get(i).associate(definitions.get(i)));

        return definitions;
    }

    private StructureSimple getGuardWithJump(ClauseWhen clauseWhen, Label end) {
        final ExpressionPreprocessor expressionPreprocessor = context.mappers.getExpressionPreprocessor();

        // Récupérer l'expression de garde
        final Expression expression = clauseWhen.getGuard();
        final Argument argument;
        final StructureSimple guard;

        if (!expression.isConstant()) {
            final ExpressionPreprocessor preprocessor = context.mappers.getExpressionPreprocessor();
            guard = preprocessor.apply(expression);
            final ExpressionMapper mapper = context.mappers.getExpressionMapper();
            argument = mapper.apply(expression);
        } else {
            final ValueMapper valueMapper = context.mappers.getValueMapper();
            argument = valueMapper.apply(expression.getValue());
            guard = StructureSimple.EMPTY();
        }

        // Construire l'instruction de saut
        final Instruction jmp = InstructionControlTST.builder()
                .comparator(Comparator.NEQ)
                .destination(end)
                .argument(argument)
                .build();

        return guard.toBuilder()
                .statement(jmp)
                .build();
    }

    private StructureSimple getBodyAsStructureSimple(ClauseWhen clauseWhen) {
        // Récupérer le mapper de statements
        final StatementMapper statementMapper = context.mappers.getStatementMapper();

        // Convertir toutes les instructions de la clause WHEN en Statements
        final List<Statement> statements = clauseWhen.getStatements().stream()
              .map(statementMapper)
              .map(Structure::getStatements)
              .flatMap(List::stream)
              .collect(Collectors.toList());

        // Initialiser la boucle
        return StructureSimple.builder()
                .statements(statements)
                .build();
    }
}
