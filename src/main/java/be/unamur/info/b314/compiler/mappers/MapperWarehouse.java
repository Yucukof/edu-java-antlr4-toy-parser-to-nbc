package be.unamur.info.b314.compiler.mappers;

import be.unamur.info.b314.compiler.mappers.actions.ActionMoveMapper;
import be.unamur.info.b314.compiler.mappers.actions.ActionShootMapper;
import be.unamur.info.b314.compiler.mappers.actions.ActionUseMapper;
import be.unamur.info.b314.compiler.mappers.actions.DirectionToToneMapper;
import be.unamur.info.b314.compiler.mappers.clauses.ClauseDefaultMapper;
import be.unamur.info.b314.compiler.mappers.clauses.ClauseWhenMapper;
import be.unamur.info.b314.compiler.mappers.declarations.DeclarationMapper;
import be.unamur.info.b314.compiler.mappers.declarations.FunctionMapper;
import be.unamur.info.b314.compiler.mappers.declarations.TypeMapper;
import be.unamur.info.b314.compiler.mappers.declarations.VariableMapper;
import be.unamur.info.b314.compiler.mappers.expressions.*;
import be.unamur.info.b314.compiler.mappers.imports.ImportMapper;
import be.unamur.info.b314.compiler.mappers.primitives.PrimitiveBooleanMapper;
import be.unamur.info.b314.compiler.mappers.primitives.PrimitiveIntegerMapper;
import be.unamur.info.b314.compiler.mappers.primitives.PrimitiveSquareMapper;
import be.unamur.info.b314.compiler.mappers.program.ProgramMapper;
import be.unamur.info.b314.compiler.mappers.program.StrategyMapper;
import be.unamur.info.b314.compiler.mappers.program.WorldMapper;
import be.unamur.info.b314.compiler.mappers.statements.*;
import be.unamur.info.b314.compiler.mappers.values.*;
import be.unamur.info.b314.compiler.mappers.values.booleans.LocationMapper;
import be.unamur.info.b314.compiler.mappers.values.booleans.NegationMapper;
import be.unamur.info.b314.compiler.mappers.values.booleans.NegationPreprocessor;
import be.unamur.info.b314.compiler.mappers.values.integers.CountMapper;
import be.unamur.info.b314.compiler.mappers.values.integers.PositionMapper;
import be.unamur.info.b314.compiler.mappers.values.squares.NearbyMapper;
import be.unamur.info.b314.compiler.mappers.values.squares.NearbyPreprocessor;
import lombok.Getter;

/**
 * L'entrepôt de convertisseurs est l'endroit où sont stockés les références vers les différents convertisseurs
 * instanciés pour la conversion du langage PILS vers le langage NBC. On distingue ici
 * <br> - Les convertisseurs pouvant fonctionner sans contexte (convertisseurs absolus)
 * <br> - Les convertisseurs servant à formuler les précalculs des objes ne pouvant pas être convertis directement en
 * NBC (convertisseurs préproducteurs).
 * <br> - Les convertisseurs nécessitant un certain contexte pour pouvoir effectuer la conversion (convertisseurs
 * relatifs).
 *
 * @author Hadrien BAILLY
 */
@Getter
public class MapperWarehouse {

    // Absolute Mappers
    private final TypeMapper typeMapper;
    private final PrimitiveBooleanMapper primitiveBooleanMapper;
    private final PrimitiveIntegerMapper primitiveIntegerMapper;
    private final PrimitiveSquareMapper primitiveSquareMapper;
    private final OperatorToComparisonMapper operatorToComparisonMapper;
    private final OperatorToKeywordMapper operatorToKeywordMapper;
    private final ImportMapper importMapper;
    private final LocationMapper locationMapper;
    private final CountMapper countMapper;
    private final PositionMapper positionMapper;

    // Preprocessors
    private final ExpressionPreprocessor expressionPreprocessor;
    private final VariableReferencePreprocessor variableReferencePreprocessor;
    private final NegationPreprocessor negationPreprocessor;
    private final NearbyPreprocessor nearbyPreprocessor;
    private final FunctionCallPreprocessor functionCallPreprocessor;

    // Relative Mappers
    private final NegationMapper negationMapper;
    private final NearbyMapper nearbyMapper;
    private final ValueMapper valueMapper;
    private final StatementMapper statementMapper;
    private final StatementComputeMapper statementComputeMapper;
    private final StatementIfMapper statementIfMapper;
    private final StatementSetMapper statementSetMapper;
    private final StatementWhileMapper statementWhileMapper;
    private final StatementNextMapper statementNextMapper;
    private final ActionShootMapper actionShootMapper;
    private final ActionMoveMapper actionMoveMapper;
    private final ActionUseMapper actionUseMapper;
    private final DirectionToToneMapper directionToToneMapper;
    private final ExpressionLeafMapper expressionLeafMapper;
    private final ExpressionMapper expressionMapper;
    private final VariableReferenceMapper variableReferenceMapper;
    private final VariableMapper variableMapper;
    private final FunctionCallMapper functionCallMapper;
    private final FunctionMapper functionMapper;
    private final DeclarationMapper declarationMapper;
    private final ClauseWhenMapper clauseWhenMapper;
    private final ClauseDefaultMapper clauseDefaultMapper;
    private final WorldMapper worldMapper;
    private final StrategyMapper strategyMapper;
    private final ProgramMapper programMapper;

    public MapperWarehouse(final Context context) {
        // Absolute Mappers
        typeMapper = new TypeMapper();
        primitiveBooleanMapper = new PrimitiveBooleanMapper();
        primitiveIntegerMapper = new PrimitiveIntegerMapper();
        primitiveSquareMapper = new PrimitiveSquareMapper();
        operatorToComparisonMapper = new OperatorToComparisonMapper();
        operatorToKeywordMapper = new OperatorToKeywordMapper();
        importMapper = new ImportMapper();
        locationMapper = new LocationMapper();
        countMapper = new CountMapper();
        positionMapper = new PositionMapper();
        functionCallMapper = new FunctionCallMapper();

        // Preprocessors
        expressionPreprocessor = new ExpressionPreprocessor(context);
        variableReferencePreprocessor = new VariableReferencePreprocessor(context);
        negationPreprocessor = new NegationPreprocessor(context);
        nearbyPreprocessor = new NearbyPreprocessor(context);
        functionCallPreprocessor = new FunctionCallPreprocessor(context);

        // Relative Mappers
        negationMapper = new NegationMapper(context);
        nearbyMapper = new NearbyMapper(context);
        valueMapper = new ValueMapper(context);
        statementMapper = new StatementMapper(context);
        statementComputeMapper = new StatementComputeMapper(context);
        statementIfMapper = new StatementIfMapper(context);
        statementSetMapper = new StatementSetMapper(context);
        statementWhileMapper = new StatementWhileMapper(context);
        statementNextMapper = new StatementNextMapper(context);
        actionShootMapper = new ActionShootMapper(context);
        actionMoveMapper = new ActionMoveMapper(context);
        actionUseMapper = new ActionUseMapper(context);
        directionToToneMapper = new DirectionToToneMapper(context);
        expressionLeafMapper = new ExpressionLeafMapper(context);
        expressionMapper = new ExpressionMapper(context);
        variableReferenceMapper = new VariableReferenceMapper(context);
        variableMapper = new VariableMapper(context);
        functionMapper = new FunctionMapper(context);
        declarationMapper = new DeclarationMapper(context);
        clauseWhenMapper = new ClauseWhenMapper(context);
        clauseDefaultMapper = new ClauseDefaultMapper(context);
        worldMapper = new WorldMapper(context);
        strategyMapper = new StrategyMapper(context);
        programMapper = new ProgramMapper(context);
    }
}
