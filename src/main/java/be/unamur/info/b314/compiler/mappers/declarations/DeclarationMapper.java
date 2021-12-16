package be.unamur.info.b314.compiler.mappers.declarations;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.definitions.Definition;
import be.unamur.info.b314.compiler.pils.declarations.Declaration;
import be.unamur.info.b314.compiler.pils.declarations.Function;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import lombok.NonNull;

/**
 * @author Hadrien BAILLY
 */
public class DeclarationMapper implements java.util.function.Function<Declaration, Definition> {

    private final Context context;

    public DeclarationMapper(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public Definition apply(@NonNull final Declaration declaration) {

        final VariableMapper variableMapper = context.mappers.getVariableMapper();
        final FunctionMapper functionMapper = context.mappers.getFunctionMapper();

        if (declaration instanceof Function) {
            return functionMapper.apply((Function) declaration);
        }
        if (declaration instanceof Variable) {
            return variableMapper.apply((Variable) declaration);
        }
        throw new UnsupportedOperationException("Invalid Declaration");
    }
}
