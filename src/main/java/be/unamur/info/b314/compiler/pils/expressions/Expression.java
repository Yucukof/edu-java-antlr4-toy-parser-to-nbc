package be.unamur.info.b314.compiler.pils.expressions;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.pils.keywords.Square;
import be.unamur.info.b314.compiler.pils.values.Value;

/**
 * Une expression représente une valeur simple obtenue par calcul, dont on peut déduire un type final effectif.
 * <br>Une expression est composée d'opérandes, qui sont soit des termes uniques soit des sous-expressions composées
 * d'autres termes.
 *
 * @author Hadrien BAILLY
 */
public interface Expression {

    /**
     * Retourne le type effectif de l'expression, sans égard au type individuel de ses opérandes.
     *
     * @return un type.
     */
    Type getType();

    /**
     * Retourne la valeur effective de l'expression, obtenue au terme des opérations contenues dans l'expression.
     *
     * @return une valeur simple.
     */
    Value getValue();

    /**
     * Indique si l'expression est une feuille (valeur simple).
     *
     * @return vrai si l'expression est une instance de {@link ExpressionLeaf}, faux sinon.
     */
    boolean isLeaf();

    /**
     * Indique si l'expression est un nœud composé de deux feuilles.
     *
     * @return vrai si l'expression est une instance de {@link ExpressionNode} et
     *       si ses deux branches sont des feuilles, faux sinon.
     */
    boolean isTerminal();

    /**
     * Vérifie si l'expression courante est valide.
     *
     * @return vrai si l'expression est correcte du point de vue sémantique, faux sinon.
     * @see #validate()
     */
    boolean isValid();

    /**
     * Vérifie si l'expression courante produit un résultat booléen.
     *
     * @return vrai si le type de l'opérateur avec la plus haute précédence est de type booléen, faux sinon.
     */
    boolean isBoolean();

    /**
     * Vérifie si l'expression courante produit un résultat entier.
     *
     * @return vrai si le type de l'opérateur avec la plus haute précédence est de type entier, faux sinon.
     */
    boolean isInteger();

    /**
     * Vérifie si l'expression courante produit un résultat de type case.
     *
     * @return vrai si le type de l'opérateur avec la plus haute précédence est de type case, faux sinon.
     */
    boolean isSquare();

    /**
     * Vérifie si l'expression est correct du point de vue des types.
     *
     * @throws InvalidDataTypeException si l'un des membres de l'expression n'est pas compatible avec l'opérateur ou
     *                                  avec un autre opérande.
     * @throws AssertionError           si l'un des membres de l'expression est nul.
     */
    Expression validate();

    /**
     * Ajoute une nouvelle opération à l'expression courante, en fonction du niveau de précédence de l'opérateur.
     *
     * @param expression l'expression droite de l'opération à ajouter
     * @param operation  l'opérateur de l'opération
     * @return Une nouvelle expression respectant l'ordre de précédence.
     */
    ExpressionNode append(final Expression expression, final Operator operation);

    boolean isVoid();

    /**
     * Vérifie si l'expression n'est constituée que d'éléments primitifs.
     *
     * @return vrai si l'expression ne contient que des valeurs primitives, faux sinon.
     * @see Value#isPrimitive()
     */
    boolean isPrimitive();

    /**
     * Vérifie si la portée de l'expression est constante
     *
     * @return vrai si la valeur de la variable doit être calculée au runtime.
     * @see Value#isConstant()
     */
    boolean isConstant();

    /**
     * Indique l'espace-mémoire nécessaire à la réalisation du calcul de l'expression
     *
     * @return un compte des différents registres nécessaires simultanément.
     * @see Value#getSpaceRequirement()
     */
    SpaceRequirement getSpaceRequirement();

    /**
     * Indique si l'expression courante nécessite un précalcul.
     *
     * @return faux si l'expression peut être convertie directement en NBC, vrai sinon.
     */
    boolean needsPreprocessing();

    /**
     * Calcule la valeur effective de l'expression, sous forme entière.
     *
     * @return un résultat entier.
     * @throws InvalidDataTypeException si l'expression n'est pas de type entier.
     */
    Integer getIntValue();

    /**
     * Calcule la valeur effective de l'expression, sous forme booléenne.
     *
     * @return un résultat booléen.
     * @throws InvalidDataTypeException si l'expression n'est pas de type booléen.
     */
    Boolean getBoolValue();

    /**
     * Calcule la valeur effective de l'expression, sous forme de case.
     *
     * @return un résultat de case.
     * @throws InvalidDataTypeException si l'expression n'est pas de type case.
     */
    Square getSquareValue();
}
