package be.unamur.info.b314.compiler.visitors;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.GrammarVisitor;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * @author Hadrien BAILLY
 */
public class OperatorVisitor implements GrammarVisitor<Operator> {

    @Override
    public Operator visitRoot(final GrammarParser.RootContext ctx) {
        return null;
    }

    @Override
    public Operator visitWorld(final GrammarParser.WorldContext ctx) {
        return null;
    }

    @Override
    public Operator visitStrategy(final GrammarParser.StrategyContext ctx) {
        return null;
    }

    @Override
    public Operator visitDeclaration(final GrammarParser.DeclarationContext ctx) {
        return null;
    }

    @Override
    public Operator visitVarDecl(final GrammarParser.VarDeclContext ctx) {
        return null;
    }

    @Override
    public Operator visitFctDecl(final GrammarParser.FctDeclContext ctx) {
        return null;
    }

    @Override
    public Operator visitVarType(final GrammarParser.VarTypeContext ctx) {
        return null;
    }

    @Override
    public Operator visitSkipInstr(final GrammarParser.SkipInstrContext ctx) {
        return null;
    }

    @Override
    public Operator visitIfInstr(final GrammarParser.IfInstrContext ctx) {
        return null;
    }

    @Override
    public Operator visitWhileInstr(final GrammarParser.WhileInstrContext ctx) {
        return null;
    }

    @Override
    public Operator visitSetInstr(final GrammarParser.SetInstrContext ctx) {
        return null;
    }

    @Override
    public Operator visitComputeInstr(final GrammarParser.ComputeInstrContext ctx) {
        return null;
    }

    @Override
    public Operator visitNextInstr(final GrammarParser.NextInstrContext ctx) {
        return null;
    }

    @Override
    public Operator visitBody(final GrammarParser.BodyContext ctx) {
        return null;
    }

    @Override
    public Operator visitAction(final GrammarParser.ActionContext ctx) {
        return null;
    }

    @Override
    public Operator visitExpression(final GrammarParser.ExpressionContext ctx) {
        return visitSubExpression(ctx.subExpression());
    }

    @Override
    public Operator visitSubExpression(final GrammarParser.SubExpressionContext ctx) {
        if (ctx.operation() != null) {
            return visitOperation(ctx.operation());
        }
        throw new UnsupportedOperationException(ctx.getText());
    }

    @Override
    public Operator visitOperation(final GrammarParser.OperationContext ctx) {
        return Operator.resolve(ctx.getText());
    }

    @Override
    public Operator visitValue(final GrammarParser.ValueContext ctx) {
        if (ctx.reference() != null) {
            return visitReference(ctx.reference());
        }
        throw new UnsupportedOperationException(ctx.getText());
    }

    @Override
    public Operator visitReference(final GrammarParser.ReferenceContext ctx) {
        if (ctx.function() != null) {
            return visitFunction(ctx.function());
        }
        throw new UnsupportedOperationException(ctx.getText());
    }

    @Override
    public Operator visitFunction(final GrammarParser.FunctionContext ctx) {
        throw new UnsupportedOperationException(ctx.getText());
    }

    @Override
    public Operator visitInteger(final GrammarParser.IntegerContext ctx) {
        return null;
    }

    @Override
    public Operator visitIntVariable(final GrammarParser.IntVariableContext ctx) {
        return null;
    }

    @Override
    public Operator visitPosition(final GrammarParser.PositionContext ctx) {
        return null;
    }

    @Override
    public Operator visitCount(final GrammarParser.CountContext ctx) {
        return null;
    }

    @Override
    public Operator visitBool(final GrammarParser.BoolContext ctx) {
        return null;
    }

    @Override
    public Operator visitBoolValue(final GrammarParser.BoolValueContext ctx) {
        return null;
    }

    @Override
    public Operator visitBoolLocation(final GrammarParser.BoolLocationContext ctx) {
        return null;
    }

    @Override
    public Operator visitBoolNegation(final GrammarParser.BoolNegationContext ctx) {
        return null;
    }

    @Override
    public Operator visitSquare(final GrammarParser.SquareContext ctx) {
        return null;
    }

    @Override
    public Operator visitSquareValue(final GrammarParser.SquareValueContext ctx) {
        return null;
    }

    @Override
    public Operator visitSquareNearby(final GrammarParser.SquareNearbyContext ctx) {
        return null;
    }

    @Override
    public Operator visitType(final GrammarParser.TypeContext ctx) {
        return null;
    }

    @Override
    public Operator visitScalar(final GrammarParser.ScalarContext ctx) {
        return null;
    }

    @Override
    public Operator visitArray(final GrammarParser.ArrayContext ctx) {
        return null;
    }

    @Override
    public Operator visitImpDecl(final GrammarParser.ImpDeclContext ctx) {
        return null;
    }

    @Override
    public Operator visitClauseWhen(final GrammarParser.ClauseWhenContext ctx) {
        return null;
    }

    @Override
    public Operator visitClauseDefault(final GrammarParser.ClauseDefaultContext ctx) {
        return null;
    }

    @Override
    public Operator visit(final ParseTree tree) {
        return null;
    }

    @Override
    public Operator visitChildren(final RuleNode node) {
        return null;
    }

    @Override
    public Operator visitTerminal(final TerminalNode node) {
        return null;
    }

    @Override
    public Operator visitErrorNode(final ErrorNode node) {
        return null;
    }

    @Override
    public Operator visitVariable(final GrammarParser.VariableContext ctx) {
        return null;
    }
}
