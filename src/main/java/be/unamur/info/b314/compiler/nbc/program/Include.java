package be.unamur.info.b314.compiler.nbc.program;

import be.unamur.info.b314.compiler.nbc.keywords.Language;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * The #include command works as in Standard C, with the caveat that the filename must
 * be enclosed in double quotes.
 *
 * @author Hadrien BAILLY
 */
@Data
@Builder
public class Include {

    /**
     * Le nom du fichier à importer.
     */
    private final String filename;

    /**
     * Vérifie si l'instruction d'import est valide.
     *
     * @return vrai si le nom de fichier est non-vide.
     */
    public boolean isValid() {
        return StringUtils.isNotEmpty(filename);
    }

    @Override
    public String toString() {
        return "#" + Language.INCLUDE.getToken() + " \"" + filename + "\"";
    }
}
