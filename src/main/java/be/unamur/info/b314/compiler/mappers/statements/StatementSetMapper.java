package be.unamur.info.b314.compiler.mappers.statements;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionMapper;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionPreprocessor;
import be.unamur.info.b314.compiler.mappers.values.VariableReferenceMapper;
import be.unamur.info.b314.compiler.mappers.values.VariableReferencePreprocessor;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.instructions.Instruction;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionDataUnary;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.statements.StatementSet;
import be.unamur.info.b314.compiler.pils.values.references.FunctionCall;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

import static be.unamur.info.b314.compiler.nbc.keywords.Assignment.MOV;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
public class StatementSetMapper implements Function<StatementSet, StructureSimple> {

    private final Context context;

    public StatementSetMapper(@NonNull final Context context) {
        this.context = context;
    }

    @Override
    public StructureSimple apply(final StatementSet statement) {

            final VariableReference reference = statement.getReference();
            final Expression expression = statement.getExpression();

            log.trace("Processing statement SET [{}]", statement);
            StructureSimple structure = StructureSimple.EMPTY();

            // STEP 1 - Vérifier si la référence nécessite des calculs préparatoires.
            structure = preprocess(reference, structure);
            // STEP 2 - Récupérer la référence
            final VariableReferenceMapper referenceMapper = context.mappers.getVariableReferenceMapper();
            final Identifier identifier = referenceMapper.apply(reference);

            // STEP 3 - Vérifier si l'expression nécessite un pré-calcul.
            structure = preprocess(expression, structure);

            // STEP 3.2 - Récupérer le résultat de l'expression
            if (!expression.isConstant() && !expression.isLeaf()) {
                context.memory.getDisposable(expression.getType());
            }
            final ExpressionMapper expressionMapper = context.mappers.getExpressionMapper();
            final Argument argument = expressionMapper.apply(expression);

            // STEP 3.3 - Construire l'instruction.
            log.trace("Constructing {} Instruction", MOV);
            final Instruction instruction = InstructionDataUnary.builder()
                    .keyword(MOV)
                    .destination(identifier)
                    .argument(argument)
                    .build();
            log.trace("{}", instruction);


            // STEP 4 - Retourner la structure complète.
            return structure.toBuilder()
                    .statement(instruction)
                    .build();
    }

    private StructureSimple preprocess(final VariableReference reference, StructureSimple structure) {
        if (!reference.isConstant()) {
            final VariableReferencePreprocessor preprocessor = new VariableReferencePreprocessor(context);
            final StructureSimple struct = preprocessor.apply(reference);
            structure = structure.toBuilder()
                  .statements(struct.getStatements())
                  .build();
        }
        return structure;
    }

    private StructureSimple preprocess(final Expression expression, StructureSimple structure) {
        if (!expression.isConstant()) {
            final ExpressionPreprocessor preprocessor = context.mappers.getExpressionPreprocessor();
            final StructureSimple struct = preprocessor.apply(expression);
            structure = structure.toBuilder()
                  .statements(struct.getStatements())
                  .build();
        }
        return structure;
    }
}
