package be.unamur.info.b314.compiler.mappers.expressions;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.mappers.values.FunctionCallPreprocessor;
import be.unamur.info.b314.compiler.mappers.values.ValueMapper;
import be.unamur.info.b314.compiler.mappers.values.booleans.NegationMapper;
import be.unamur.info.b314.compiler.mappers.values.booleans.NegationPreprocessor;
import be.unamur.info.b314.compiler.mappers.values.squares.NearbyMapper;
import be.unamur.info.b314.compiler.mappers.values.squares.NearbyPreprocessor;
import be.unamur.info.b314.compiler.nbc.Argument;
import be.unamur.info.b314.compiler.nbc.Statement;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionDataBinary;
import be.unamur.info.b314.compiler.nbc.instructions.InstructionDataCMP;
import be.unamur.info.b314.compiler.nbc.keywords.Keyword;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.nbc.symbols.Comparator;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.values.references.FunctionCall;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionGroup;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionNode;
import be.unamur.info.b314.compiler.pils.values.Value;
import be.unamur.info.b314.compiler.pils.values.booleans.Location;
import be.unamur.info.b314.compiler.pils.values.booleans.Negation;
import be.unamur.info.b314.compiler.pils.values.integers.Count;
import be.unamur.info.b314.compiler.pils.values.integers.Life;
import be.unamur.info.b314.compiler.pils.values.integers.Position;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import be.unamur.info.b314.compiler.pils.values.squares.Nearby;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * Le préprocesseur d'expression permet de créer les instructions de calcul nécessaires à l'obtention de la valeur de
 * l'expression.
 *
 * @author Hadrien BAILLY
 * @see ExpressionMapper
 */
@Slf4j
public class ExpressionPreprocessor implements Function<Expression, StructureSimple> {

    private final Context context;

    public ExpressionPreprocessor(final Context context) {
        this.context = context;
    }

    @Override
    public StructureSimple apply(@NonNull final Expression expression) {

        log.trace("Preprocessing Expression [{}]", expression);
        if (expression.isPrimitive()) {
            log.trace("Expression is constant: nothing to preprocess.");
            return StructureSimple.EMPTY();
        }
        return expression.isLeaf()
                ? preprocess((ExpressionLeaf) expression)
                : preprocess((ExpressionNode) expression);

    }

    private StructureSimple preprocess(final ExpressionLeaf expression) {

        final Value value = expression.getValue();

        if (value instanceof VariableReference) {
            log.trace("Expression is a Variable Reference");
            return StructureSimple.EMPTY();
        }

        if (value instanceof Negation) {
            final NegationPreprocessor preprocessor = context.mappers.getNegationPreprocessor();
            return preprocessor.apply((Negation) value);
        }

        if (value instanceof Nearby) {
            final NearbyPreprocessor preprocessor = context.mappers.getNearbyPreprocessor();
            return preprocessor.apply((Nearby) value);
        }

        if (value instanceof Life || value instanceof Position || value instanceof Count || value instanceof Location) {
            return StructureSimple.EMPTY();
        }

        if (value instanceof ExpressionGroup) {
            return this.apply(((ExpressionGroup) value).getExpression());
        }

        if (value instanceof FunctionCall) {
            final FunctionCallPreprocessor preprocessor = context.mappers.getFunctionCallPreprocessor();
            return preprocessor.apply((FunctionCall) value);
        }
        return StructureSimple.EMPTY();
    }

    private StructureSimple preprocess(@NonNull final ExpressionNode expression) {

        final ExpressionMapper mapper = context.mappers.getExpressionMapper();

        final Expression expressionLeft = expression.getLeft();
        final Expression expressionRight = expression.getRight();

        final StructureSimple.StructureSimpleBuilder<?, ?> builder = StructureSimple.builder();

        builder.statements(apply(expressionRight).getStatements());
        final Argument right = mapper.apply(expressionRight);
        if (!expressionRight.isConstant() && !expressionRight.isLeaf()) {
            context.memory.reserveAndGet(expressionRight.getType());
        }

        builder.statements(apply(expressionLeft).getStatements());
        final Argument left = mapper.apply(expressionLeft);

        if (!expressionRight.isConstant() && !expressionRight.isLeaf()) {
            context.memory.release(expressionRight.getType());
        }

        // STEP 1 - Récupérer le mot-clé
        final Keyword keyword = new OperatorToKeywordMapper().apply(expression.getOperator());

        // STEP 4 - Récupérer le registre contenant le résultat de l'expression
        final Identifier destination = context.memory.getDisposable(expression.getType());
        log.trace("Using register [{}]", destination);

        // STEP 5 - Construire le statement
        log.trace("Constructing [{}] statement", keyword);
        final Statement statement;
        if (expression.getOperator().isComparator()) {
            final OperatorToComparisonMapper comparisonMapper = context.mappers.getOperatorToComparisonMapper();
            final Comparator comparator = comparisonMapper.apply(expression.getOperator());

            statement = InstructionDataCMP.builder()
                    .keyword(keyword)
                    .comparator(comparator)
                    .destination(destination)
                    .argument1(left)
                    .argument2(right)
                    .build();
        } else {
            statement = InstructionDataBinary.builder()
                    .keyword(keyword)
                    .destination(destination)
                    .argument1(left)
                    .argument2(right)
                    .build();
        }
        log.trace("{}", statement);

        // Créer et retourner une dernière expression liant les deux branches
        return builder
                .statement(statement)
                .build();
    }
}
