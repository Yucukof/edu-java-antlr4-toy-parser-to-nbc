package be.unamur.info.b314.compiler.mappers.statements;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionMapper;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionPreprocessor;
import be.unamur.info.b314.compiler.mappers.values.ValueMapper;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.Callable;
import be.unamur.info.b314.compiler.nbc.Statement;
import be.unamur.info.b314.compiler.nbc.Structure;
import be.unamur.info.b314.compiler.nbc.instructions.Instruction;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionControlJMP;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionControlTST;
import be.unamur.info.b314.compiler.nbc.program.Label;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.nbc.structures.StructureWhile;
import be.unamur.info.b314.compiler.nbc.symbols.Comparator;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.statements.StatementWhile;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static be.unamur.info.b314.compiler.mappers.NameFactory.NameCategory.WHILE;

/**
 * @author Anthony DI STASIO
 */
@Slf4j
public class StatementWhileMapper implements Function<StatementWhile, StructureWhile> {

    private final Context context;

    public StatementWhileMapper(@NonNull final Context context) {
        this.context = context;
    }

    @Override
    public StructureWhile apply(final StatementWhile statement) {

        log.trace("Processing statement WHILE");
        final Callable baseLabel = context.names.createNew(WHILE);
        final Label start = new Label("start_" + baseLabel.getName());
        final Label end = new Label("end_" + baseLabel.getName());
        final StructureSimple guardWithJump = getGuardWithJump(statement, end);
        final StructureSimple body = getBodyAsStructureSimple(statement);
        final InstructionControlJMP jumpToEndIfFalse = InstructionControlJMP.to(end);
        final InstructionControlJMP jumpToStartIfTrue = InstructionControlJMP.to(start);

        // TODO: 29/04/2021: handle jumps when it is compared to 0 or 1
        return StructureWhile.builder()
                .start(start)
                .jumpToEndIfFalse(jumpToEndIfFalse)
                .guard(guardWithJump)
                .body(body)
                .jumpToStartIfTrue(jumpToStartIfTrue)
                .end(end)
                .build();
    }

    private StructureSimple getGuardWithJump(final StatementWhile statementWhile, final Label end) {

        // Récupérer l'expression de garde
        final Expression expression = statementWhile.getGuard();
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

    private StructureSimple getBodyAsStructureSimple(final StatementWhile statementWhile) {
        // Récupérer le mapper de statements
        final StatementMapper statementMapper = context.mappers.getStatementMapper();

        // Convertir toutes les instructions de la clause WHILE en Statements
        final List<Statement> statements = statementWhile.getStatements().stream()
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
