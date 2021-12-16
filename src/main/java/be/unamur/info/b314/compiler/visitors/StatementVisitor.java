package be.unamur.info.b314.compiler.visitors;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.GrammarVisitor;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.pils.actions.Action;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.statements.*;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import lombok.Data;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Hadrien BAILLY
 */
@Data
public class StatementVisitor implements GrammarVisitor<Statement> {

    private final Context symbols;

    @Override
    public Statement visitRoot(final GrammarParser.RootContext ctx) {
        return null;
    }

    @Override
    public Statement visitWorld(final GrammarParser.WorldContext ctx) {
        return null;
    }

    @Override
    public Statement visitStrategy(final GrammarParser.StrategyContext ctx) {
        return null;
    }

    @Override
    public Statement visitDeclaration(final GrammarParser.DeclarationContext ctx) {
        return null;
    }

    @Override
    public Statement visitVarDecl(final GrammarParser.VarDeclContext ctx) {
        return null;
    }

    @Override
    public Statement visitFctDecl(final GrammarParser.FctDeclContext ctx) {
        return null;
    }

    @Override
    public Statement visitVarType(final GrammarParser.VarTypeContext ctx) {
        return null;
    }

    @Override
    public Statement visitSkipInstr(final GrammarParser.SkipInstrContext ctx) {
        return new StatementSkip();
    }

    @Override
    public Statement visitIfInstr(final GrammarParser.IfInstrContext ctx) {
        final Expression guard = new ExpressionVisitor(symbols).visitExpression(ctx.expression());

        final AtomicInteger indentation = symbols.getIndentation();
        indentation.incrementAndGet();

        assert ctx.body().size() > 0;
        final List<Statement> branchTrue = ctx.body(0).instruction().stream().map(this::visitInstruction).collect(Collectors.toList());

        final List<Statement> branchFalse;
        if (ctx.body().size() > 1) {
            branchFalse = ctx.body(1).instruction().stream().map(this::visitInstruction).collect(Collectors.toList());
        } else {
            branchFalse = Collections.emptyList();
        }

        return StatementIf.builder()
              .indentation(indentation.decrementAndGet())
              .guard(guard)
              .branchTrue(branchTrue)
              .branchFalse(branchFalse)
              .build();
    }

    @Override
    public Statement visitWhileInstr(final GrammarParser.WhileInstrContext ctx) {
        final Expression guard = new ExpressionVisitor(symbols).visitExpression(ctx.expression());

        final AtomicInteger indentation = symbols.getIndentation();
        indentation.incrementAndGet();

        final List<Statement> body = ctx.body().instruction()
              .stream()
              .map(this::visitInstruction)
              .collect(Collectors.toList());

        return StatementWhile.builder()
              .indentation(indentation.decrementAndGet())
              .guard(guard)
              .statements(body)
              .build();
    }

    @Override
    public Statement visitSetInstr(final GrammarParser.SetInstrContext ctx) {
        final VariableReference element = new VariableReferenceVisitor(symbols).visitVariable(ctx.variable());
        final Expression expression = new ExpressionVisitor(symbols).visitExpression(ctx.expression());

        return StatementSet.builder()
              .reference(element)
              .expression(expression)
              .build();
    }

    @Override
    public Statement visitComputeInstr(final GrammarParser.ComputeInstrContext ctx) {
        final Expression expression = new ExpressionVisitor(symbols).visitExpression(ctx.expression());

        return StatementCompute.builder()
              .expression(expression)
              .build();
    }

    @Override
    public Statement visitNextInstr(final GrammarParser.NextInstrContext ctx) {
        final Action action = new ActionVisitor(symbols).visitAction(ctx.action());

        return StatementNext.builder()
              .action(action)
              .build();
    }

    @Override
    public Statement visitBody(final GrammarParser.BodyContext ctx) {
        return null;
    }

    @Override
    public Statement visitAction(final GrammarParser.ActionContext ctx) {
        return null;
    }

    @Override
    public Statement visitReference(final GrammarParser.ReferenceContext ctx) {
        return null;
    }

    @Override
    public Statement visitVariable(final GrammarParser.VariableContext ctx) {
        return null;
    }

    @Override
    public Statement visitFunction(final GrammarParser.FunctionContext ctx) {
        return null;
    }

    @Override
    public Statement visitExpression(final GrammarParser.ExpressionContext ctx) {
        return null;
    }

    @Override
    public Statement visitSubExpression(final GrammarParser.SubExpressionContext ctx) {
        return null;
    }

    @Override
    public Statement visitOperation(final GrammarParser.OperationContext ctx) {
        return null;
    }

    @Override
    public Statement visitValue(final GrammarParser.ValueContext ctx) {
        return null;
    }

    @Override
    public Statement visitInteger(final GrammarParser.IntegerContext ctx) {
        return null;
    }

    @Override
    public Statement visitIntVariable(final GrammarParser.IntVariableContext ctx) {
        return null;
    }

    @Override
    public Statement visitPosition(final GrammarParser.PositionContext ctx) {
        return null;
    }

    @Override
    public Statement visitCount(final GrammarParser.CountContext ctx) {
        return null;
    }

    @Override
    public Statement visitBool(final GrammarParser.BoolContext ctx) {
        return null;
    }

    @Override
    public Statement visitBoolValue(final GrammarParser.BoolValueContext ctx) {
        return null;
    }

    @Override
    public Statement visitBoolLocation(final GrammarParser.BoolLocationContext ctx) {
        return null;
    }

    @Override
    public Statement visitBoolNegation(final GrammarParser.BoolNegationContext ctx) {
        return null;
    }

    @Override
    public Statement visitSquare(final GrammarParser.SquareContext ctx) {
        return null;
    }

    @Override
    public Statement visitSquareValue(final GrammarParser.SquareValueContext ctx) {
        return null;
    }

    @Override
    public Statement visitSquareNearby(final GrammarParser.SquareNearbyContext ctx) {
        return null;
    }

    @Override
    public Statement visitType(final GrammarParser.TypeContext ctx) {
        return null;
    }

    @Override
    public Statement visitScalar(final GrammarParser.ScalarContext ctx) {
        return null;
    }

    @Override
    public Statement visitArray(final GrammarParser.ArrayContext ctx) {
        return null;
    }

    @Override
    public Statement visitImpDecl(final GrammarParser.ImpDeclContext ctx) {
        return null;
    }

    @Override
    public Statement visitClauseWhen(final GrammarParser.ClauseWhenContext ctx) {
        return null;
    }

    @Override
    public Statement visitClauseDefault(final GrammarParser.ClauseDefaultContext ctx) {
        return null;
    }

    public Statement visitInstruction(final GrammarParser.InstructionContext ctx) {
        if (ctx instanceof GrammarParser.IfInstrContext) {
            return visitIfInstr((GrammarParser.IfInstrContext) ctx);
        }
        if (ctx instanceof GrammarParser.ComputeInstrContext) {
            return visitComputeInstr((GrammarParser.ComputeInstrContext) ctx);
        }
        if (ctx instanceof GrammarParser.SetInstrContext) {
            return visitSetInstr((GrammarParser.SetInstrContext) ctx);
        }
        if (ctx instanceof GrammarParser.WhileInstrContext) {
            return visitWhileInstr((GrammarParser.WhileInstrContext) ctx);
        }
        if (ctx instanceof GrammarParser.NextInstrContext) {
            return visitNextInstr((GrammarParser.NextInstrContext) ctx);
        }
        if (ctx instanceof GrammarParser.SkipInstrContext) {
            return visitSkipInstr((GrammarParser.SkipInstrContext) ctx);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Statement visit(final ParseTree tree) {
        return null;
    }

    @Override
    public Statement visitChildren(final RuleNode node) {
        return null;
    }

    @Override
    public Statement visitTerminal(final TerminalNode node) {
        return null;
    }

    @Override
    public Statement visitErrorNode(final ErrorNode node) {
        return null;
    }
}
