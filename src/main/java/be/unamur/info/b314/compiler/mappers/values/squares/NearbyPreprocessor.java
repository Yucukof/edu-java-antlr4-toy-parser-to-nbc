package be.unamur.info.b314.compiler.mappers.values.squares;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionMapper;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionPreprocessor;
import be.unamur.info.b314.compiler.nbc.constants.ConstantNumerical;
import be.unamur.info.b314.compiler.nbc.instructions.Instruction;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionDataBinary;
import be.unamur.info.b314.compiler.nbc.keywords.Math;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.values.squares.Nearby;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 * @see NearbyMapper
 */
@Slf4j
public class NearbyPreprocessor implements Function<Nearby, StructureSimple> {

    private final Context context;

    public NearbyPreprocessor(final Context context) {
        this.context = context;
    }

    @Override
    public StructureSimple apply(final Nearby nearby) {

        log.trace("Preprocessing Nearby...");
        StructureSimple structure = StructureSimple.EMPTY();

        // STEP 1 - Pré-traiter les expressions si nécessaire
        final Expression left = nearby.getCoordinates().getLeft();
        final StructureSimple structureLeft = process(left);
        structure = structure.toBuilder()
              .statements(structureLeft.getStatements())
              .build();

        if (!left.isConstant()){
            context.memory.reserveAndGet(left.getType());
        }

        final Expression right = nearby.getCoordinates().getRight();
        final StructureSimple structureRight = process(right);
        structure = structure.toBuilder()
              .statements(structureRight.getStatements())
              .build();

        // STEP 2 - Libérer les registres réservés
        if (!left.isConstant()) {
            context.memory.release(left.getType());
        }

        return structure;
    }

    private StructureSimple process(final Expression expression) {
        if (!expression.isPrimitive()) {
            final ExpressionPreprocessor preprocessor = context.mappers.getExpressionPreprocessor();

            final StructureSimple statements = preprocessor.apply(expression);
            log.trace("Reserving register...");
            final ExpressionMapper mapper = context.mappers.getExpressionMapper();
            final Identifier identifier = (Identifier) mapper.apply(expression);

            log.trace("Shifting index...");
            final Instruction instruction = InstructionDataBinary.builder()
                  .keyword(Math.SUB)
                  .destination(identifier)
                  .argument1(identifier)
                  .argument2(ConstantNumerical.ONE())
                  .build();
            log.trace("{}", instruction);

            return StructureSimple.builder()
                  .statements(statements.getStatements())
                  .statement(instruction)
                  .build();
        }
        return StructureSimple.EMPTY();
    }
}
