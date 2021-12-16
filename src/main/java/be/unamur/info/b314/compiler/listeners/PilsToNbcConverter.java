package be.unamur.info.b314.compiler.listeners;

import be.unamur.info.b314.compiler.CompilerJob;
import be.unamur.info.b314.compiler.CompilerResult;
import be.unamur.info.b314.compiler.GrammarBaseListener;
import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.pils.clauses.ClauseDefault;
import be.unamur.info.b314.compiler.pils.clauses.ClauseWhen;
import be.unamur.info.b314.compiler.pils.declarations.Declaration;
import be.unamur.info.b314.compiler.pils.declarations.Function;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.exceptions.DivisionByZeroException;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidOperationException;
import be.unamur.info.b314.compiler.pils.exceptions.VariableNotFoundException;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.imports.Import;
import be.unamur.info.b314.compiler.pils.program.Program;
import be.unamur.info.b314.compiler.pils.program.Strategy;
import be.unamur.info.b314.compiler.pils.program.World;
import be.unamur.info.b314.compiler.pils.statements.Statement;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import be.unamur.info.b314.compiler.visitors.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static be.unamur.info.b314.compiler.listeners.ListenerUtils.getOriginalText;

/**
 * Un observateur déclenchant la décoration de l'arbre syntaxique au cours de la lecture du fichier-source.
 *
 * @author Hadrien BAILLY
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class PilsToNbcConverter extends GrammarBaseListener {

    /**
     * Le répertoire contenant les fichiers B314 et WLD à traiter.
     */
    private final Path path;
    /**
     * L'objet contenant les tables des symboles couramment définies (selon le niveau de profondeur) et la stack.
     *
     * @see Context#getDepth()
     */
    private final Context context = new Context();
    /**
     * La stratégie en cours d'élaboration.
     */
    private final Strategy strategy = new Strategy();
    /**
     * Le fichier monde en cours d'élaboration.
     */
    private final World world = new World();
    /**
     * Le programme en cours d'élaboration.
     */
    private Program program;

    @Override
    public void exitRoot(final GrammarParser.RootContext ctx) {
        log.info("Program:\n{}\n", program);
        super.exitRoot(ctx);
    }

    @Override
    public void enterWorld(final GrammarParser.WorldContext ctx) {
        super.enterWorld(ctx);
        log.info("Processing World...");
        program = world;
        log.info("Input:\n{}\n", getOriginalText(ctx));
        this.program = new World();
        context.pushAndReset();
        context.insertGameProperty(false);
        context.canDeclareArena(true);
    }

    @Override
    public void exitWorld(final GrammarParser.WorldContext ctx) {
        // On exécute le programme
        ((World) program).run();
        // On récupère le plateau de jeu constitué par la déclaration du monde et on détruit le contexte.
        context.setArena();
        context.getArena().validate();
        context.popAndRestore();
        context.canDeclareArena(true);
        context.put(context.getArena().getVariable());
        context.canDeclareArena(false);

        log.info("World processed.");
        super.exitWorld(ctx);
    }

    @Override
    public void enterStrategy(final GrammarParser.StrategyContext ctx) {
        super.enterStrategy(ctx);
        log.info("Processing Strategy...");
        program = strategy;
        log.info("Input:\n{}\n", getOriginalText(ctx));
        context.insertGameProperty(false);
    }

    @Override
    public void exitStrategy(final GrammarParser.StrategyContext ctx) {
        log.info("Strategy processed.");
        super.exitStrategy(ctx);
    }

    @Override
    public void enterDeclaration(final GrammarParser.DeclarationContext ctx) {
        super.enterDeclaration(ctx);
        final Declaration declaration = new DeclarationVisitor(context).visitDeclaration(ctx);
        log.trace("Declaration of {} {} added to program.", declaration.getKind(), declaration.getName());
        program.add(declaration);
    }

    /**
     * L'instruction de déclaration de fonction.
     * <br> - Détermine le nom, le type, et les arguments de la fonction.
     * <br> - Vérifie si la fonction est légale (càd encore non-définie).
     * <br> - Sauve la fonction dans la table de symboles des fonctions.
     *
     * @param ctx l'instruction en cours de traitement.
     * @throws IllegalArgumentException si la déclaration est en conflit avec une déclaration précédente.
     * @see Context#getDepth() le niveau d'application de la variable
     */
    @Override
    public void enterFctDecl(final GrammarParser.FctDeclContext ctx) {
        super.enterFctDecl(ctx);
        log.debug("Entering function declaration for {}", ctx.ID().getText());
        final Function function = new FunctionVisitor(context).visitFctDecl(ctx);
        context.put(function);
        log.trace("Declaration of {} {} added to program.", function.getKind(), function.getName());
        context.pushAndPropagate();
    }

    /**
     * L'instruction de retour d'une fonction.
     * <br> - Détermine si une expression doit être retournée
     * <br> - Si oui, vérifie qu'une clause return est présente, et que le type de l'expression retourné correspond au
     * type de la fonction
     * <br> - Si non, vérifie que la clause return est bien absente
     *
     * @param ctx l'instruction en cours de traitement
     * @throws InvalidDataTypeException si le type de l'expression retournée est illégal
     * @throws IllegalArgumentException si une clause return est absente et qu'elle est requise
     * @throws IllegalArgumentException si une clause return est présente, alors qu'elle n'est pas requise
     */
    @Override
    public void exitFctDecl(final GrammarParser.FctDeclContext ctx) {
        // Restaure le contexte à son état avant l'entrée dans la déclaration de fonction
        context.popAndRestore();
        super.exitFctDecl(ctx);
    }

    @Override
    public void enterVarType(final GrammarParser.VarTypeContext ctx) {
        super.enterVarType(ctx);
        // STEP 1 - Récupérer la déclaration de la variable
        final Variable variable = new VariableVisitor(context).visitVarType(ctx);
        // STEP 2 - Stocker la variable dans le contexte
        context.put(variable);
    }

    @Override
    public void enterSkipInstr(final GrammarParser.SkipInstrContext ctx) {
        super.enterSkipInstr(ctx);
        log.debug("Executing SKIP instruction...");
    }

    @Override
    public void enterIfInstr(final GrammarParser.IfInstrContext ctx) {
        super.enterIfInstr(ctx);
        log.debug("Executing IF instruction [if {}]...", getOriginalText(ctx.expression()));

        // STEP 1 - Récupérer l'expression de condition
        final Expression condition = new ExpressionVisitor(context).visitExpression(ctx.expression());
        // STEP 2 - Vérifier si cette condition est bien de type boolean
        if (!condition.getType().equals(Type.BOOLEAN)) {
            throw new InvalidDataTypeException("If condition [" + ctx.expression().getText() + "] is not a boolean expression.");
        }
    }

    @Override
    public void exitIfInstr(final GrammarParser.IfInstrContext ctx) {
        log.debug("IF instruction [if {}] done", getOriginalText(ctx.expression()));

        super.exitIfInstr(ctx);
    }

    @Override
    public void enterWhileInstr(final GrammarParser.WhileInstrContext ctx) {
        super.enterWhileInstr(ctx);
        log.debug("Executing WHILE instruction [while {}]...", getOriginalText(ctx.expression()));
        // STEP 1 - Récupérer l'expression de condition
        final Expression guard = new ExpressionVisitor(context).visitExpression(ctx.expression());
        // STEP 2 - Vérifier si cette condition est bien de type boolean
        if (!guard.isBoolean()) {
            throw new InvalidDataTypeException("Loop condition [" + ctx.expression().getText() + "] is not a boolean expression.");
        }
    }

    @Override
    public void exitWhileInstr(final GrammarParser.WhileInstrContext ctx) {
        log.debug("WHILE instruction [while {}] done.", getOriginalText(ctx.expression()));
        super.exitWhileInstr(ctx);
    }

    /**
     * Instruction d'affectation.
     * <br> - Récupère la variable depuis la table de symboles si existe.
     * <br> - récupère et vérifie le type de la valeur à affecter.
     * <br> - Assigne la valeur.
     *
     * @param ctx l'instruction en cours de traitement
     * @throws InvalidDataTypeException  lorsque le type de la valeur à assigner ne correspond pas au type de la
     *                                   variable-cible.
     * @throws VariableNotFoundException lorsque la variable recherchée n'existe pas/n'est pas disponible à ce niveau de
     *                                   profondeur.
     * @see Context#getDepth() le niveau de profondeur actuel
     */
    @Override
    public void enterSetInstr(final GrammarParser.SetInstrContext ctx) {
        super.enterSetInstr(ctx);
        log.debug("Executing SET instruction [{}]...", getOriginalText(ctx));

        // STEP 1 - On récupère la variable à assigner.
        final VariableReference reference = new VariableReferenceVisitor(context).visitVariable(ctx.variable());
        log.trace("Reference: {}", reference);

        // STEP 2 - On vérifie le type de l'expression correspond à celui de la variable.
        final Expression expression = new ExpressionVisitor(context).visitExpression(ctx.expression());
        log.trace("Expression: {}", expression);
        log.trace("Type: {}", expression.getType());
        if (!expression.getType().equals(reference.getType())) {
            // Si non, alors on renvoie une exception.
            throw new InvalidDataTypeException(String.format("Cannot assign %s value to %s variable [%s]", expression.getType(), reference.getType(), reference.getName()));
        }
        log.trace("Value assigned");
    }

    /**
     * Instruction de calcul.
     * <br> - Évalue une expression donnée, sans en garder le résultat
     *
     * @param ctx l'instruction en cours de traitement
     * @throws InvalidDataTypeException  si l'expression est mal formée.
     * @throws DivisionByZeroException   si l'expresssion tente une division par zéro.
     * @throws InvalidOperationException si l'expression comporte une opération non-reconnue.
     */
    @Override
    public void enterComputeInstr(final GrammarParser.ComputeInstrContext ctx) {
        super.enterComputeInstr(ctx);
        log.debug("Executing COMPUTE instruction [{}]...", getOriginalText(ctx));
        new ExpressionVisitor(context).visitExpression(ctx.expression());
    }

    @Override
    public void enterNextInstr(final GrammarParser.NextInstrContext ctx) {
        super.enterNextInstr(ctx);
        log.debug("Executing NEXT instruction [{}]...", getOriginalText(ctx));
    }

    @Override
    public void enterImpDecl(final GrammarParser.ImpDeclContext ctx) {
        super.enterImpDecl(ctx);

        try {
            final String inputFile = ctx.FILENAME().getText();
            final File file = new File(path.toFile(), inputFile);
            final CompilerJob world = CompilerJob.builder()
                  .directory(path)
                  .inputFile(file)
                  .outputFile(file)
                  .build();

            final CompilerResult result = world.compile();
            result.parseWorld(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void exitImpDecl(final GrammarParser.ImpDeclContext ctx) {
        this.program = strategy;

        final String filename = ctx.FILENAME().getText();
        final Import world = Import.builder()
              .filename(filename)
              .build();

        context.setProperties();
        program.add(world);

        super.exitImpDecl(ctx);
    }

    @Override
    public void enterClauseWhen(final GrammarParser.ClauseWhenContext ctx) {
        super.enterClauseWhen(ctx);
        log.info("Processing clause when [when {}]...", getOriginalText(ctx.expression()));
        // STEP 1 - Récupérer l'expression de condition
        final Expression guard = new ExpressionVisitor(context).visitExpression(ctx.expression());
        // STEP 2 - Vérifier si cette condition est bien de type boolean
        if (!guard.getType().equals(Type.BOOLEAN)) {
            throw new InvalidDataTypeException("When condition [" + ctx.expression().getText() + "] is not a boolean expression.");
        }
        context.pushAndPropagate();
    }

    @Override
    public void exitClauseWhen(final GrammarParser.ClauseWhenContext ctx) {
        log.info("Clause when [when {}] processed.", getOriginalText(ctx.expression()));

        // STEP 1 - Récupérer la liste des déclarations locales
        final VariableVisitor variableVisitor = new VariableVisitor(context);
        final List<Variable> declarations = ctx.varDecl().stream().map(variableVisitor::visitVarDecl).collect(Collectors.toList());

        //STEP 2 - Récupérer la liste des instructions
        final StatementVisitor statementVisitor = new StatementVisitor(context);
        final List<Statement> statements = ctx.instruction().stream()
              .map(statementVisitor::visitInstruction)
              .collect(Collectors.toList());

        context.popAndRestore();

        // STEP 3 - Récupérer l'expression de garde
        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(context);
        final Expression guard = expressionVisitor.visitExpression(ctx.expression());

        // STEP 4 - Construire et ajouter la clause
        final ClauseWhen when = ClauseWhen.builder()
              .guard(guard)
              .declarations(declarations)
              .statements(statements)
              .build();

        program.add(when);

        super.exitClauseWhen(ctx);
    }

    @Override
    public void enterClauseDefault(final GrammarParser.ClauseDefaultContext ctx) {
        super.enterClauseDefault(ctx);
        log.info("Processing clause default...");
        context.pushAndPropagate();
    }

    @Override
    public void exitClauseDefault(final GrammarParser.ClauseDefaultContext ctx) {

        // STEP 1 - Récupérer la liste des déclarations locales
        final VariableVisitor variableVisitor = new VariableVisitor(context);
        final List<Variable> declarations = ctx.varDecl().stream().map(variableVisitor::visitVarDecl).collect(Collectors.toList());

        //STEP 2 - Récupérer la liste des instructions
        final StatementVisitor statementVisitor = new StatementVisitor(context);
        final List<Statement> statements = ctx.instruction().stream().map(statementVisitor::visitInstruction).collect(Collectors.toList());

        context.popAndRestore();

        // STEP 3 - Construire et ajouter la clause
        final ClauseDefault clauseDefault = ClauseDefault.builder()
              .declarations(declarations)
              .statements(statements)
              .build();

        program.add(clauseDefault);
        log.info("Clause default processed.");

        super.exitClauseDefault(ctx);
    }
}
