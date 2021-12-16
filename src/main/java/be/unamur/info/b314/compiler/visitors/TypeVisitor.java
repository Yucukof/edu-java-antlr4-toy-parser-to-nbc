package be.unamur.info.b314.compiler.visitors;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.GrammarVisitor;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import be.unamur.info.b314.compiler.listeners.Context;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import static be.unamur.info.b314.compiler.listeners.ListenerUtils.getOriginalText;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
@Data
@AllArgsConstructor
public class TypeVisitor implements GrammarVisitor<Type> {

    private final Context symbols;

    @Override
    public Type visitRoot(final GrammarParser.RootContext ctx) {
        return null;
    }

    @Override
    public Type visitWorld(final GrammarParser.WorldContext ctx) {
        return null;
    }

    @Override
    public Type visitStrategy(final GrammarParser.StrategyContext ctx) {
        return null;
    }

    @Override
    public Type visitDeclaration(final GrammarParser.DeclarationContext ctx) {
        return null;
    }

    @Override
    public Type visitVarDecl(final GrammarParser.VarDeclContext ctx) {
        return null;
    }

    @Override
    public Type visitFctDecl(final GrammarParser.FctDeclContext ctx) {
        if (ctx.scalar() != null) {
            return visitScalar(ctx.scalar());
        }
        if (ctx.VOID() != null) {
            return Type.VOID;
        }

        throw new UnsupportedOperationException(getOriginalText(ctx));
    }

    @Override
    public Type visitVarType(final GrammarParser.VarTypeContext ctx) {
        return null;
    }

    @Override
    public Type visitSkipInstr(final GrammarParser.SkipInstrContext ctx) {
        return null;
    }

    @Override
    public Type visitIfInstr(final GrammarParser.IfInstrContext ctx) {
        return null;
    }

    @Override
    public Type visitWhileInstr(final GrammarParser.WhileInstrContext ctx) {
        return null;
    }

    @Override
    public Type visitSetInstr(final GrammarParser.SetInstrContext ctx) {
        return null;
    }

    @Override
    public Type visitComputeInstr(final GrammarParser.ComputeInstrContext ctx) {
        return null;
    }

    @Override
    public Type visitNextInstr(final GrammarParser.NextInstrContext ctx) {
        return null;
    }

    @Override
    public Type visitBody(final GrammarParser.BodyContext ctx) {
        return null;
    }

    @Override
    public Type visitAction(final GrammarParser.ActionContext ctx) {
        return null;
    }

    @Override
    public Type visitReference(final GrammarParser.ReferenceContext ctx) {
        if (ctx.function() != null) {
            return visitFunction(ctx.function());
        }

        if (ctx.variable() != null) {
            return visitVariable(ctx.variable());
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public Type visitVariable(GrammarParser.VariableContext ctx) {
        return symbols.getVariable(ctx.ID().getText()).getType();
    }

    @Override
    public Type visitFunction(final GrammarParser.FunctionContext ctx) {
        return symbols.getFunction(ctx.ID().getText()).getType();
    }

    @Override
    public Type visitExpression(final GrammarParser.ExpressionContext ctx) {
        return visitSubExpression(ctx.subExpression());
    }

    @Override
    public Type visitSubExpression(final GrammarParser.SubExpressionContext ctx) {

        if (ctx.operation() != null) {
            return Operator.resolve(ctx.operation().getText()).getType();
        }
        return visitValue(ctx.value());
    }

    @Override
    public Type visitOperation(final GrammarParser.OperationContext ctx) {
        return null;
    }

    @Override
    public Type visitValue(final GrammarParser.ValueContext ctx) {

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
            return visitExpression(ctx.expression());
        }

        throw new UnsupportedOperationException(ctx.getText());
    }

    @Override
    public Type visitInteger(final GrammarParser.IntegerContext ctx) {
        return Type.INTEGER;
    }

    @Override
    public Type visitIntVariable(final GrammarParser.IntVariableContext ctx) {
        return Type.INTEGER;
    }

    @Override
    public Type visitPosition(final GrammarParser.PositionContext ctx) {
        return Type.INTEGER;
    }

    @Override
    public Type visitCount(final GrammarParser.CountContext ctx) {
        return Type.INTEGER;
    }

    @Override
    public Type visitBool(final GrammarParser.BoolContext ctx) {
        return Type.BOOLEAN;
    }

    @Override
    public Type visitBoolValue(final GrammarParser.BoolValueContext ctx) {
        return Type.BOOLEAN;
    }

    @Override
    public Type visitBoolLocation(final GrammarParser.BoolLocationContext ctx) {
        return Type.BOOLEAN;
    }

    @Override
    public Type visitBoolNegation(final GrammarParser.BoolNegationContext ctx) {
        return Type.BOOLEAN;
    }

    @Override
    public Type visitSquare(final GrammarParser.SquareContext ctx) {
        return Type.SQUARE;
    }

    @Override
    public Type visitSquareValue(final GrammarParser.SquareValueContext ctx) {
        return Type.SQUARE;
    }

    @Override
    public Type visitSquareNearby(final GrammarParser.SquareNearbyContext ctx) {
        return Type.SQUARE;
    }

    @Override
    public Type visitType(final GrammarParser.TypeContext ctx) {
        if (ctx.scalar() != null) {
            return visitScalar(ctx.scalar());
        }
        if (ctx.array() != null) {
            return visitArray(ctx.array());
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Type visitScalar(final GrammarParser.ScalarContext ctx) {
        return Type.resolve(ctx.getText());
    }

    @Override
    public Type visitArray(final GrammarParser.ArrayContext ctx) {
        return visitScalar(ctx.scalar());
    }

    @Override
    public Type visitImpDecl(final GrammarParser.ImpDeclContext ctx) {
        return null;
    }

    @Override
    public Type visitClauseWhen(final GrammarParser.ClauseWhenContext ctx) {
        return null;
    }

    @Override
    public Type visitClauseDefault(final GrammarParser.ClauseDefaultContext ctx) {
        return null;
    }

    @Override
    public Type visit(final ParseTree tree) {
        return null;
    }

    @Override
    public Type visitChildren(final RuleNode node) {
        return null;
    }

    @Override
    public Type visitTerminal(final TerminalNode node) {
        return null;
    }

    @Override
    public Type visitErrorNode(final ErrorNode node) {
        return null;
    }
}
