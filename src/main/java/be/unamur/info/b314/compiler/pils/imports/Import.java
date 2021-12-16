package be.unamur.info.b314.compiler.pils.imports;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Un import est une instruction permettant de lire et d'interpréter un fichier .WLD pour en récupérer une arène.
 *
 * @author Hadrien BAILLY
 */
@Data
@Builder(toBuilder = true)
public class Import {

    /**
     * Le nom du fichier à importer.
     */
    private final String filename;

    @Override
    public String toString() {
        return "import " + filename;
    }

    /**
     * Une méthode permettant de vérifier qu'un import est valide d'un point de vue sémantique.
     *
     * @return vrai si l'import est valide, faux sinon.
     */
    public boolean isValid() {
        return StringUtils.isNotEmpty(filename);
    }
}
