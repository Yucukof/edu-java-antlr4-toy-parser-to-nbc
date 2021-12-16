package be.unamur.info.b314.compiler.mappers;

import be.unamur.info.b314.compiler.nbc.constants.ConstantString;
import be.unamur.info.b314.compiler.nbc.declarations.Segment;
import be.unamur.info.b314.compiler.nbc.definitions.DefinitionVariable;
import be.unamur.info.b314.compiler.nbc.keywords.Type;
import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.keywords.Item;
import be.unamur.info.b314.compiler.pils.values.booleans.Location;
import be.unamur.info.b314.compiler.pils.values.integers.Life;
import be.unamur.info.b314.compiler.pils.values.integers.Position;
import be.unamur.info.b314.compiler.pils.values.squares.Nearby;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * L'environnement est l'ensemble des variables nécessaires à l'exécution du programme PILS originel.
 *
 * @author Hadrien BAILLY
 */
public class Environment {

    /**
     * Les variables relatives aux tests de position.
     *
     * @see Location
     */
    public enum LocationVariable implements EnvironmentVariable {
        ENEMY_NORTH,
        ENEMY_SOUTH,
        ENEMY_EAST,
        ENEMY_WEST,
        GRAAL_NORTH,
        GRAAL_SOUTH,
        GRAAL_EAST,
        GRAAL_WEST;

        @Override
        public DefinitionVariable getVariable() {
            return DefinitionVariable.builder()
                  .identifier(getIdentifier())
                  .typeName(getTypeName())
                  .dimensions(Collections.emptyList())
                  .build();
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public Type getType() {
            return Type.SuperType.BOOL.getType();
        }
    }

    /**
     * Les variables relatives aux tests de condition d'environnement.
     *
     * @see Position
     */
    public enum PositionVariable implements EnvironmentVariable {
        LATITUDE,
        LONGITUDE,
        GRID_SIZE;

        @Override
        public DefinitionVariable getVariable() {
            return DefinitionVariable.builder()
                  .identifier(getIdentifier())
                  .typeName(getTypeName())
                  .dimensions(Collections.emptyList())
                  .build();
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public Type getType() {
            return Type.SuperType.INT.getType();
        }
    }

    /**
     * Les variables relatives aux tests de contenu d'inventaire.
     *
     * @see Item
     */
    public enum ItemVariable implements EnvironmentVariable {
        MAP,
        RADIO,
        RADAR,
        AMMO,
        FRUITS,
        SODA;

        @Override
        public DefinitionVariable getVariable() {
            return DefinitionVariable.builder()
                  .identifier(getIdentifier())
                  .typeName(getTypeName())
                  .dimensions(Collections.emptyList())
                  .build();
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public Type getType() {
            return Type.SuperType.INT.getType();
        }
    }

    /**
     * Les variables relatives aux tests de niveau de vie.
     *
     * @see Life
     */
    public enum LifeVariable implements EnvironmentVariable {
        LIFE;

        @Override
        public DefinitionVariable getVariable() {
            return DefinitionVariable.builder()
                  .identifier(getIdentifier())
                  .typeName(getTypeName())
                  .dimensions(Collections.emptyList())
                  .build();
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public Type getType() {
            return Type.SuperType.INT.getType();
        }
    }

    /**
     * La variable relative aux objets avoisinant le joueur.
     *
     * @see Nearby
     */
    public enum NearbyVariable implements EnvironmentVariable {
        NEARBY;

        @Override
        public List<Integer> getDimensions() {
            return Arrays.asList(9, 9);
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public Type getType() {
            return Type.SuperType.SQUARE.getType();
        }

    }

    /**
     * Une variable d'environnement est une variable correspondant à un mot réservé.
     *
     * @see Item
     * @see Position
     * @see Location
     * @see Life
     */
    public interface EnvironmentVariable {

        /**
         * Construit et retourne un segment contenant les définitions des variables d'environnement.
         *
         * @return un segment composé de variables.
         */
        static Segment getSegment() {
            return Segment.builder()
                  .definitions(getAll())
                  .build();
        }

        /**
         * Récupère l'ensemble des variables déclarées par les classes implémentant l'interface {@link
         * EnvironmentVariable}.
         *
         * @return une liste de mots-clé.
         */
        static Set<DefinitionVariable> getAll() {
            Reflections reflections = new Reflections("be.unamur.info.b314.compiler");
            Set<Class<? extends EnvironmentVariable>> classes = reflections.getSubTypesOf(EnvironmentVariable.class);

            return classes.stream()
                  .map(EnvironmentVariable::getAll)
                  .flatMap(Set::stream)
                  .collect(Collectors.toSet());
        }

        /**
         * Récupère la liste des variables définies par l'une des classes implémentant {@link EnvironmentVariable}.
         *
         * @param clazz la classe à parcourir
         * @return une liste de variables
         */
        static Set<DefinitionVariable> getAll(final Class<? extends EnvironmentVariable> clazz) {
            return Arrays.stream(clazz.getEnumConstants())
                  .map(EnvironmentVariable::getVariable)
                  .collect(Collectors.toSet());
        }

        /**
         * Retourne la définition de la variable d'environnement.
         *
         * @return une définition de variable NBC.
         */
        default DefinitionVariable getVariable() {
            return DefinitionVariable.builder()
                  .identifier(getIdentifier())
                  .typeName(getTypeName())
                  .dimensions(getDimensions())
                  .build();
        }

        /**
         * Renvoie l'identifiant unique de la variable d'environnement.
         *
         * @return un identifiant NBC.
         */
        default Identifier getIdentifier() {
            return new Identifier(getName());
        }

        /**
         * Renvoie l'identifiant unique du type afférent à la variable d'environnement.
         *
         * @return un identifiant NBC.
         * @see Type#getIdentifier()
         */
        default Identifier getTypeName() {
            return getType().getIdentifier();
        }

        default List<Integer> getDimensions() {
            return Collections.emptyList();
        }

        /**
         * Renvoie le nom de la variable d'environnement.
         *
         * @return un nom de variable.
         */
        String getName();

        /**
         * Renvoie le type nécessaire au stockage de la variable d'environnement.
         *
         * @return un type NBC.
         */
        Type getType();

        /**
         * Retourne une constante littérale correspondant à la variable d'environnement.
         *
         * @return une constante de type String.
         */
        default ConstantString get() {
            return ConstantString.builder()
                  .value(getName())
                  .build();
        }

    }
}
