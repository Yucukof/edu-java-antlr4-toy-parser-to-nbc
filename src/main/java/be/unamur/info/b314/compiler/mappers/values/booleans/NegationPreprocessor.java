package be.unamur.info.b314.compiler.mappers.values.booleans;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionMapper;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionPreprocessor;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.instructions.Instruction;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionDataUnary;
import be.unamur.info.b314.compiler.nbc.keywords.Math;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.values.booleans.Negation;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 * @see NegationMapper
 */
@Slf4j
public class NegationPreprocessor implements Function<Negation, StructureSimple> {

    private final Context context;

    public NegationPreprocessor(final Context context) {
        this.context = context;
    }

    @Override
    public StructureSimple apply(final Negation negation) {

        log.trace("Preprocessing Negation [{}]", negation);
        StructureSimple structure = StructureSimple.EMPTY();

        final Expression expression = negation.getExpression();

        if (!expression.isPrimitive()) {
            final ExpressionPreprocessor preprocessor = context.mappers.getExpressionPreprocessor();
            final StructureSimple struct = preprocessor.apply(expression);
            structure = structure.toBuilder()
                  .statements(struct.getStatements())
                  .build();
        }

        final ExpressionMapper mapper = context.mappers.getExpressionMapper();
        final Argument argument = mapper.apply(expression);

        final Identifier destination = context.memory.getDisposable(expression.getType());

        log.trace("Construction NEG instruction");
        final Instruction instruction = InstructionDataUnary.builder()
              .destination(destination)
              .keyword(Math.NEG)
              .argument(argument)
              .build();
        log.trace("{}", instruction);

        structure = structure.toBuilder()
              .statement(instruction)
              .build();

        return structure;
    }
}
