package be.unamur.info.b314.compiler.mappers.declarations;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.values.ValueMapper;
import be.unamur.info.b314.compiler.nbc.constants.Constant;
import be.unamur.info.b314.compiler.nbc.definitions.DefinitionVariable;
import be.unamur.info.b314.compiler.nbc.keywords.Type;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import lombok.NonNull;

import java.util.List;
import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 */
public class VariableMapper implements Function<Variable, DefinitionVariable> {

    private final Context context;

    public VariableMapper(final Context context) {
        this.context = context;
    }

    @Override
    public DefinitionVariable apply(@NonNull final Variable variable) {

        final TypeMapper typeMapper = context.mappers.getTypeMapper();

        // TODO 17/04/2021 [HBA]: gérer l'aliasing sur plusieurs niveaux
        // TODO: 03-05-21 : vérifier si on est dans le contexte d'une fonction (et alors il faut traiter l'aliasing)
        final Identifier identifier = new Identifier(variable.getName());
        final Type.SuperType type = typeMapper.apply(variable.getType());
        final Identifier typeIdentifier = type.getTypeIdentifier();
        final List<Integer> dimensions = variable.getDimensions();

        DefinitionVariable definition = DefinitionVariable.builder()
              .identifier(identifier)
              .typeName(typeIdentifier)
              .dimensions(dimensions)
              .build();
        if (variable.isScalar()) {
            final ValueMapper valueMapper = context.mappers.getValueMapper();
            final Constant constant = (Constant) valueMapper.apply(variable.get());
            definition = definition.toBuilder()
                  .value(constant)
                  .build();
        }
        return definition;
    }

}
