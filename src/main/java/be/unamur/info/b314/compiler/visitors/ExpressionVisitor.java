package be.unamur.info.b314.compiler.visitors;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.GrammarVisitor;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.pils.declarations.Function;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidNumberOfIndexesException;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionGroup;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import be.unamur.info.b314.compiler.pils.keywords.Direction;
import be.unamur.info.b314.compiler.pils.keywords.PositionParameter;
import be.unamur.info.b314.compiler.pils.keywords.Square;
import be.unamur.info.b314.compiler.pils.keywords.Target;
import be.unamur.info.b314.compiler.pils.values.Value;
import be.unamur.info.b314.compiler.pils.values.booleans.Location;
import be.unamur.info.b314.compiler.pils.values.booleans.Negation;
import be.unamur.info.b314.compiler.pils.values.integers.Count;
import be.unamur.info.b314.compiler.pils.values.integers.Life;
import be.unamur.info.b314.compiler.pils.values.integers.Position;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveBoolean;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveInteger;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveSquare;
import be.unamur.info.b314.compiler.pils.values.references.FunctionCall;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import be.unamur.info.b314.compiler.pils.values.squares.Nearby;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;
import java.util.stream.Collectors;

import static be.unamur.info.b314.compiler.pils.keywords.Item.*;
import static be.unamur.info.b314.compiler.pils.keywords.Property.LIFE;

/**
 * @author Hadrien BAILLY
 */
@Data
@AllArgsConstructor
public class ExpressionVisitor implements GrammarVisitor<Expression> {

    private final Context context;

    @Override
    public Expression visitRoot(final GrammarParser.RootContext ctx) {
        return null;
    }

    @Override
    public Expression visitWorld(final GrammarParser.WorldContext ctx) {
        return null;
    }

    @Override
    public Expression visitStrategy(final GrammarParser.StrategyContext ctx) {
        return null;
    }

    @Override
    public Expression visitDeclaration(final GrammarParser.DeclarationContext ctx) {
        return null;
    }

    @Override
    public Expression visitVarDecl(final GrammarParser.VarDeclContext ctx) {
        return null;
    }

    @Override
    public Expression visitFctDecl(final GrammarParser.FctDeclContext ctx) {
        return visitExpression(ctx.expression());
    }

    @Override
    public Expression visitVarType(final GrammarParser.VarTypeContext ctx) {
        return null;
    }

    @Override
    public Expression visitSkipInstr(final GrammarParser.SkipInstrContext ctx) {
        return null;
    }

    @Override
    public Expression visitIfInstr(final GrammarParser.IfInstrContext ctx) {
        return null;
    }

    @Override
    public Expression visitWhileInstr(final GrammarParser.WhileInstrContext ctx) {
        return null;
    }

    @Override
    public Expression visitSetInstr(final GrammarParser.SetInstrContext ctx) {
        return visitExpression(ctx.expression());
    }

    @Override
    public Expression visitComputeInstr(final GrammarParser.ComputeInstrContext ctx) {
        return null;
    }

    @Override
    public Expression visitNextInstr(final GrammarParser.NextInstrContext ctx) {
        return null;
    }

    @Override
    public Expression visitBody(final GrammarParser.BodyContext ctx) {
        return null;
    }

    @Override
    public Expression visitAction(final GrammarParser.ActionContext ctx) {
        return null;
    }

    @Override
    public Expression visitReference(final GrammarParser.ReferenceContext ctx) {
        if (ctx.variable() != null) {
            return visitVariable(ctx.variable());
        }
        if (ctx.function() != null) {
            return visitFunction(ctx.function());
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression visitVariable(final GrammarParser.VariableContext ctx) {
        final VariableReference element = new VariableReferenceVisitor(context).visitVariable(ctx);
        return new ExpressionLeaf(element);
    }

    @Override
    public Expression visitFunction(final GrammarParser.FunctionContext ctx) {

        // STEP 1 - Récupérer la fonction depuis la table des symboles
        final Function function = context.getFunction(ctx.ID().getText());

        // STEP 2 - Récupérer la liste des arguments de la fonction
        final List<Expression> arguments = ctx.expression().stream()
              .map(this::visitExpression)
              .collect(Collectors.toList());

        // STEP 3 - Construire l'appel de fonction
        final FunctionCall call = FunctionCall.builder()
              .function(function)
              .arguments(arguments)
              .build()
              .validate();

        return ExpressionLeaf.builder()
              .value(call)
              .build();
    }

    @Override
    public Expression visitExpression(final GrammarParser.ExpressionContext ctx) {
        return visitSubExpression(ctx.subExpression())
              .validate();
    }

    @Override
    public Expression visitSubExpression(final GrammarParser.SubExpressionContext ctx) {

        // Vérifier si l'expression est une valeur simple
        if (ctx.value() != null) {
            // Si oui, retourner la valeur
            return visitValue(ctx.value());
        }

        // Sinon, recomposer l'opération
        if (ctx.operation() != null) {
            // Récupérer l'opérateur.
            final Operator operator = new OperatorVisitor().visitOperation(ctx.operation());
            // Récupérer l'opérande gauche.
            final Expression left = visitSubExpression(ctx.subExpression(0));
            // récupérer l'opérande droite
            final ExpressionLeaf right = (ExpressionLeaf) visitSubExpression(ctx.subExpression(1));
            // Construire et retourner l'opération
            return left.append(right, operator);

        }
        throw new UnsupportedOperationException(ctx.getText());
    }

    @Override
    public Expression visitOperation(final GrammarParser.OperationContext ctx) {
        return null;
    }

    @Override
    public Expression visitValue(final GrammarParser.ValueContext ctx) {

        if (ctx.integer() != null) {
            return visitInteger(ctx.integer());
        }

        if (ctx.bool() != null) {
            return visitBool(ctx.bool());
        }

        if (ctx.square() != null) {
            return visitSquare(ctx.square());
        }

        if (ctx.reference() != null) {
            return visitReference(ctx.reference());
        }

        if (ctx.expression() != null) {
            final Expression expression = visitExpression(ctx.expression());

            final ExpressionGroup expressionGroup = ExpressionGroup.builder()
                  .expression(expression)
                  .build();

            return ExpressionLeaf.builder()
                  .value(expressionGroup)
                  .build();
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public Expression visitInteger(final GrammarParser.IntegerContext ctx) {
        if (ctx.NUMBER() != null) {
            final int integer = Integer.parseInt(ctx.NUMBER().getText());
            final PrimitiveInteger primitiveInteger = new PrimitiveInteger(ctx.neg == null ? integer : -integer);
            return ExpressionLeaf.builder()
                  .value(primitiveInteger)
                  .build();
        }
        if (ctx.intVariable() != null) {
            return visitIntVariable(ctx.intVariable());
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression visitIntVariable(final GrammarParser.IntVariableContext ctx) {

        if (ctx.position() != null) {
            return visitPosition(ctx.position());
        }
        if (ctx.count() != null) {
            return visitCount(ctx.count());
        }
        if (ctx.LIFE() != null) {
            final Life life = new Life(context.getVariable(LIFE.getToken()));
            return ExpressionLeaf.builder()
                  .value(life)
                  .build();
        }
        throw new UnsupportedOperationException(ctx.getText());
    }

    @Override
    public Expression visitPosition(final GrammarParser.PositionContext ctx) {
        final Position position;
        if (ctx.LONGITUDE() != null) {
            position = Position.builder()
                  .parameter(PositionParameter.LONGITUDE)
                  .context(context)
                  .build();
        } else if (ctx.LATITUDE() != null) {
            position = Position.builder()
                  .parameter(PositionParameter.LATITUDE)
                  .context(context)
                  .build();
        } else if (ctx.GRID() != null) {
            position = Position.builder()
                  .parameter(PositionParameter.GRID_SIZE)
                  .context(context)
                  .build();
        } else {
            throw new UnsupportedOperationException(ctx.getText());
        }
        return ExpressionLeaf.builder()
              .value(position)
              .build();
    }

    @Override
    public Expression visitCount(final GrammarParser.CountContext ctx) {
        final Count count;
        if (ctx.MAP() != null) {
            count = Count.builder()
                  .item(MAP)
                  .variable(context.getVariable(MAP.getToken()))
                  .build();
        } else if (ctx.RADIO() != null) {
            count = Count.builder()
                  .item(RADIO)
                  .variable(context.getVariable(RADIO.getToken()))
                  .build();
        } else if (ctx.RADAR() != null) {
            count = Count.builder()
                  .item(RADAR)
                  .variable(context.getVariable(RADAR.getToken()))
                  .build();
        } else if (ctx.AMMO() != null) {
            count = Count.builder()
                  .item(AMMO)
                  .variable(context.getVariable(AMMO.getToken()))
                  .build();
        } else if (ctx.FRUITS() != null) {
            count = Count.builder()
                  .item(FRUITS)
                  .variable(context.getVariable(FRUITS.getToken()))
                  .build();
        } else if (ctx.SODA() != null) {
            count = Count.builder()
                  .item(SODA)
                  .variable(context.getVariable(SODA.getToken()))
                  .build();
        } else {
            throw new UnsupportedOperationException(ctx.getText());
        }
        return ExpressionLeaf.builder()
              .value(count)
              .build();
    }

    @Override
    public Expression visitBool(final GrammarParser.BoolContext ctx) {
        if (ctx.boolValue() != null) {
            return visitBoolValue(ctx.boolValue());
        }
        if (ctx.boolNegation() != null) {
            return visitBoolNegation(ctx.boolNegation());
        }
        if (ctx.boolLocation() != null) {
            return visitBoolLocation(ctx.boolLocation());
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression visitBoolValue(final GrammarParser.BoolValueContext ctx) {
        final Value value = ctx.TRUE() != null
              ? PrimitiveBoolean.TRUE
              : PrimitiveBoolean.FALSE;

        return ExpressionLeaf.builder()
              .value(value)
              .build();
    }

    @Override
    public Expression visitBoolLocation(final GrammarParser.BoolLocationContext ctx) {
        final Target target = ctx.GRAAL() != null
              ? Target.GRAAL
              : Target.ENEMY;

        final Direction direction = ctx.NORTH() != null
              ? Direction.NORTH
              : ctx.SOUTH() != null
              ? Direction.SOUTH
              : ctx.EAST() != null
              ? Direction.EAST
              : Direction.WEST;

        final Location location = Location.builder()
              .target(target)
              .direction(direction)
              .arena(context.getArena())
              .build();

        return ExpressionLeaf.builder()
              .value(location)
              .build();
    }

    @Override
    public Expression visitBoolNegation(final GrammarParser.BoolNegationContext ctx) {
        final Expression expression = visitExpression(ctx.expression());

        if (!expression.isBoolean()) {
            throw new InvalidDataTypeException(String.format("Cannot negate [%s] expression.", expression.getType()));
        }
        final Negation negation = Negation.builder()
              .expression(expression)
              .build();

        return ExpressionLeaf.builder()
              .value(negation)
              .build();
    }

    @Override
    public Expression visitSquare(final GrammarParser.SquareContext ctx) {
        if (ctx.squareValue() != null) {
            return visitSquareValue(ctx.squareValue());
        }
        if (ctx.squareNearby() != null) {
            return visitSquareNearby(ctx.squareNearby());
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression visitSquareValue(final GrammarParser.SquareValueContext ctx) {
        final Square square = Square.resolve(ctx.getText());
        final PrimitiveSquare primitiveSquare = new PrimitiveSquare(square);

        return ExpressionLeaf.builder()
              .value(primitiveSquare)
              .build();
    }

    @Override
    public Expression visitSquareNearby(final GrammarParser.SquareNearbyContext ctx) {

        // STEP 0 - Initialiser un visiteur d'expression (optimisation)
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(context);
        // STEP 1 - Récupérer la liste de l'ensemble des indices sous forme d'expression.
        final List<Expression> expressions = ctx.expression().stream()
              .map(expressionVisitor::visitExpression)
              .collect(Collectors.toList());

        // STEP 2 - Vérifier que
        if (expressions.size() != 2) {
            throw new InvalidNumberOfIndexesException(String.format("The number of indexes in the nearby expression is not valid. Found %s", expressions.size()));
        }
        if (!expressions.stream().allMatch(Expression::isInteger)) {
            throw new InvalidDataTypeException(String.format("One of the index expressions is not of type %s", Type.INTEGER));
        }
        final Nearby nearby = Nearby.builder()
              .coordinates(new ImmutablePair<>(expressions.get(0), expressions.get(1)))
              .arena(context.getArena())
              .build();

        return ExpressionLeaf.builder()
              .value(nearby)
              .build();
    }

    @Override
    public Expression visitType(final GrammarParser.TypeContext ctx) {
        return null;
    }

    @Override
    public Expression visitScalar(final GrammarParser.ScalarContext ctx) {
        return null;
    }

    @Override
    public Expression visitArray(final GrammarParser.ArrayContext ctx) {
        return null;
    }

    @Override
    public Expression visitImpDecl(final GrammarParser.ImpDeclContext ctx) {
        return null;
    }

    @Override
    public Expression visitClauseWhen(final GrammarParser.ClauseWhenContext ctx) {
        return null;
    }

    @Override
    public Expression visitClauseDefault(final GrammarParser.ClauseDefaultContext ctx) {
        return null;
    }

    @Override
    public Expression visit(final ParseTree tree) {
        return null;
    }

    @Override
    public Expression visitChildren(final RuleNode node) {
        return null;
    }

    @Override
    public Expression visitTerminal(final TerminalNode node) {
        return null;
    }

    @Override
    public Expression visitErrorNode(final ErrorNode node) {
        return null;
    }

}
