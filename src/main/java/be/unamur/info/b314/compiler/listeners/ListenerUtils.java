package be.unamur.info.b314.compiler.listeners;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

/**
 * Un simple utilitaire lié à la lecture et au parsing d'un texte par ANTLR.
 *
 * @author Hadrien BAILLY
 * @see PilsToNbcConverter
 */
public abstract class ListenerUtils {

    /**
     * Une simple fonction permettant d'afficher le texte original d'une règle lue par un contexte du Parseur.
     *
     * @param ctx le contexte à introspecter.
     * @return une chaîne de caractère.
     */
    public static String getOriginalText(final ParserRuleContext ctx) {
        final CharStream input = ctx.start.getInputStream();
        int a = ctx.start.getStartIndex();
        int b = ctx.stop.getStopIndex();
        Interval interval = new Interval(a, b);
        return input.getText(interval);
    }
}
