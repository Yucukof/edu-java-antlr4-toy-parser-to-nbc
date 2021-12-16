package be.unamur.info.b314.compiler.visitors;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.GrammarVisitor;
import be.unamur.info.b314.compiler.pils.declarations.Declaration;
import be.unamur.info.b314.compiler.listeners.Context;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * @author Hadrien BAILLY
 */
public class DeclarationVisitor implements GrammarVisitor<Declaration> {

    private final Context symbols;

    public DeclarationVisitor(final Context symbols) {
        this.symbols = symbols;
    }

    @Override
    public Declaration visitRoot(final GrammarParser.RootContext ctx) {
        return null;
    }

    @Override
    public Declaration visitWorld(final GrammarParser.WorldContext ctx) {
        return null;
    }

    @Override
    public Declaration visitStrategy(final GrammarParser.StrategyContext ctx) {
        return null;
    }

    @Override
    public Declaration visitDeclaration(final GrammarParser.DeclarationContext ctx) {
        if (ctx.varDecl() != null) {
            return visitVarDecl(ctx.varDecl());
        }
        if (ctx.fctDecl() != null) {
            return visitFctDecl(ctx.fctDecl());
        }
        throw new UnsupportedOperationException(ctx.getText());
    }

    @Override
    public Declaration visitVarDecl(final GrammarParser.VarDeclContext ctx) {
        return new VariableVisitor(symbols).visitVarDecl(ctx);
    }

    @Override
    public Declaration visitFctDecl(final GrammarParser.FctDeclContext ctx) {
        return new FunctionVisitor(symbols).visitFctDecl(ctx);
    }

    @Override
    public Declaration visitVarType(final GrammarParser.VarTypeContext ctx) {
        return null;
    }

    @Override
    public Declaration visitSkipInstr(final GrammarParser.SkipInstrContext ctx) {
        return null;
    }

    @Override
    public Declaration visitIfInstr(final GrammarParser.IfInstrContext ctx) {
        return null;
    }

    @Override
    public Declaration visitWhileInstr(final GrammarParser.WhileInstrContext ctx) {
        return null;
    }

    @Override
    public Declaration visitSetInstr(final GrammarParser.SetInstrContext ctx) {
        return null;
    }

    @Override
    public Declaration visitComputeInstr(final GrammarParser.ComputeInstrContext ctx) {
        return null;
    }

    @Override
    public Declaration visitNextInstr(final GrammarParser.NextInstrContext ctx) {
        return null;
    }

    @Override
    public Declaration visitBody(final GrammarParser.BodyContext ctx) {
        return null;
    }

    @Override
    public Declaration visitAction(final GrammarParser.ActionContext ctx) {
        return null;
    }

    @Override
    public Declaration visitReference(final GrammarParser.ReferenceContext ctx) {
        return null;
    }

    @Override
    public Declaration visitVariable(final GrammarParser.VariableContext ctx) {
        return null;
    }

    @Override
    public Declaration visitFunction(final GrammarParser.FunctionContext ctx) {
        return null;
    }

    @Override
    public Declaration visitExpression(final GrammarParser.ExpressionContext ctx) {
        return null;
    }

    @Override
    public Declaration visitSubExpression(final GrammarParser.SubExpressionContext ctx) {
        return null;
    }

    @Override
    public Declaration visitOperation(final GrammarParser.OperationContext ctx) {
        return null;
    }

    @Override
    public Declaration visitValue(final GrammarParser.ValueContext ctx) {
        return null;
    }

    @Override
    public Declaration visitInteger(final GrammarParser.IntegerContext ctx) {
        return null;
    }

    @Override
    public Declaration visitIntVariable(final GrammarParser.IntVariableContext ctx) {
        return null;
    }

    @Override
    public Declaration visitPosition(final GrammarParser.PositionContext ctx) {
        return null;
    }

    @Override
    public Declaration visitCount(final GrammarParser.CountContext ctx) {
        return null;
    }

    @Override
    public Declaration visitBool(final GrammarParser.BoolContext ctx) {
        return null;
    }

    @Override
    public Declaration visitBoolValue(final GrammarParser.BoolValueContext ctx) {
        return null;
    }

    @Override
    public Declaration visitBoolLocation(final GrammarParser.BoolLocationContext ctx) {
        return null;
    }

    @Override
    public Declaration visitBoolNegation(final GrammarParser.BoolNegationContext ctx) {
        return null;
    }

    @Override
    public Declaration visitSquare(final GrammarParser.SquareContext ctx) {
        return null;
    }

    @Override
    public Declaration visitSquareValue(final GrammarParser.SquareValueContext ctx) {
        return null;
    }

    @Override
    public Declaration visitSquareNearby(final GrammarParser.SquareNearbyContext ctx) {
        return null;
    }

    @Override
    public Declaration visitType(final GrammarParser.TypeContext ctx) {
        return null;
    }

    @Override
    public Declaration visitScalar(final GrammarParser.ScalarContext ctx) {
        return null;
    }

    @Override
    public Declaration visitArray(final GrammarParser.ArrayContext ctx) {
        return null;
    }

    @Override
    public Declaration visitImpDecl(final GrammarParser.ImpDeclContext ctx) {
        return null;
    }

    @Override
    public Declaration visitClauseWhen(final GrammarParser.ClauseWhenContext ctx) {
        return null;
    }

    @Override
    public Declaration visitClauseDefault(final GrammarParser.ClauseDefaultContext ctx) {
        return null;
    }

    @Override
    public Declaration visit(final ParseTree tree) {
        return null;
    }

    @Override
    public Declaration visitChildren(final RuleNode node) {
        return null;
    }

    @Override
    public Declaration visitTerminal(final TerminalNode node) {
        return null;
    }

    @Override
    public Declaration visitErrorNode(final ErrorNode node) {
        return null;
    }
}
