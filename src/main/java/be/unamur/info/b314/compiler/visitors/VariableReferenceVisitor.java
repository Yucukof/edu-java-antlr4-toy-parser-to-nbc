package be.unamur.info.b314.compiler.visitors;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.GrammarVisitor;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidNumberOfIndexesException;
import be.unamur.info.b314.compiler.pils.exceptions.VariableNotFoundException;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hadrien BAILLY
 */
public class VariableReferenceVisitor implements GrammarVisitor<VariableReference> {

    private final Context symbols;

    public VariableReferenceVisitor(final Context symbols) {
        this.symbols = symbols;
    }

    @Override
    public VariableReference visitRoot(final GrammarParser.RootContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitWorld(final GrammarParser.WorldContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitStrategy(final GrammarParser.StrategyContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitDeclaration(final GrammarParser.DeclarationContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitVarDecl(final GrammarParser.VarDeclContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitFctDecl(final GrammarParser.FctDeclContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitVarType(final GrammarParser.VarTypeContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitSkipInstr(final GrammarParser.SkipInstrContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitIfInstr(final GrammarParser.IfInstrContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitWhileInstr(final GrammarParser.WhileInstrContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitSetInstr(final GrammarParser.SetInstrContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitComputeInstr(final GrammarParser.ComputeInstrContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitNextInstr(final GrammarParser.NextInstrContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitBody(final GrammarParser.BodyContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitAction(final GrammarParser.ActionContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitReference(final GrammarParser.ReferenceContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitVariable(final GrammarParser.VariableContext ctx) {
        // STEP 1 - Récupérer la variable correspondante
        final String name = ctx.ID().getText();
        final Variable variable = symbols.getVariable(name);

        if (variable == null) {
            throw new VariableNotFoundException(String.format("Cannot find any variable with name [%s]", name));
        }

        // STEP 2 - Vérifier si la référence comporte des indices
        if (ctx.expression().size() > 0) {
            // Si oui, récupérer ces indices
            final ExpressionVisitor expressionVisitor = new ExpressionVisitor(symbols);
            final List<Expression> indexes = ctx.expression().stream().map(expressionVisitor::visitExpression).collect(Collectors.toList());
            // Construire et retourner une référence indicée
            return VariableReference.builder()
                  .variable(variable)
                  .indexes(indexes)
                  .build()
                  .validate();
        } else {
            // STEP 3 - S'il n'y a pas d'indices, vérifier que la variable n'est pas un tableau.
            if (variable.isArray()) {
                throw new InvalidNumberOfIndexesException(String.format("Cannot get non-indexed value from array variable [%s]", variable.getName()));
            }
            // Sinon, construire et retourner une référence sans indice.
            return VariableReference.builder()
                  .variable(variable)
                  .build()
                  .validate();
        }
    }

    @Override
    public VariableReference visitFunction(final GrammarParser.FunctionContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitExpression(final GrammarParser.ExpressionContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitSubExpression(final GrammarParser.SubExpressionContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitOperation(final GrammarParser.OperationContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitValue(final GrammarParser.ValueContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitInteger(final GrammarParser.IntegerContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitIntVariable(final GrammarParser.IntVariableContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitPosition(final GrammarParser.PositionContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitCount(final GrammarParser.CountContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitBool(final GrammarParser.BoolContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitBoolValue(final GrammarParser.BoolValueContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitBoolLocation(final GrammarParser.BoolLocationContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitBoolNegation(final GrammarParser.BoolNegationContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitSquare(final GrammarParser.SquareContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitSquareValue(final GrammarParser.SquareValueContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitSquareNearby(final GrammarParser.SquareNearbyContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitType(final GrammarParser.TypeContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitScalar(final GrammarParser.ScalarContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitArray(final GrammarParser.ArrayContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitImpDecl(final GrammarParser.ImpDeclContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitClauseWhen(final GrammarParser.ClauseWhenContext ctx) {
        return null;
    }

    @Override
    public VariableReference visitClauseDefault(final GrammarParser.ClauseDefaultContext ctx) {
        return null;
    }

    @Override
    public VariableReference visit(final ParseTree tree) {
        return null;
    }

    @Override
    public VariableReference visitChildren(final RuleNode node) {
        return null;
    }

    @Override
    public VariableReference visitTerminal(final TerminalNode node) {
        return null;
    }

    @Override
    public VariableReference visitErrorNode(final ErrorNode node) {
        return null;
    }
}
