package be.unamur.info.b314.compiler.mappers.values;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionMapper;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.nbc.symbols.IndexedIdentifier;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import com.ibm.icu.impl.number.parse.ScientificMatcher;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
public class VariableReferenceMapper implements Function<VariableReference, Identifier> {

    private final Context context;

    public VariableReferenceMapper(final Context context) {
        this.context = context;
    }

    @Override
    public Identifier apply(final VariableReference reference) {

        log.trace("Processing reference [{}]", reference);

        // STEP 1 - Récupérer le nom de la référence
        final String name = reference.getVariable().getIdentifier().toString();
        final Identifier identifier;


        // STEP 2 - Vérifier si la référence utilise des indices.
        if (reference.isArray()) {
            // Si oui, récupérer un ExpressionMapper.
            final ExpressionMapper mapper = context.mappers.getExpressionMapper();
            // Inverser la liste pour récupérer les registres dans l'ordre inverse de réservation (LIFO)
            final ArrayList<Expression> copy = new ArrayList<>(reference.getIndexes());
            Collections.reverse(copy);
            // Construire la liste des arguments
            final List<Argument> arguments = copy.stream()
                  .map(mapper)
                  .collect(Collectors.toList());
            // Remettre la liste d'arguments dans son ordre véritable
            Collections.reverse(arguments);
            // Libérer les différents registres
            reference.getIndexes().stream()
                  .filter(expression -> !expression.isConstant())
                  .forEach(expression -> context.memory.release(expression.getType()));
            // Construire et retourner une référence indicée.
            identifier = IndexedIdentifier.builder()
                    .name(name)
                    .indexes(arguments)
                    .build();
        }

        else {
            // Si non, construire et retourner une référence simple.
            identifier = Identifier.builder()
                    .name(name)
                    .build();
        }

        log.trace("Target Register: [{}]", identifier);
        return identifier;
    }
}
