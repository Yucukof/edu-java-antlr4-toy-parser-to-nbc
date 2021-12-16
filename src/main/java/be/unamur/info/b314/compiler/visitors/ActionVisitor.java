package be.unamur.info.b314.compiler.visitors;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.GrammarVisitor;
import be.unamur.info.b314.compiler.pils.actions.*;
import be.unamur.info.b314.compiler.pils.exceptions.ActionNotFoundException;
import be.unamur.info.b314.compiler.pils.exceptions.DirectionNotFoundException;
import be.unamur.info.b314.compiler.pils.exceptions.ItemNotFoundException;
import be.unamur.info.b314.compiler.pils.keywords.Act;
import be.unamur.info.b314.compiler.pils.keywords.Direction;
import be.unamur.info.b314.compiler.pils.keywords.Item;
import be.unamur.info.b314.compiler.listeners.Context;
import lombok.Data;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * @author Anthony DI STASIO
 */
@Data
public class ActionVisitor implements GrammarVisitor<Action> {
    private final Context symbols;

    @Override
    public Action visitRoot(GrammarParser.RootContext ctx) {
        return null;
    }

    @Override
    public Action visitWorld(GrammarParser.WorldContext ctx) {
        return null;
    }

    @Override
    public Action visitStrategy(GrammarParser.StrategyContext ctx) {
        return null;
    }

    @Override
    public Action visitDeclaration(final GrammarParser.DeclarationContext ctx) {
        return null;
    }

    @Override
    public Action visitVarDecl(GrammarParser.VarDeclContext ctx) {
        return null;
    }

    @Override
    public Action visitFctDecl(GrammarParser.FctDeclContext ctx) {
        return null;
    }

    @Override
    public Action visitVarType(GrammarParser.VarTypeContext ctx) {
        return null;
    }

    @Override
    public Action visitSkipInstr(GrammarParser.SkipInstrContext ctx) {
        return null;
    }

    @Override
    public Action visitIfInstr(GrammarParser.IfInstrContext ctx) {
        return null;
    }

    @Override
    public Action visitWhileInstr(GrammarParser.WhileInstrContext ctx) {
        return null;
    }

    @Override
    public Action visitSetInstr(GrammarParser.SetInstrContext ctx) {
        return null;
    }

    @Override
    public Action visitComputeInstr(GrammarParser.ComputeInstrContext ctx) {
        return null;
    }

    @Override
    public Action visitNextInstr(GrammarParser.NextInstrContext ctx) {
        return null;
    }

    @Override
    public Action visitBody(GrammarParser.BodyContext ctx) {
        return null;
    }

    @Override
    public Action visitAction(GrammarParser.ActionContext ctx) {

        if (ctx.MOVE() != null || ctx.SHOOT() != null) {
            final Direction direction = getDirection(ctx);

            if (ctx.MOVE() != null) {
                return ActionMove.builder()
                      .act(Act.MOVE)
                      .direction(direction)
                      .build();
            }

            if (ctx.SHOOT() != null) {
                return ActionShoot.builder()
                      .act(Act.SHOOT)
                      .direction(direction)
                      .build();
            }
        }

        if (ctx.USE() != null) {
            return ActionUse.builder()
                  .act(Act.USE)
                  .item(getItem(ctx))
                  .build();
        }

        if (ctx.DO() != null) {
            return ActionIdle.builder()
                  .build();
        }

        throw new ActionNotFoundException("Action has not been found!");
    }

    private Direction getDirection(GrammarParser.ActionContext ctx) {
        if (ctx.NORTH() != null) {
            return Direction.NORTH;
        }

        if (ctx.SOUTH() != null) {
            return Direction.SOUTH;
        }

        if (ctx.WEST() != null) {
            return Direction.WEST;
        }

        if (ctx.EAST() != null) {
            return Direction.EAST;
        }

        throw new DirectionNotFoundException("The direction has not been found!");
    }

    private Item getItem(GrammarParser.ActionContext ctx) {
        if (ctx.RADIO() != null) {
            return Item.RADIO;
        }
        if (ctx.RADAR() != null) {
            return Item.RADAR;
        }

        if (ctx.FRUITS() != null) {
            return Item.FRUITS;
        }

        if (ctx.SODA() != null) {
            return Item.SODA;
        }

        if (ctx.MAP() != null) {
            return Item.MAP;
        }

        throw new ItemNotFoundException("The item has not been found!");
    }

    @Override
    public Action visitReference(GrammarParser.ReferenceContext ctx) {
        return null;
    }

    @Override
    public Action visitVariable(final GrammarParser.VariableContext ctx) {
        return null;
    }

    @Override
    public Action visitFunction(GrammarParser.FunctionContext ctx) {
        return null;
    }

    @Override
    public Action visitExpression(GrammarParser.ExpressionContext ctx) {
        return null;
    }

    @Override
    public Action visitSubExpression(final GrammarParser.SubExpressionContext ctx) {
        return null;
    }

    @Override
    public Action visitOperation(GrammarParser.OperationContext ctx) {
        return null;
    }

    @Override
    public Action visitValue(GrammarParser.ValueContext ctx) {
        return null;
    }

    @Override
    public Action visitInteger(GrammarParser.IntegerContext ctx) {
        return null;
    }

    @Override
    public Action visitIntVariable(GrammarParser.IntVariableContext ctx) {
        return null;
    }

    @Override
    public Action visitPosition(GrammarParser.PositionContext ctx) {
        return null;
    }

    @Override
    public Action visitCount(GrammarParser.CountContext ctx) {
        return null;
    }

    @Override
    public Action visitBool(GrammarParser.BoolContext ctx) {
        return null;
    }

    @Override
    public Action visitBoolValue(GrammarParser.BoolValueContext ctx) {
        return null;
    }

    @Override
    public Action visitBoolLocation(GrammarParser.BoolLocationContext ctx) {
        return null;
    }

    @Override
    public Action visitBoolNegation(GrammarParser.BoolNegationContext ctx) {
        return null;
    }

    @Override
    public Action visitSquare(GrammarParser.SquareContext ctx) {
        return null;
    }

    @Override
    public Action visitSquareValue(GrammarParser.SquareValueContext ctx) {
        return null;
    }

    @Override
    public Action visitSquareNearby(GrammarParser.SquareNearbyContext ctx) {
        return null;
    }

    @Override
    public Action visitType(GrammarParser.TypeContext ctx) {
        return null;
    }

    @Override
    public Action visitScalar(GrammarParser.ScalarContext ctx) {
        return null;
    }

    @Override
    public Action visitArray(GrammarParser.ArrayContext ctx) {
        return null;
    }

    @Override
    public Action visitImpDecl(GrammarParser.ImpDeclContext ctx) {
        return null;
    }

    @Override
    public Action visitClauseWhen(GrammarParser.ClauseWhenContext ctx) {
        return null;
    }

    @Override
    public Action visitClauseDefault(GrammarParser.ClauseDefaultContext ctx) {
        return null;
    }

    @Override
    public Action visit(ParseTree parseTree) {
        return null;
    }

    @Override
    public Action visitChildren(RuleNode ruleNode) {
        return null;
    }

    @Override
    public Action visitTerminal(TerminalNode terminalNode) {
        return null;
    }

    @Override
    public Action visitErrorNode(ErrorNode errorNode) {
        return null;
    }
}