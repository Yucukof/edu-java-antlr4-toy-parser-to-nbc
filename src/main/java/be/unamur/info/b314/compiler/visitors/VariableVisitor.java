package be.unamur.info.b314.compiler.visitors;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.GrammarVisitor;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.exceptions.NegativeArrayIndexException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static be.unamur.info.b314.compiler.listeners.ListenerUtils.getOriginalText;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
@Data
@AllArgsConstructor
public class VariableVisitor implements GrammarVisitor<Variable> {

    private final Context symbols;

    @Override
    public Variable visitRoot(final GrammarParser.RootContext ctx) {
        return null;
    }

    @Override
    public Variable visitWorld(final GrammarParser.WorldContext ctx) {
        return null;
    }

    @Override
    public Variable visitStrategy(final GrammarParser.StrategyContext ctx) {
        return null;
    }

    @Override
    public Variable visitDeclaration(final GrammarParser.DeclarationContext ctx) {
        return null;
    }

    @Override
    public Variable visitVarDecl(final GrammarParser.VarDeclContext ctx) {
        return visitVarType(ctx.varType());
    }

    @Override
    public Variable visitFctDecl(final GrammarParser.FctDeclContext ctx) {
        return null;
    }

    @Override
    public Variable visitVarType(final GrammarParser.VarTypeContext ctx) {
        log.debug("Processing variable declaration [{}]", getOriginalText(ctx));
        return visitType(ctx.type()).toBuilder()
              .name(ctx.ID().getText())
              .build();
    }

    @Override
    public Variable visitSkipInstr(final GrammarParser.SkipInstrContext ctx) {
        return null;
    }

    @Override
    public Variable visitIfInstr(final GrammarParser.IfInstrContext ctx) {
        return null;
    }

    @Override
    public Variable visitWhileInstr(final GrammarParser.WhileInstrContext ctx) {
        return null;
    }

    @Override
    public Variable visitSetInstr(final GrammarParser.SetInstrContext ctx) {
        return visitVariable(ctx.variable());
    }

    @Override
    public Variable visitComputeInstr(final GrammarParser.ComputeInstrContext ctx) {
        return null;
    }

    @Override
    public Variable visitNextInstr(final GrammarParser.NextInstrContext ctx) {
        return null;
    }

    @Override
    public Variable visitBody(final GrammarParser.BodyContext ctx) {
        return null;
    }

    @Override
    public Variable visitAction(final GrammarParser.ActionContext ctx) {
        return null;
    }

    @Override
    public Variable visitReference(final GrammarParser.ReferenceContext ctx) {
        if (ctx.variable() != null) {
            return visitVariable(ctx.variable());
        }
        if (ctx.function() != null) {
            return visitFunction(ctx.function());
        }
        return null;
    }

    @Override
    public Variable visitVariable(final GrammarParser.VariableContext ctx) {
        return null;
    }

    @Override
    public Variable visitFunction(final GrammarParser.FunctionContext ctx) {
        return null;
    }

    @Override
    public Variable visitExpression(final GrammarParser.ExpressionContext ctx) {
        return null;
    }

    @Override
    public Variable visitSubExpression(final GrammarParser.SubExpressionContext ctx) {
        return null;
    }

    @Override
    public Variable visitOperation(final GrammarParser.OperationContext ctx) {
        return null;
    }

    @Override
    public Variable visitValue(final GrammarParser.ValueContext ctx) {
        return null;
    }

    @Override
    public Variable visitInteger(final GrammarParser.IntegerContext ctx) {
        return null;
    }

    @Override
    public Variable visitIntVariable(final GrammarParser.IntVariableContext ctx) {
        return null;
    }

    @Override
    public Variable visitPosition(final GrammarParser.PositionContext ctx) {
        return null;
    }

    @Override
    public Variable visitCount(final GrammarParser.CountContext ctx) {
        return null;
    }

    @Override
    public Variable visitBool(final GrammarParser.BoolContext ctx) {
        return null;
    }

    @Override
    public Variable visitBoolValue(final GrammarParser.BoolValueContext ctx) {
        return null;
    }

    @Override
    public Variable visitBoolLocation(final GrammarParser.BoolLocationContext ctx) {
        return null;
    }

    @Override
    public Variable visitBoolNegation(final GrammarParser.BoolNegationContext ctx) {
        return null;
    }

    @Override
    public Variable visitSquare(final GrammarParser.SquareContext ctx) {
        return null;
    }

    @Override
    public Variable visitSquareValue(final GrammarParser.SquareValueContext ctx) {
        return null;
    }

    @Override
    public Variable visitSquareNearby(final GrammarParser.SquareNearbyContext ctx) {
        return null;
    }

    @Override
    public Variable visitType(final GrammarParser.TypeContext ctx) {
        if (ctx.scalar() != null) {
            return visitScalar(ctx.scalar());
        }

        if (ctx.array() != null) {
            return visitArray(ctx.array());
        }

        throw new IllegalStateException(getOriginalText(ctx));
    }

    @Override
    public Variable visitScalar(final GrammarParser.ScalarContext ctx) {
        // STEP 1 - Récupérer le type déclaré
        final Type type = new TypeVisitor(symbols).visitScalar(ctx);
        // STEP 2 - Initialiser les indices à une collection vide.
        final List<Integer> dimensions = new ArrayList<>();
        // STEP 3 - Construire et retourner la variable
        final Variable variable = Variable.builder()
              .type(type)
              .dimensions(dimensions)
              .build();
        variable.set(variable.getType().getDefault().getValue());
        return variable;
    }

    @Override
    public Variable visitArray(final GrammarParser.ArrayContext ctx) {
        // STEP 1 - Récupérer le type déclaré.
        final Type type = new TypeVisitor(symbols).visitScalar(ctx.scalar());

        // STEP 2 - Récupérer la liste des dimensions.
        final List<Integer> dimensions = ctx.NUMBER().stream()
              .map(number -> Integer.parseInt(number.getText()))
              .collect(Collectors.toList());

        // STEP 3 - Vérifier que les dimensions sont acceptables
        if (dimensions.stream().anyMatch(i -> i < 0)) {
            throw new NegativeArrayIndexException(String.format("Negative index(es) found in variable declaration: [%s]", dimensions.stream().map(String::valueOf).collect(Collectors.joining(","))));
        }

        // STEP 4 - Construire et retourner la variable.
        return Variable.builder()
              .type(type)
              .dimensions(dimensions)
              .build();
    }

    @Override
    public Variable visitImpDecl(final GrammarParser.ImpDeclContext ctx) {
        return null;
    }

    @Override
    public Variable visitClauseWhen(final GrammarParser.ClauseWhenContext ctx) {
        return null;
    }

    @Override
    public Variable visitClauseDefault(final GrammarParser.ClauseDefaultContext ctx) {
        return null;
    }

    @Override
    public Variable visit(final ParseTree tree) {
        return null;
    }

    @Override
    public Variable visitChildren(final RuleNode node) {
        return null;
    }

    @Override
    public Variable visitTerminal(final TerminalNode node) {
        return null;
    }

    @Override
    public Variable visitErrorNode(final ErrorNode node) {
        return null;
    }
}
