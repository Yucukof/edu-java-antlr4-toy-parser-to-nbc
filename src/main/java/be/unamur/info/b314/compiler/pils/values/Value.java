package be.unamur.info.b314.compiler.pils.values;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Function;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.keywords.Square;

/**
 * Une valeur est une entité à laquelle est associé un type déclaré et une valeur effective.
 *
 * @author Hadrien BAILLY
 */
public interface Value {

    /**
     * Retourne l'entité de valeur sous forme booléenne si applicable.
     *
     * @return un booléen.
     * @throws InvalidDataTypeException si la valeur n'est pas de type booléen.
     * @see Square
     */
    default Boolean getBoolValue() {
        throw new InvalidDataTypeException(String.format("Cannot get [%s] value from [%s]", Type.BOOLEAN, this.getType()));
    }

    /**
     * Retourne le type de la valeur.
     *
     * @see Type
     */
    Type getType();

    /**
     * Retourne l'entité de valeur sous forme d'entier si applicable.
     *
     * @return un entier.
     * @throws InvalidDataTypeException si la valeur n'est pas de type entier.
     */
    default Integer getIntValue() {
        throw new InvalidDataTypeException(String.format("Cannot get [%s] value from [%s]", Type.INTEGER, this.getType()));
    }

    /**
     * Retourne l'entité de valeur sous forme de case si applicable.
     *
     * @return une case.
     * @throws InvalidDataTypeException si la valeur n'est pas de type case.
     * @see Square
     */
    default Square getSquareValue() {
        throw new InvalidDataTypeException(String.format("Cannot get [%s] value from [%s]", Type.SQUARE, this.getType()));
    }

    /**
     * Indique si la valeur peut être exprimée sous forme booléenne.
     *
     * @return vrai si la valeur est un booléen, faux sinon.
     */
    default boolean isBoolean() {
        return false;
    }

    /**
     * Indique si la valeur peut être exprimée sous forme d'entier.
     *
     * @return vrai si la valeur est un entier, faux sinon.
     */
    default boolean isInteger() {
        return false;
    }

    /**
     * Indique si la valeur peut être exprimée sous forme de case.
     *
     * @return vrai si la valeur est une case, faux sinon.
     */
    default boolean isSquare() {
        return false;
    }

    /**
     * Indique si la valeur est de type vide.
     * <br>
     * Applicable uniquement dans le cas d'une fonction.
     *
     * @return vrai si la valeur est vide, faux sinon.
     * @see Function
     */
    default boolean isVoid() {
        return false;
    }

    /**
     * Une valeur est un tableau si elle peut contenir plusieurs valeurs possibles identifiées par un, ou plusieurs,
     * indice(s).
     *
     * @return vrai si la variable est un tableau, faux sinon.
     */
    default boolean isArray() {
        return false;
    }

    /**
     * Vérifie si la valeur courante est valide d'un point de vue sémantique.
     *
     * @return vrai si la valeur est correcte, faux sinon.
     */
    boolean isValid();

    /**
     * Une valeur est primitive si elle ne s'obtient pas par calcul (variable) mais s'auto-suffit à elle-même.
     * Elle est ainsi constante à tout moment dans l'exécution du programme. Il s'agira de toute valeur primitive ou
     * agrégat de valeurs primitives dont on peut calculer la valeur de manière absolue.
     *
     * @return vrai si la valeur est un objet primitif, faux sinon.
     */
    default boolean isPrimitive() {
        return false;
    }

    /**
     * Vérifie si la valeur référencée peut être différente selon le contexte.
     * <br>
     * Attention, ne pas confondre avec {@link #isPrimitive()} : une variable peut ainsi être constante, sans être un
     * élément primitif.
     *
     * @return vrai si la valeur référencée est toujours la même, faux sinon.
     */
    default boolean isConstant() {
        return true;
    }

    /**
     * Indique l'espace-mémoire nécessaire à la réalisation du calcul de la valeur
     *
     * @return un compte des différents registres nécessaires simultanément.
     * @see Expression#getSpaceRequirement()
     */
    SpaceRequirement getSpaceRequirement();
}
