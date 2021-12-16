package be.unamur.info.b314.compiler.visitors;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.GrammarVisitor;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.pils.declarations.Function;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidFunctionDeclarationException;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static be.unamur.info.b314.compiler.listeners.ListenerUtils.getOriginalText;

/**
 * @author Benoît DUVIVIER
 */
@Slf4j
@Data
@AllArgsConstructor
public class FunctionVisitor implements GrammarVisitor<Function> {

    private final Context symbols;

    @Override
    public Function visitRoot(final GrammarParser.RootContext ctx) {
        return null;
    }

    @Override
    public Function visitWorld(final GrammarParser.WorldContext ctx) {
        return null;
    }

    @Override
    public Function visitStrategy(final GrammarParser.StrategyContext ctx) {
        return null;
    }

    @Override
    public Function visitDeclaration(final GrammarParser.DeclarationContext ctx) {
        return null;
    }

    @Override
    public Function visitVarDecl(final GrammarParser.VarDeclContext ctx) {
        return visitVarType(ctx.varType());
    }

    @Override
    public Function visitFctDecl(final GrammarParser.FctDeclContext ctx) {
        log.debug("Processing function declaration [{}]", getOriginalText(ctx));

        // STEP 0 - initialiser la visite de la fonction en sauvegardant l'état de la stack
        symbols.pushAndPropagate();

        // STEP 1 - récupérer le nom de la fonction
        final String name = ctx.ID().getText();

        // STEP 2 - récupérer le type déclaré de la fonction
        final Type type = new TypeVisitor(symbols).visitFctDecl(ctx);

        // STEP 3 - récupérer la liste des arguments
        final VariableVisitor variableVisitor = new VariableVisitor(symbols);
        final List<Variable> arguments = ctx.varType().stream().map(variableVisitor::visitVarType).collect(Collectors.toList());
        arguments.forEach(symbols::put);

        // STEP 4 - récupérer la liste des déclarations
        final List<Variable> declarations = ctx.varDecl().stream().map(variableVisitor::visitVarDecl).collect(Collectors.toList());
        declarations.forEach(symbols::put);

        // STEP 5 - Stocker une déclaration temporaire de la fonction
        final Function function = Function.builder()
                .name(name)
                .type(type)
                .arguments(arguments)
                .declarations(declarations)
                .output(type.getDefault())
                .build();
        symbols.put(function);

        // STEP 6 - récupérer la liste des instructions
        final StatementVisitor statementVisitor = new StatementVisitor(symbols);
        ctx.instruction().stream()
                .map(statementVisitor::visitInstruction)
                .forEach(function::add);

        // STEP 7 - récupérer l'expression de retour
        final Expression expression = Optional.ofNullable(ctx.expression())
                .map(expr -> new ExpressionVisitor(symbols).visitExpression(expr))
                .orElse(ExpressionLeaf.VOID);

        // STEP 8 - Vérifier si le type de retour est compatible
        // Vérifier exclusivement si la fonction a un retour ou si la fonction est de type VOID.
        if (type.equals(Type.VOID) ^ expression.isVoid()) {
            throw new InvalidFunctionDeclarationException(String.format("%s function \"%s\" %s have a return expression.", type.getToken(), name, expression.isVoid() ? "must" : "cannot"));
        }
        // Vérifier si le type de retour
        if (!type.equals(expression.getType())) {
            throw new InvalidFunctionDeclarationException(String.format("%s function has a %s return", type, expression.getType()));
        }

        function.setOutput(expression);

        // STEP 9 - restaurer l'état de la stack
        symbols.popAndRestore();

        // STEP 10 - construire et retourner la fonction
        return function;
    }

    @Override
    public Function visitVarType(final GrammarParser.VarTypeContext ctx) {
        return visitType(ctx.type());
    }

    @Override
    public Function visitSkipInstr(final GrammarParser.SkipInstrContext ctx) {
        return null;
    }

    @Override
    public Function visitIfInstr(final GrammarParser.IfInstrContext ctx) {
        return null;
    }

    @Override
    public Function visitWhileInstr(final GrammarParser.WhileInstrContext ctx) {
        return null;
    }

    @Override
    public Function visitSetInstr(final GrammarParser.SetInstrContext ctx) {
        return null;
    }

    @Override
    public Function visitComputeInstr(final GrammarParser.ComputeInstrContext ctx) {
        return null;
    }

    @Override
    public Function visitNextInstr(final GrammarParser.NextInstrContext ctx) {
        return null;
    }

    @Override
    public Function visitBody(GrammarParser.BodyContext ctx) {
        return null;
    }

    @Override
    public Function visitAction(final GrammarParser.ActionContext ctx) {
        return null;
    }

    @Override
    public Function visitReference(final GrammarParser.ReferenceContext ctx) {
        if (ctx.variable() != null) {
            return visitVariable(ctx.variable());
        }
        if (ctx.function() != null) {
            return visitFunction(ctx.function());
        }
        return null;
    }

    @Override
    public Function visitVariable(final GrammarParser.VariableContext ctx) {
        return null;
    }

    @Override
    public Function visitFunction(final GrammarParser.FunctionContext ctx) {
        return null;
    }

    @Override
    public Function visitExpression(final GrammarParser.ExpressionContext ctx) {
        return null;
    }

    @Override
    public Function visitSubExpression(final GrammarParser.SubExpressionContext ctx) {
        if (ctx.value() != null) {
            return visitValue(ctx.value());
        }
        throw new UnsupportedOperationException(ctx.getText());
    }

    @Override
    public Function visitOperation(final GrammarParser.OperationContext ctx) {
        return null;
    }

    @Override
    public Function visitValue(final GrammarParser.ValueContext ctx) {
        return null;
    }

    @Override
    public Function visitInteger(final GrammarParser.IntegerContext ctx) {
        return null;
    }

    @Override
    public Function visitIntVariable(final GrammarParser.IntVariableContext ctx) {
        return null;
    }

    @Override
    public Function visitPosition(GrammarParser.PositionContext ctx) {
        return null;
    }

    @Override
    public Function visitCount(GrammarParser.CountContext ctx) {
        return null;
    }

    @Override
    public Function visitBool(final GrammarParser.BoolContext ctx) {
        return null;
    }

    @Override
    public Function visitBoolValue(GrammarParser.BoolValueContext ctx) {
        return null;
    }

    @Override
    public Function visitBoolLocation(GrammarParser.BoolLocationContext ctx) {
        return null;
    }

    @Override
    public Function visitBoolNegation(GrammarParser.BoolNegationContext ctx) {
        return null;
    }

    @Override
    public Function visitSquare(final GrammarParser.SquareContext ctx) {
        return null;
    }

    @Override
    public Function visitSquareValue(GrammarParser.SquareValueContext ctx) {
        return null;
    }

    @Override
    public Function visitSquareNearby(GrammarParser.SquareNearbyContext ctx) {
        return null;
    }

    @Override
    public Function visitType(final GrammarParser.TypeContext ctx) {

        if (ctx.scalar() != null) {
            return visitScalar(ctx.scalar());
        }

        throw new IllegalStateException("Invalid TypeContext - Should not happen.");
    }

    @Override
    public Function visitScalar(final GrammarParser.ScalarContext ctx) {

        final Type type = Type.resolve(ctx.getText());

        return Function.builder()
                .type(type)
                .build();
    }

    @Override
    public Function visitArray(final GrammarParser.ArrayContext ctx) {
        return null;
    }

    @Override
    public Function visitImpDecl(final GrammarParser.ImpDeclContext ctx) {
        return null;
    }

    @Override
    public Function visitClauseWhen(final GrammarParser.ClauseWhenContext ctx) {
        return null;
    }

    @Override
    public Function visitClauseDefault(final GrammarParser.ClauseDefaultContext ctx) {
        return null;
    }

    @Override
    public Function visit(final ParseTree tree) {
        return null;
    }

    @Override
    public Function visitChildren(final RuleNode node) {
        return null;
    }

    @Override
    public Function visitTerminal(final TerminalNode node) {
        return null;
    }

    @Override
    public Function visitErrorNode(final ErrorNode node) {
        return null;
    }
}

