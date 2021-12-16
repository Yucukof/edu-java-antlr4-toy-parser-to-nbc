package be.unamur.info.b314.compiler.mappers.values;


import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.pils.values.references.FunctionCall;

import java.util.function.Function;

public class FunctionCallMapper implements Function<FunctionCall, Argument> {

    @Override
    public Argument apply(FunctionCall functionCall) {
        be.unamur.info.b314.compiler.pils.declarations.Function function = functionCall.getFunction();
        if (!function.isVoid()) {
            return function.getOutputIdentifier();
        } else {
            throw new UnsupportedOperationException("Cannot map void function to register.");
        }
    }
}
