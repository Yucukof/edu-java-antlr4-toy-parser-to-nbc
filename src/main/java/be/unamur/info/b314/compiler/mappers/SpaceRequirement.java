package be.unamur.info.b314.compiler.mappers;

import be.unamur.info.b314.compiler.nbc.declarations.Segment;
import be.unamur.info.b314.compiler.nbc.definitions.DefinitionVariable;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Un prérequis-mémoire indique le nombre de registres de chaque type nécessaires à l'exécution du programme courant
 * dans le pire des cas d'utilisation.
 *
 * @author Hadrien BAILLY
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class SpaceRequirement {

    /**
     * Un raccourci pour produire un prérequis vide.
     */
    public static SpaceRequirement NONE = new SpaceRequirement();

    /**
     * Un compteur du nombre de registres nécessaires pouvant contenir des entiers.
     */
    private final int integer;
    /**
     * Un compteur du nombre de registres nécessaires pouvant contenir des booléens.
     */
    private final int bool;
    /**
     * Un compteur du nombre de registres nécessaires pouvant contenir des cases.
     */
    private final int square;

    /**
     * Un constructeur par défaut, qui initialise les besoins à zéro registres dans toutes les catégories.
     */
    public SpaceRequirement() {
        this.integer = 0;
        this.bool = 0;
        this.square = 0;
    }

    /**
     * Fusionne deux prérequis-mémoire en conservant la valeur la plus haute de chacun des prérequis.
     *
     * @param requirements la liste des prérequis à combiner.
     * @return un nouveau prérequis-mémoire.
     */
    public static SpaceRequirement merge(final SpaceRequirement... requirements) {
        final int integer = Arrays.stream(requirements).map(SpaceRequirement::getInteger).max(Integer::compareTo).orElse(0);
        final int bool = Arrays.stream(requirements).map(SpaceRequirement::getBool).max(Integer::compareTo).orElse(0);
        final int square = Arrays.stream(requirements).map(SpaceRequirement::getSquare).max(Integer::compareTo).orElse(0);

        return SpaceRequirement.builder()
              .integer(integer)
              .bool(bool)
              .square(square)
              .build();
    }

    /**
     * Associe plusieurs prérequis-mémoire à appliquer simultanément.
     *
     * @param requirements la liste des prérequis à combiner.
     * @return un nouveau prérequis-mémoire.
     */
    public static SpaceRequirement combine(final SpaceRequirement... requirements) {
        final int integer = Arrays.stream(requirements).map(SpaceRequirement::getInteger).reduce(Integer::sum).orElse(0);
        final int bool = Arrays.stream(requirements).map(SpaceRequirement::getBool).reduce(Integer::sum).orElse(0);
        final int square = Arrays.stream(requirements).map(SpaceRequirement::getSquare).reduce(Integer::sum).orElse(0);

        return SpaceRequirement.builder()
              .integer(integer)
              .bool(bool)
              .square(square)
              .build();
    }

    /**
     * Un producteur générant un registre NBC avec les définitions de registres nécessaires pour accommoder les besoins
     * indiqués dans l'objet courant.
     *
     * @return Un segment avec des définitions de registres internes au fonctionnement du programme NBC.
     */
    public Segment getSpaceReservation() {

        final Identifier integerTypeIdentifier = be.unamur.info.b314.compiler.nbc.keywords.Type.SuperType.INT.getType().getIdentifier();
        final Stream<DefinitionVariable> integers = IntStream.range(1, integer + 1)
              .mapToObj(i -> new Identifier("integer" + i))
              .map(i -> DefinitionVariable.builder()
                    .identifier(i)
                    .typeName(integerTypeIdentifier)
                    .build());

        final Identifier booleanTypeIdentifier = be.unamur.info.b314.compiler.nbc.keywords.Type.SuperType.BOOL.getType().getIdentifier();
        final Stream<DefinitionVariable> booleans = IntStream.range(1, bool + 1)
              .mapToObj(i -> new Identifier("boolean" + i))
              .map(i -> DefinitionVariable.builder()
                    .identifier(i)
                    .typeName(booleanTypeIdentifier)
                    .build());

        final Identifier squareTypeIdentifier = be.unamur.info.b314.compiler.nbc.keywords.Type.SuperType.SQUARE.getType().getIdentifier();
        final Stream<DefinitionVariable> squares = IntStream.range(1, square + 1)
              .mapToObj(i -> new Identifier("square" + i))
              .map(i -> DefinitionVariable.builder()
                    .identifier(i)
                    .typeName(squareTypeIdentifier)
                    .build());

        final List<DefinitionVariable> definitions = Stream.concat(integers, Stream.concat(booleans, squares))
              .collect(Collectors.toList());

        return Segment.builder()
              .definitions(definitions)
              .build();
    }

    /**
     * Augmente les besoins en fonction du type du registre demandé.
     *
     * @param type le type de registre pour lequel augmenter le besoin.
     * @return le prérequis en espace courant, augmenté de 1 dans le type demandé.
     */
    public SpaceRequirement add(final Type type) {
        switch (type) {
            case INTEGER:
                return this.addInteger();
            case BOOLEAN:
                return this.addBool();
            case SQUARE:
                return this.addSquare();
            default:
                throw new RuntimeException("Should not happen");
        }
    }

    /**
     * Ajouter un besoin en registre d'entier.
     *
     * @return un prérequis en espace équivalent, augmenté de 1 dans le type entier.
     */
    public SpaceRequirement addInteger() {
        return this.addInteger(1);
    }

    /**
     * Ajouter un besoin en registre booléen.
     *
     * @return un prérequis en espace équivalent, augmenté de 1 dans le type booléen.
     */
    public SpaceRequirement addBool() {
        return this.addBool(1);
    }

    /**
     * Ajouter un besoin en registre de case.
     *
     * @return un prérequis en espace équivalent, augmenté de 1 dans le type case.
     */
    public SpaceRequirement addSquare() {
        return this.addSquare(1);
    }

    /**
     * Ajouter un besoin en registre d'entiers.
     *
     * @param quantity la quantité à ajouter.
     * @return un prérequis en espace équivalent, augmenté de la quantité précisée dans le type entier.
     */
    public SpaceRequirement addInteger(final int quantity) {
        final int integer = this.integer + quantity;
        return this.toBuilder()
              .integer(integer)
              .build();
    }

    /**
     * Ajouter un besoin en registre booléen.
     *
     * @param quantity la quantité à ajouter.
     * @return un prérequis en espace équivalent, augmenté de la quantité précisée dans le type booléen.
     */
    public SpaceRequirement addBool(final int quantity) {
        final int bool = this.bool + quantity;
        return this.toBuilder()
              .bool(bool)
              .build();
    }

    /**
     * Ajouter un besoin en registre de case.
     *
     * @param quantity la quantité à ajouter.
     * @return un prérequis en espace équivalent, augmenté de la quantité précisée dans le type case.
     */
    public SpaceRequirement addSquare(final int quantity) {
        final int square = this.square + quantity;
        return this.toBuilder()
              .square(square)
              .build();
    }

    /**
     * Indique le nombre total de registres nécessaire à l'exécution du programme.
     *
     * @return la somme de la quantité de chaque type de regisre.
     */
    public int size() {
        return integer + bool + square;
    }
}
