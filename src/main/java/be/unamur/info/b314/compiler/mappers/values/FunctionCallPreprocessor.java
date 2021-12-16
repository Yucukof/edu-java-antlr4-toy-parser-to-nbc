package be.unamur.info.b314.compiler.mappers.values;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionMapper;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionPreprocessor;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.Structure;
import be.unamur.info.b314.compiler.nbc.instructions.Instruction;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionControl;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionDataUnary;
import be.unamur.info.b314.compiler.nbc.keywords.Assignment;
import be.unamur.info.b314.compiler.nbc.keywords.Scheduling;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.values.references.FunctionCall;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
public class FunctionCallPreprocessor implements Function<FunctionCall, StructureSimple> {

    private final Context context;

    public FunctionCallPreprocessor(final Context context) {
        this.context = context;
    }

    @Override
    public StructureSimple apply(final FunctionCall call) {
        log.trace("Converting Function call [{}]", call);
        StructureSimple structure = StructureSimple.EMPTY();
        // Récupérer la fonction
        be.unamur.info.b314.compiler.pils.declarations.Function function = call.getFunction();
        // constuire l'instruction qui assigne la valeur de retour à l'appelant

        // constuire les instructions qui assignent la valeur des arguments formels aux arguments effectifs
        if (function.hasArguments()) {

            //récupérer arguments formels
            final List<Argument> formalArguments = function.getArguments().stream()
                    .map(Variable::getIdentifier)
                    .collect(Collectors.toList());

            final ExpressionPreprocessor preprocessor = context.mappers.getExpressionPreprocessor();
            final ExpressionMapper expressionMapper = context.mappers.getExpressionMapper();
            // Assigner les valeurs des arguments effectifs aux registres des arguments formels
            for (int i = 0; i < call.getArguments().size(); i++) {
                // Récupérer l'argument formel à assigner
                final Identifier formalArgument = (Identifier) formalArguments.get(i);
                // Récupérer l'expression à traiter
                Expression argumentExpr = call.getArguments().get(i);
                // Précalculer l'expression si nécessaire
                final Structure preprocess = preprocessor.apply(argumentExpr);
                // Récupérer les instructions liées au pré-calcul
                structure = structure.toBuilder()
                        .statements(preprocess.getStatements())
                        .build();
                // Récupérer le registre/la valeur résultante
                final Argument effectiveArgument = expressionMapper.apply(argumentExpr);
                // Assigner ce registre/cette valeur au registre de l'argument formel
                final Instruction instruction = InstructionDataUnary.builder()
                        .keyword(Assignment.MOV)
                        .destination(formalArgument)
                        .argument(effectiveArgument)
                        .build();
                // Ajouter l'instruction d'assignation à la structure
                structure = structure.toBuilder()
                        .statement(instruction)
                        .build();
            }

        }
        // Construire l'instruction d'appel à la fonction
        final Identifier identifier = new Identifier(function.getName());
        final Instruction instructionCall = InstructionControl.builder()
                .keyword(Scheduling.CALL)
                .destination(identifier)
                .build();
        // Ajouter l'instruction de saut à la structure
        structure = structure.toBuilder()
                .statement(instructionCall)
                .build();
        // Retourner la structure
        return structure;
    }
}
