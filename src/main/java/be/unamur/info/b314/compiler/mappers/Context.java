package be.unamur.info.b314.compiler.mappers;

/**
 * Le contexte de transformation du langage PILS vers le langage NBC.
 *
 * @author Hadrien BAILLY
 */
public class Context {

    /**
     * Ensemble des mappers disponibles, liés au contexte courant.
     */
    public final MapperWarehouse mappers = new MapperWarehouse(this);
    /**
     * Le gestionnaire des noms (Labels et Identifiants) au sein du programme NBC en cours de conversion.
     * <br>
     * Il garantit l'unicité des noms tout au long du programme.
     */
    public final NameFactory names = new NameFactory();
    /**
     * Le gestionnaire des registres de variables utilisés par le programme NBC en cours de conversion.
     */
    public final MemoryManager memory = new MemoryManager();


}
