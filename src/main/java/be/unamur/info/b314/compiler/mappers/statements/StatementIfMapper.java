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
import be.unamur.info.b314.compiler.nbc.structures.StructureIf;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.nbc.symbols.Comparator;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.statements.StatementIf;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static be.unamur.info.b314.compiler.mappers.NameFactory.NameCategory.IF;

/**
 * @author Hadrien BAILLY, Anthony DI STASIO
 */
@Slf4j
public class StatementIfMapper implements Function<StatementIf, StructureIf> {

    private final Context context;

    public StatementIfMapper(@NonNull final Context context) {
        this.context = context;
    }

    @Override
    public StructureIf apply(@NonNull final StatementIf statementIf) {
        log.trace("Processing Statement IF");

        final Callable baseLabel = context.names.createNew(IF);
        final Label elseLabel = new Label("else_" + baseLabel.getName());
        final Label endLabel = new Label("end_" + baseLabel.getName());
        final StructureSimple guardWithJump = getGuardWithJump(statementIf, elseLabel);
        final StructureSimple branchTrueStatements = getMappedStatementsAsStructureSimple(statementIf.getBranchTrue());
        final StructureSimple branchFalseStatements = getMappedStatementsAsStructureSimple(statementIf.getBranchFalse());
        final InstructionControlJMP jumpToEnd = InstructionControlJMP.to(endLabel);

        // TODO: 29/04/2021: handle jumps when it is compared to 0 or 1
        return StructureIf.builder()
                .guard(guardWithJump)
                .branchTrue(branchTrueStatements)
                .jumpToEnd(jumpToEnd)
                .elseLabel(elseLabel)
                .branchFalse(branchFalseStatements)
                .endLabel(endLabel)
                .build();
    }

    private StructureSimple getGuardWithJump(StatementIf statementIf, Label end) {

        // Récupérer l'expression de garde
        final Expression expression = statementIf.getGuard();
        final Argument argument;
        final StructureSimple guard;

        if (!expression.isConstant()) {
            final ExpressionPreprocessor expressionPreprocessor = context.mappers.getExpressionPreprocessor();
            guard = expressionPreprocessor.apply(expression);
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

    private StructureSimple getMappedStatementsAsStructureSimple(List<be.unamur.info.b314.compiler.pils.statements.Statement> statements) {
        // Récupérer le mapper de statements
        final StatementMapper statementMapper = context.mappers.getStatementMapper();

        // Convertir toutes les instructions de la branch false du IF en Statements
        final List<Statement> mappedStatements = statements.stream()
              .map(statementMapper)
              .map(Structure::getStatements)
              .flatMap(List::stream)
              .collect(Collectors.toList());

        // Initialiser la boucle
        return StructureSimple.builder()
                .statements(mappedStatements)
                .build();
    }

}
