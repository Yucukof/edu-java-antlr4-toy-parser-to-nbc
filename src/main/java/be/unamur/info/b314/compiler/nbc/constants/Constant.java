package be.unamur.info.b314.compiler.nbc.constants;

import be.unamur.info.b314.compiler.nbc.Argument;

/**
 * Une constante est un élément dont la représentation textuelle correspond strictement à sa valeur, à l'inverse d'un
 * variable dont la valeur se définit en fonction du contexte.
 *
 * @author Hadrien BAILLY
 */
public abstract class Constant implements Argument {

    /**
     * Une constant peut parfois s'exprimer sous forme de chaîne de caractères.
     *
     * @return une chaîne de caractère.
     */
    public abstract String getString();


    /**
     * Une constant peut parfois s'exprimer sous forme de nombre entiers.
     *
     * @return un nombre entier.
     */
    public abstract Integer getInteger();
}
