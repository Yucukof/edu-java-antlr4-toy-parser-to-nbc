package be.unamur.info.b314.compiler.listeners;

import be.unamur.info.b314.compiler.arena.Arena;
import be.unamur.info.b314.compiler.arena.Coordinates;
import be.unamur.info.b314.compiler.pils.declarations.Function;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.exceptions.*;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.keywords.Property;
import be.unamur.info.b314.compiler.pils.keywords.Reserved;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import static be.unamur.info.b314.compiler.pils.keywords.Property.*;
import static be.unamur.info.b314.compiler.pils.keywords.Square.PLAYER;

/**
 * Un objet de contexte pour la lecture et la décoration d'un arbre syntaxique.
 * <br>
 * Il comprend une table de symboles courante, une pile de table de symboles de portée supérieure, et des références
 * vers les objets-clés (programme en cours d'élaboration, arène pré-chargée, etc.)
 *
 * @author Hadrien BAILLY, Benoît DUVIVIER
 */
@Slf4j
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Context {

    /**
     * Un compteur permettant de donner un identifiant unique à chaque variable.
     */
    private final AtomicInteger counter = new AtomicInteger(0);
    /**
     * Un compteur permettant de connaître le niveau d'indentation.
     */
    private final AtomicInteger indentation = new AtomicInteger(0);
    /**
     * Stack permettant de stocker l'état des variables globales avant de rentrer dans une fonction
     */
    @Builder.Default
    private final Stack<Symbols> stack = new Stack<>();
    /**
     * L'arène précalculée lors de la lecture d'un fichier monde.
     */
    private final Arena arena = new Arena();
    /**
     * La table de symboles courante, qui peut contenir des références vers des symboles à portée globale.
     */
    @Builder.Default
    private Symbols symbols = new Symbols();

    /**
     * Sauvegarde le contexte courant sur la stack, puis initialise un nouveau contexte sans les variables existantes.
     * <br> CAUTION: efface également les {@link Property propriétés} du programme.
     *
     * @see Symbols#insertGameProperty(boolean, AtomicInteger, int)
     */
    public void pushAndReset() {
        log.debug("Resetting context...");
        pushSymbols();
        symbols = new Symbols();
    }

    /**
     * place la table des symboles des variables globales  dans la stack
     */
    private void pushSymbols() {
        stack.push(symbols);
    }

    /**
     * Sauvegarde le contexte courant sur la stack, puis initialise un nouveau contexte reprenant les variables
     * existantes.
     */
    public void pushAndPropagate() {
        log.debug("Propagating context...");
        pushSymbols();
        symbols = new Symbols(symbols);
    }

    /**
     * récupère la table des symboles des variables globales stockée dans la stack
     */
    public void popAndRestore() {
        log.debug("Restoring context...");
        symbols = stack.pop();
    }

    /**
     * Vérifie si la table de symboles courante est dans un état valide, c'est-à-dire que ses tables sont initialisées
     * et ne contiennent que des éléments valides.
     *
     * @return vrai si tables et éléments sont tous valides, faux sinon.
     */
    public boolean isValid() {
        return stack != null && stack.stream().allMatch(Symbols::isValid)
              && symbols.isValid();
    }

    /**
     * Ajoute la fonction à la table des symboles.
     *
     * @param function la fonction à insérer dans la table de symboles.
     * @throws DuplicateNameException si la fonction est déjà définie dans le scope actuel.
     * @throws ReservedNameException  si la fonction utilise un nom réservé.
     */
    public void put(final Function function) {
        symbols.put(function, counter.getAndIncrement(), stack.size());
    }


    /**
     * Ajoute la variable à la table des symboles.
     *
     * @param variable la variable à insérer dans la table de symboles.
     * @throws ReservedNameException           si la variable est un nom réservée.
     * @throws DuplicateNameException          si la variable est déjà définie dans le scope actuel.
     * @throws InvalidDataTypeException        si la variable est uen arène de type non-SQUARE.
     * @throws InvalidNumberOfIndexesException si la variable est une arène avec strictement plus ou moins de 2
     *                                         dimensions.
     * @throws IllegalArgumentException        si la variable est une arène avec une dimension non carrée.
     */
    public void put(final Variable variable) {
        symbols.put(variable, counter.getAndIncrement(), stack.size());
    }

    /**
     * Renvoie une déclaration de fonction présente dans la table des fonctions.
     *
     * @param name le nom de la fonction dont la déclaration doit être récupérée.
     * @return une déclaration de fonction.
     * @throws FunctionNotFoundException si aucune déclaration de fonction n'est disponible à ce niveau.
     */
    public Function getFunction(@NonNull final String name) {
        return symbols.getFunction(name);
    }

    /**
     * Retourne le niveau de profondeur courant, en fonction du nombre de contextes sauvegardés.
     *
     * @return un entier supérieur ou égal à zéro, correspondant au nombre de contextes supérieurs.
     */
    public int getDepth() {
        return stack.size();
    }

    public void insertGameProperty(final boolean withArena) {
        symbols.insertGameProperty(withArena, counter, stack.size());
    }

    /**
     * Associe à l'arène du contexte la variable correspondante dans le tableau de symboles.
     */
    public void setArena() {
        final Variable variable = getVariable(ARENA.getToken());
        arena.setVariable(variable);
    }

    /**
     * Renvoie une déclaration de variable présente dans la table des variables.
     *
     * @param name le nom de la variable dont la déclaration doit être récupérée.
     * @return une déclaration de variable.
     * @throws VariableNotFoundException si aucune déclaration de variable n'est disponible à ce niveau.
     */
    public Variable getVariable(@NonNull final String name) {
        return symbols.getVariable(name);
    }

    /**
     * Attribue aux variables d'environnement leur valeur effective, sur base de l'arène disponible.
     */
    public void setProperties() {

        if (arena.isValid()) {
            final ExpressionLeaf gridSize = new ExpressionLeaf(arena.getSize());
            getVariable(GRIDSIZE.getToken()).set(gridSize);

            final Coordinates player = arena.getCoordinates(PLAYER).orElseThrow(Arena.InvalidArenaException::new);
            final ExpressionLeaf longitude = new ExpressionLeaf(player.getLongitude());
            getVariable(LONGITUDE.getToken()).set(longitude);

            final ExpressionLeaf latitude = new ExpressionLeaf(player.getLatitude());
            getVariable(LATITUDE.getToken()).set(latitude);
        }
    }

    public void canDeclareArena(final boolean arenaVariableAllowed) {
        symbols.canDeclareArena = arenaVariableAllowed;
    }

    /**
     * @author Hadrien BAILLY
     */
    @Slf4j
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Symbols {

        /**
         * table des symboles stockant les déclarations des variables
         */
        @Builder.Default
        private Map<String, Variable> variables = new HashMap<>();
        /**
         * table des symboles stockant les déclarations des fonctions
         */
        @Builder.Default
        private Map<String, Function> functions = new HashMap<>();
        /**
         * Indique si le programme permet de déclarer une arène.
         */
        @Builder.Default
        private boolean canDeclareArena = false;

        /**
         * Un constructeur de tables de symboles par copie.
         *
         * @param symbols les tables de symbole à dupliquer.
         */
        public Symbols(final Symbols symbols) {
            functions = new HashMap<>(symbols.functions);
            variables = new HashMap<>(symbols.variables);
            canDeclareArena = false;
        }

        /**
         * Ajoute la fonction à la table des symboles.
         *
         * @param function la fonction à insérer dans la table de symboles.
         * @throws DuplicateNameException si la fonction est déjà définie dans le scope actuel.
         * @throws ReservedNameException  si la fonction utilise un nom réservé.
         */
        public void put(final Function function, final int id, final int level) {

            // STEP 0 - Vérifier que la fonction est valide
            if (!function.isValid()) {
                throw new InvalidFunctionException(function.getDeclaration());
            }
            // STEP 1 - Récupérer le nom de la fonction
            final String name = function.getName();
            // STEP 2 - Vérifier si le nom donné à la fonction n'est pas un nom réservé
            if (Reserved.isReserved(name)) {
                // Si non, renvoyer une exception
                throw new ReservedNameException(String.format("[%s] is a reserved name and cannot be used for a function", name));
            }
            // STEP 3 - Vérifier si la variable n'est pas déjà déclarée dans le scope courant (càd par une variable de même niveau)
            if (isAlreadyUsed(level, name)) {
                // Si oui, retourner une exception
                throw new DuplicateNameException(String.format("Function %s is already defined", name));
            }
            // STEP 4 - Générer les informations de contexte de la fonction
            final Function contextualisedFunction = function.toBuilder()
                  .id(id)
                  .level(level)
                  .build();
            functions.put(name, contextualisedFunction);
            log.debug("({}) {}", level, function.getDeclaration());
        }

        /**
         * Vérifie si le nom est déjà utilisé dans le scope courant.
         *
         * @param level le niveau d'application du nom.
         * @param name  le nom à contrôler.
         * @return vrai s'il existe déjà une variable de même nom et de même niveau dans le contexte ou une fonction de
         *       même attribut, faux sinon.
         */
        private boolean isAlreadyUsed(final int level, final String name) {
            return (functions.containsKey(name) && functions.get(name).getLevel() == level)
                  || (variables.containsKey(name) && variables.get(name).getLevel() == level);
        }

        /**
         * Ajoute la variable à la table des symboles.
         *
         * @param variable la variable à insérer dans la table de symboles.
         * @throws ReservedNameException           si la variable est un nom réservée.
         * @throws DuplicateNameException          si la variable est déjà définie dans le scope actuel.
         * @throws InvalidDataTypeException        si la variable est uen arène de type non-SQUARE.
         * @throws InvalidNumberOfIndexesException si la variable est une arène avec strictement plus ou moins de 2
         *                                         dimensions.
         * @throws IllegalArgumentException        si la variable est une arène avec une dimension non carrée.
         */
        public void put(@NonNull final Variable variable, final int id, final int level) {

            // STEP 0 - Vérifier que la fonction est valide
            if (!variable.isValid()) {
                throw new InvalidVariableException(variable.getDeclaration());
            }
            // STEP 1 - Récupérer le nom de la variable
            final String name = variable.getName();
            // STEP 2 - Vérifier si le nom donné à la variable n'est pas un nom réservé
            if (Reserved.isReserved(name)) {
                // Vérifier si le nom réservée est celui de l'arène
                if (ARENA.matches(name) && canDeclareArena) {
                    // Si oui, vérifier que l'arène est valide
                    isValidArena(variable);
                } else {
                    // Si non, renvoyer une exception
                    throw new ReservedNameException(String.format("[%s] is a reserved name and cannot be used for a variable", name));
                }
            }
            // STEP 3 - Vérifier si la variable n'est pas déjà déclarée dans le scope courant (càd par une variable de même niveau)
            if (isAlreadyUsed(level, name)) {
                // Si oui, retourner une exception
                throw new DuplicateNameException(String.format("Variable %s is already defined", name));
            }
            // STEP 4 - Générer les informations de contexte de la variable
            variable.setId(id);
            variable.setLevel(level);

            // STEP 5 - Sauvegarder la variable dans la table de symboles
            variables.put(name, variable);
            log.debug("({}) {}", level, variable.getDeclaration());
        }

        /**
         * Vérifie si la variable passée en paramètre est une arène valide et lance une exception si nécessaire.
         *
         * @param variable la variable à vérifier
         * @throws InvalidDataTypeException        si la variable est uen arène de type non-SQUARE.
         * @throws InvalidNumberOfIndexesException si la variable est une arène avec strictement plus ou moins de 2
         *                                         dimensions.
         * @throws IllegalArgumentException        si la variable est une arène avec une dimension non carrée.
         */
        private static void isValidArena(@NonNull final Variable variable) {
            if (ARENA.matches(variable.getName())) {
                if (!variable.getType().equals(Type.SQUARE)) {
                    throw new InvalidDataTypeException(String.format("Cannot declare [%s] with a different type than %s", ARENA.name(), Type.SQUARE));
                }
                if (variable.getDimensions().size() != 2) {
                    throw new InvalidNumberOfIndexesException(String.format("Cannot declare [%s] with dimensions other than 2 (found %s)", ARENA.name(), variable.getDimensions().size()));
                }
                if (!variable.getDimensions().get(0).equals(variable.getDimensions().get(1))) {
                    throw new IllegalArgumentException(String.format("Cannot declare [%s] that is not a square (%s<>%s)", ARENA.name(), variable.getDimensions().get(0), variable.getDimensions().get(1)));
                }
            }
        }

        /**
         * Renvoie une déclaration de fonction présente dans la table des fonctions.
         *
         * @param name le nom de la fonction dont la déclaration doit être récupérée.
         * @return une déclaration de fonction.
         * @throws FunctionNotFoundException si aucune déclaration de fonction n'est disponible à ce niveau.
         */
        public Function getFunction(@NonNull final String name) {
            log.trace("Retrieving function with name [{}]", name);
            // Vérifier si la variable existe
            if (functions.containsKey(name)) {
                // Si oui, la retourner
                return functions.get(name);
            }
            // Si non, renvoyer une exception
            throw new FunctionNotFoundException(String.format("Cannot find function with name [%s]", name));
        }

        /**
         * Renvoie une déclaration de variable présente dans la table des variables.
         *
         * @param name le nom de la variable dont la déclaration doit être récupérée.
         * @return une déclaration de variable.
         * @throws VariableNotFoundException si aucune déclaration de variable n'est disponible à ce niveau.
         */
        public Variable getVariable(@NonNull final String name) {
            log.trace("Retrieving variable with name [{}]", name);
            // Vérifier si la variable existe
            if (variables.containsKey(name)) {
                // Si oui, la retourner
                return variables.get(name);
            }
            // Si non, renvoyer une exception
            throw new VariableNotFoundException(String.format("Cannot find variable with name [%s]", name));
        }


        /**
         * Insère les propriétés du jeu dans les variables.
         *
         * @param withArena marqueur booléen pour filtrer ou non la propriété arena.
         */
        public void insertGameProperty(final boolean withArena, final AtomicInteger counter, final int level) {
            log.debug("Inserting reserved INTEGER keywords...");

            Arrays.stream(values())
                  .filter(value -> !value.equals(ARENA) || withArena)
                  .forEach(value -> {
                            final Variable var = Variable.builder()
                                  .id(counter.getAndIncrement())
                                  .level(level)
                                  .name(value.getToken())
                                  .type(value.getType())
                                  .dimensions(value.getDimensions())
                                  .build();
                            log.debug("{}", var.getDeclaration());
                            variables.putIfAbsent(var.getName(), var);
                        }
                  );
        }

        /**
         * Une méthode de validation du tableau de symboles.
         *
         * @return vrai si les deux tables internes sont non-nulles et ne contiennent que des objets valides, faux sinon.
         */
        public boolean isValid() {
            return variables != null && variables.values().stream().allMatch(Variable::isValid)
                  && functions != null && functions.values().stream().allMatch(Function::isValid);
        }
    }
}
