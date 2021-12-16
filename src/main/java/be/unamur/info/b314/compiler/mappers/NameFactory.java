package be.unamur.info.b314.compiler.mappers;

import be.unamur.info.b314.compiler.nbc.Callable;
import be.unamur.info.b314.compiler.nbc.program.Label;
import be.unamur.info.b314.compiler.nbc.routines.Routine;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static be.unamur.info.b314.compiler.mappers.NameFactory.Scope.GLOBAL;
import static be.unamur.info.b314.compiler.mappers.NameFactory.Scope.LOCAL;

/**
 * Le gestionnaire de noms est chargé de suivre et de connaître à tout moment la liste des noms déjà utilisés par le
 * programme NBC et ne pouvant plus être utilisés par ailleurs.
 * <br>
 * On distingue ici deux types de portées : les portées locales, internes à une routine et dont les valeurs ne doivent
 * être uniques qu'au sein de la même routine, et les portées globales qui doivent être respectées par l'ensemble des
 * routines du programme indiféremment.
 */
@Data
@NoArgsConstructor
public class NameFactory {

    /**
     * L'espace des noms à portée globale.
     *
     * @see Scope#GLOBAL
     */
    private final Namespace global = new Namespace();
    /**
     * L'espace des noms à portée locale.
     *
     * @see Scope#LOCAL
     */
    private Namespace local = new Namespace();

    /**
     * Retourne le dernier appelable créé pour la catégorie donnée.
     *
     * @param category une catégorie d'élément pouvant générer un appelable.
     * @return un appelable ({@link Label}/{@link Identifier}) pouvant être atteint depuis un autre endroit du programme.
     */
    public Callable getCurrent(final NameCategory category) {
        return category.getScope().equals(GLOBAL)
              ? global.getCurrent(category)
              : local.getCurrent(category);
    }

    /**
     * Crée un nouvel appelable dans la catégorie donnée.
     *
     * @param category la catégorie d'élément dans laquelle générer un appelable.
     * @return un appelable ({@link Label}/{@link Identifier}) pouvant être atteint depuis un autre endroit du programme.
     */
    public Callable createNew(final NameCategory category) {
        return category.getScope().equals(GLOBAL)
              ? global.createNew(category)
              : local.createNew(category);
    }

    /**
     * Réinitialise l'espace de noms locaux ({@link Label Labels}).
     * <br>
     * À appeler lorsque l'on change de {@link Routine}.
     */
    public void resetLocal() {
        local = new Namespace();
    }

    /**
     * Une catégorie nominative définit un type de nom pouvant être créé, sa {@link Scope portée} au sein du programme
     * et le type d'appelable auquel elle est associée.
     */
    public enum NameCategory {
        THREAD(GLOBAL, Identifier::new),
        ROUTINE(GLOBAL, Identifier::new),
        SUBROUTINE(GLOBAL, Identifier::new),
        DEFAULT(GLOBAL, Identifier::new),
        WHEN(LOCAL, Identifier::new),
        IF(LOCAL, Identifier::new),
        EXPR(LOCAL, Identifier::new),
        WHILE(LOCAL, Identifier::new);

        /**
         * La portée d'un appelable indique la "puissance" des sauts pouvant être effectués vers cet appelable.
         */
        private final Scope scope;
        /**
         * Le fournisseur permet de générer un nouvel appelable sur base de la catégorie nominative, sur base d'une
         * chaine de caractères donnée.
         */
        private final Function<String, Callable> supplier;

        NameCategory(final Scope scope, final Function<String, Callable> supplier) {
            this.scope = scope;
            this.supplier = supplier;
        }

        /**
         * @return la portée de la catégorie nominative.
         */
        public Scope getScope() {
            return scope;
        }

        /**
         * @return le fournisseur d'appelable associé à la catégorie nominative.
         */
        public Function<String, Callable> getSupplier() {
            return supplier;
        }

        /**
         * @return le nom général de la catégorie, qui sert de base à la création d'appelables.
         */
        public String getBase() {
            return name();
        }
    }

    /**
     * La portée indique le niveau de validité d'un nom.
     */
    public enum Scope {
        /**
         * Définition absolue.
         * <br>
         * Un élément global est défini pour l'ensemble du programme et ne peut être déclaré qu'une unique fois pour
         * tout le code généré.
         */
        GLOBAL,
        /**
         * Définition relative à une {@link Routine}.
         * <br>
         * Un élément global est défini pour la durée d'une routine et est "éteint" à la terminaison de celle-ci. Il
         * peut être déclaré simultanément par plusieurs routines. Tout appel vers cet élément ne peut se faire que
         * nécessairement au sein de la même routine.
         */
        LOCAL
    }

    /**
     * Un espace nominatif est un espace au sein duquel une série de noms est déclarée et parmi lequel aucun doublon
     * n'est toléré.
     */
    @Data
    public static class Namespace {

        /**
         * Pour suivre l'utilisation des noms, on utilise un compteur par catégorie, que l'on incrémente au fur et à
         * mesure que des noms sont attribués.
         */
        private final Map<NameCategory, AtomicInteger> namesByCategory = new HashMap<>();

        /**
         * Retourne le dernier nom ayant été attribué pour la catégorie donnée.
         *
         * @param category la catégorie pour laquelle obtenir le nom.
         * @return un identifiant unique déjà utilisé, composé du nom de la catégorie et de l'état courant du compteur.
         */
        public Callable getCurrent(final NameCategory category) {
            final String base = category.getBase();
            final int index = namesByCategory.get(category).get();
            return category.getSupplier().apply(base + "_" + index);
        }

        /**
         * Attribue un nouveau nom pour la catégorie donnée.
         *
         * @param category la catégorie pour laquelle obtenir le nom.
         * @return un identifiant unique encore non utilisé, composé du nom de la catégorie et de l'état courant du
         *       compteur incrémenté de 1.
         */
        public Callable createNew(final NameCategory category) {
            namesByCategory.putIfAbsent(category, new AtomicInteger(0));
            final String base = category.getBase();
            final int index = namesByCategory.get(category).incrementAndGet();
            return category.getSupplier().apply(base + "_" + index);
        }
    }
}
