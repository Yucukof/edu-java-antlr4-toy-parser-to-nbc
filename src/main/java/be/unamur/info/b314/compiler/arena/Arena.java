package be.unamur.info.b314.compiler.arena;

import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.keywords.Square;
import be.unamur.info.b314.compiler.pils.keywords.Target;
import be.unamur.info.b314.compiler.pils.values.Value;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Un objet représentant la surface du plateau de jeu.
 *
 * @author Hadrien BAILLY
 * @see Square
 */
@Slf4j
@Data
public class Arena {

    /**
     * La variable d'environnement correspond à l'arène de jeu.
     */
    private Variable variable;

    /**
     * Vérifie si l'arène courante est valide d'un point de vue sémantique.
     *
     * @return l'arène courante si elle est valide.
     * @throws InvalidArenaException si l'arène ne répond pas à l'un des critères de validité.
     */
    public Arena validate() {

        assertIsValid();

        assertContainsOnlyOne(Square.PLAYER);
        assertContainsOnlyOne(Square.ENEMY);
        assertContainsOnlyOne(Square.GRAAL);
        assertContainsAtLeastOne(Square.ZOMBIE);
        assertContainsAtLeastOne(Square.MAP);
        assertContainsAtLeastOne(Square.RADAR);

        return this;
    }

    /**
     * Vérifie si la variable sous-jacente à l'arène courante est valide conformément à une arène générique.
     *
     * @throws InvalidArenaException si la variable d'arène est invalide.
     */
    private void assertIsValid() {
        if (!isValid()) {
            throw new InvalidArenaException("Arena variable is not valid");
        }
    }

    /**
     * Vérifie si l'arène contient une et une seule instance de la cible donnée parmi son tableau de valeurs.
     *
     * @param target le type de case/cible à rechercher.
     * @throws InvalidArenaException s'il n'y a pas exactement une unique instance de la case/cible.
     */
    private void assertContainsOnlyOne(final Square target) {
        variable.getValues().values().stream()
              .map(Value::getSquareValue)
              .filter(square -> square.equals(target))
              .reduce((a, b) -> {
                  throw new InvalidArenaException(String.format("Arena contains more than 1 instance of [%s]", target));
              })
              .orElseThrow(() -> new InvalidArenaException(String.format("Arena does not contain any instance of [%s]", target)));
    }

    /**
     * Vérifie si l'arène contient au moins une instance de la cible donnée parmi son tableau de valeurs.
     *
     * @param target le type de case/cible à rechercher.
     * @throws InvalidArenaException s'il y a zéro ou une instance de la case/cible.
     */
    private void assertContainsAtLeastOne(final Square target) {
        variable.getValues().values().stream()
              .map(Value::getSquareValue)
              .filter(square -> square.equals(target))
              .findAny()
              .orElseThrow(() -> new InvalidArenaException(String.format("Arena does not contain any instance of [%s]", target)));
    }

    /**
     * Vérifie si la variable répond aux critères de validité d'une variable d'arène:
     * <br> - La variable ne peut pas être nulle.
     * <br> - La variable doit être de type case.
     * <br> - La variable doit être un tableau bi-dimensionnel.
     * <br> - Le tableau doit être rempli uniquement de valeurs de type case.
     * <br> - Le tableau doit être de taille carrée.
     *
     * @return vrai si la variable répond à tous les critères, faux sinon.
     */
    public boolean isValid() {
        return variable != null && variable.isValid()
              && variable.isSquare()
              && variable.getDimensions().size() == 2
              && variable.getValues().values().stream().allMatch(Value::isSquare)
              && variable.getDimensions().stream().distinct().count() == 1;
    }

    /**
     * Retourne la dimension de l'arène courante.
     *
     * @return la première dimension du tableau de la variable sous-jacente.
     */
    public int getSize() {
        return getVariable().getDimensions().get(0);
    }

    /**
     * Renvoie les coordonnées de l'emplacement du joueur sur le tableau de l'arène.
     *
     * @return Une coordonnée composée d'une latitude et d'une longitude.
     * @throws IllegalStateException si l'arène ne contient pas de joueur.
     */
    public Coordinates getPlayerCoordinates() {
        return variable.getValues().entrySet().stream()
              .filter(entry -> entry.getValue().getSquareValue().equals(Square.PLAYER))
              .map(Map.Entry::getKey)
              .map(Coordinates::new)
              .findFirst()
              .orElseThrow(IllegalStateException::new);
    }

    /**
     * Affiche le contenu de la variable d'arène à l'écran, sous forme low-fidelity.
     */
    public void display() {
        final int dimension = variable.getDimensions().get(0);

        final String header = "\t|" + IntStream.range(0, dimension)
              .boxed()
              .map(String::valueOf)
              .collect(Collectors.joining("\t|")) + "\t|\n";

        final String rows = IntStream.range(0, dimension)
              .boxed()
              .map(rowNum -> rowNum + "\t|" + printRow(rowNum, dimension))
              .collect(Collectors.joining("\t|\n"));

        log.info("Map:\n{}{}", header, rows);
    }

    /**
     * Imprime l'une des lignes du tableau de l'arène.
     *
     * @param rowNum    la ligne à imprimer.
     * @param dimension la longueur de la ligne à imprimer.
     * @return Une chaîne de symboles représentant la ligne.
     */
    private String printRow(final int rowNum, final int dimension) {
        final Expression x = new ExpressionLeaf(rowNum);
        return rowNum + "\t|" + IntStream.range(0, dimension)
              .mapToObj(ExpressionLeaf::new)
              .map(i -> Arrays.asList(x, i))
              .map(variable::getAt)
              .map(Optional::ofNullable)
              .map(value -> value.orElse(ExpressionLeaf.DIRT))
              .map(Value::getSquareValue)
              .map(Square::getSymbol)
              .collect(Collectors.joining("\t|"));
    }

    /**
     * Récupère le type de case à l'endroit indiqué.
     *
     * @param target Les coordonnées de l'endroit à inspecter.
     * @return le type de case présente dans le tableau à l'indice donné.
     */
    public Square get(final Coordinates target) {
        final Expression longitude = new ExpressionLeaf(target.getLongitude());
        final Expression latitude = new ExpressionLeaf(target.getLatitude());
        final List<Expression> coordinate = Arrays.asList(longitude, latitude);

        return variable.getAt(coordinate).getSquareValue();
    }

    /**
     * Retourne les coordonnées de l'un des objets présents dans le tableau et équivalent à la cible.
     *
     * @param target le type de cible à retrouver.
     * @return Les coordonnées si l'une des cases correspond au type de cible, sinon rien.
     */
    public Optional<Coordinates> getCoordinates(final Target target) {
        return getCoordinates(target.getSquare());
    }

    /**
     * Retourne les coordonnées de l'un des objets présents dans le tableau et équivalent à la cible.
     *
     * @param square le type de case à retrouver.
     * @return Les coordonnées si l'une des cases correspond au type de cible, sinon rien.
     */
    public Optional<Coordinates> getCoordinates(final Square square) {
        return variable.getValues().entrySet().stream()
              .filter(entry -> entry.getValue().getSquareValue().equals(square))
              .map(Map.Entry::getKey)
              .map(Coordinates::new)
              .findFirst();
    }

    /**
     * Une exception spécifiant que l'arène courante est invalide au sens sémantique.
     */
    public static class InvalidArenaException extends RuntimeException {

        public InvalidArenaException() {
            super();
        }

        public InvalidArenaException(final String message) {
            super(message);
        }
    }
}
