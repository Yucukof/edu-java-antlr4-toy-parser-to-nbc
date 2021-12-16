package be.unamur.info.b314.compiler.mappers.declarations;


import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionMapper;
import be.unamur.info.b314.compiler.mappers.expressions.ExpressionPreprocessor;
import be.unamur.info.b314.compiler.mappers.statements.StatementMapper;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.Structure;
import be.unamur.info.b314.compiler.nbc.declarations.Segment;
import be.unamur.info.b314.compiler.nbc.definitions.Definition;
import be.unamur.info.b314.compiler.nbc.definitions.DefinitionVariable;
import be.unamur.info.b314.compiler.nbc.instructions.Instruction;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionDataUnary;
import be.unamur.info.b314.compiler.nbc.keywords.Assignment;
import be.unamur.info.b314.compiler.nbc.routines.Subroutine;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.nbc.symbols.IndexedIdentifier;
import be.unamur.info.b314.compiler.pils.declarations.Function;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RequiredArgsConstructor
public class FunctionMapper implements java.util.function.Function<Function, Subroutine> {

    public final Context context;

    @Override
    public Subroutine apply(@NonNull final Function function) {

        final Identifier identifier = new Identifier(function.getName());

        //final Identifier destination = context.memory.get(output.getType());

        final DeclarationMapper declarationMapper = context.mappers.getDeclarationMapper();
        final StatementMapper statementMapper = context.mappers.getStatementMapper();
        
        Segment segment = Segment.builder().build();

        if (function.hasArguments()) {
            final List<Variable> arguments = function.getArguments();
            final List<Definition> definitions = arguments.stream()
                    .map(declarationMapper)
                    .map(definition -> (DefinitionVariable) definition)
                    .map(definition -> definition.toBuilder().identifier(new Identifier(function.getName() + "_" + definition.getIdentifier().toString())).build())
                    .collect(Collectors.toList());

            IntStream.range(0, arguments.size())
                    .forEach(i -> arguments.get(i).associate(definitions.get(i)));

            // STEP 1.3 - Organiser les définitions dans un segment
            segment = segment.toBuilder()
                    .definitions(definitions)
                    .build();
        }

        // STEP 1.2 - Convertir les déclarations en définitions, s'il y en a
        if (function.hasDeclarations()) {
            final List<Variable> declarations = function.getDeclarations();
            final List<Definition> definitions = declarations.stream()
                    .map(declarationMapper)
                    .map(definition -> (DefinitionVariable) definition)
                    .map(definition -> definition.toBuilder().identifier(new Identifier(function.getName() + "_" + definition.getIdentifier().toString())).build())
                    .collect(Collectors.toList());

            IntStream.range(0, declarations.size())
                    .forEach(i -> declarations.get(i).associate(definitions.get(i)));


            // STEP 1.3 - Organiser les définitions dans un segment
            segment = segment.toBuilder()
                    .definitions(definitions)
                    .build();
        }

        final List<Structure> structures = function.getStatements().stream()
                .map(statementMapper)
                .collect(Collectors.toList());

        if (!function.isVoid()) {
            final Type type = function.getType();
            final TypeMapper typeMapper = context.mappers.getTypeMapper();
            final be.unamur.info.b314.compiler.nbc.keywords.Type.SuperType superType = typeMapper.apply(type);

            final Identifier returnIdentifier = Identifier.builder()
                    .name(function.getName() + "_result")
                    .build();

            final DefinitionVariable definition = DefinitionVariable.builder()
                    .identifier(returnIdentifier)
                    .typeName(superType.getTypeIdentifier())
                    .build();

            function.setOutputIdentifier(returnIdentifier);

            segment = segment.toBuilder()
                    .definition(definition)
                    .build();
        }

        Subroutine functionRoutine = Subroutine.builder()
                .identifier(identifier)
                .segment(segment)
                .structures(structures)
                .build();

        if (!function.isVoid()) {
            final ExpressionPreprocessor preprocessor = context.mappers.getExpressionPreprocessor();
            StructureSimple returnStructure = preprocessor.apply(function.getOutput());

            final ExpressionMapper expressionMapper = context.mappers.getExpressionMapper();
            final Argument outputRegister = expressionMapper.apply(function.getOutput());

            final Instruction instruction = InstructionDataUnary.builder()
                    .keyword(Assignment.MOV)
                    .destination(function.getOutputIdentifier())
                    .argument(outputRegister)
                    .build();

            returnStructure = returnStructure.toBuilder()
                    .statement(instruction)
                    .build();

            functionRoutine = functionRoutine.toBuilder()
                    .structure(returnStructure)
                    .build();
        }
        return functionRoutine;
    }

    public void link(final Variable variable, final Definition definition) {
        final IndexedIdentifier indexedIdentifer = IndexedIdentifier.builder()
                .name(definition.getIdentifier().toString())
                .build();



        variable.setIdentifier(indexedIdentifer);
    }
}