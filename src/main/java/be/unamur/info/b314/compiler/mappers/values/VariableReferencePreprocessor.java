package be.unamur.info.b314.compiler.mappers.values;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionPreprocessor;
import be.unamur.info.b314.compiler.nbc.Statement;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
public class VariableReferencePreprocessor implements Function<VariableReference, StructureSimple> {

    private final Context context;

    public VariableReferencePreprocessor(final Context context) {
        this.context = context;
    }

    @Override
    public StructureSimple apply(@NonNull final VariableReference reference) {

        if (reference.isPrimitive()) {
            return StructureSimple.EMPTY();
        }

        log.trace("Preprocessing Variable Reference [{}]", reference);

        final ExpressionPreprocessor preprocessor = context.mappers.getExpressionPreprocessor();
        final List<Statement> statements = reference.getIndexes().stream()
              .map(preprocessor)
              .map(StructureSimple::getStatements)
              .flatMap(List::stream)
              .collect(Collectors.toList());

        return StructureSimple.builder()
              .statements(statements)
              .build();
    }
}
