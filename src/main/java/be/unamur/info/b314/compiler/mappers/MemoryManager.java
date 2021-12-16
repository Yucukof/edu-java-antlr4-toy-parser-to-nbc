package be.unamur.info.b314.compiler.mappers;

import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Le gestionnaire de mémoire (Memory Manager) est l'instrument chargé de contrôler l'attribution des registres de
 * calcul d'expression lors de la conversion du programme PILS vers le langage NBC. Il rend compte du nombre de
 * registres disponibles et de la quantité utilisée à tout moment dans la conversion du programme vers NBC.
 *
 * @author Hadrien BAILLY
 */
@Slf4j
@Data
public class MemoryManager {
    /**
     * The number of registers available for use, by supertype.
     */
    private final Map<Type, Integer> availability = new HashMap<>();
    /**
     * The number of register already in use, by supertype.
     */
    private final Map<Type, AtomicInteger> reservation = new HashMap<>();

    /**
     * Constructeur par défaut, il initie un nouvel espace mémoire sans aucun registre.
     */
    public MemoryManager() {
        Arrays.stream(Type.values())
              .filter(type -> !type.equals(Type.VOID))
              .forEach(type -> availability.put(type, 0));

        Arrays.stream(Type.values())
              .filter(type -> !type.equals(Type.VOID))
              .forEach(type -> reservation.put(type, new AtomicInteger(0)));
    }

    /**
     * Associe de nouveaux registres à la mémoire, sur base des besoins en espace donnés.
     *
     * @param requirement le compte des différents registres supplémentaires nécessaires.
     */
    public void allocate(final SpaceRequirement requirement) {
        log.info("Allocating memory space...");
        final int integer = availability.get(Type.INTEGER) + requirement.getInteger();
        availability.put(Type.INTEGER, integer);
        final int bool = availability.get(Type.BOOLEAN) + requirement.getBool();
        availability.put(Type.BOOLEAN, bool);
        final int square = availability.get(Type.SQUARE) + requirement.getSquare();
        availability.put(Type.SQUARE, square);
        log.info("[{}]", availability);
    }

    /**
     * Alloue de nouveaux registres à la mémoire, sur base des besoins exprimés explicitement dans les paramètres.
     *
     * @param integer le nombre de registres supplémentaires nécessaires, de type {@link
     *                be.unamur.info.b314.compiler.nbc.keywords.Type.SuperType#INT
     *                INT}.
     * @param bool    le nombre de registres supplémentaires nécessaires, de type {@link
     *                be.unamur.info.b314.compiler.nbc.keywords.Type.SuperType#BOOL
     *                BOOL}.
     * @param square  le nombre de registres supplémentaires nécessaires, de type {@link
     *                be.unamur.info.b314.compiler.nbc.keywords.Type.SuperType#SQUARE
     *                SQUARE}.
     */
    public void allocate(final int integer, final int bool, final int square) {
        log.info("Allocating memory space...");
        final int newInteger = availability.get(Type.INTEGER) + integer;
        availability.put(Type.INTEGER, newInteger);
        final int newBool = availability.get(Type.BOOLEAN) + bool;
        availability.put(Type.BOOLEAN, newBool);
        final int newSquare = availability.get(Type.SQUARE) + square;
        availability.put(Type.SQUARE, newSquare);
        log.info("[{}]", availability);
    }

    /**
     * Réserve un nouveau registre du {@link be.unamur.info.b314.compiler.nbc.keywords.Type.SuperType type donné} si
     * disponible et retourne son identifiant unique.
     *
     * @param type le type PILS du registre à réserver.
     * @return un identifiant unique vers le registre réservé.
     * @throws SpaceAllocationExceeded si le stock de registres disponibles du type demandé est épuisé.
     * @see #identify(Type, int)
     */
    public Identifier reserveAndGet(final Type type) {
        final int available = availability.get(type);
        final AtomicInteger allocated = reservation.get(type);
        if (allocated.get() >= available) {
            final String message = String.format("Cannot allocate new register of type [%s] (maximum number of [%s] exceeded)"
                  , type
                  , available);
            throw new SpaceAllocationExceeded(message);
        }
        final Identifier register = identify(type, allocated.incrementAndGet());
        log.trace("Allocated register [{}]", register);
        return register;
    }

    /**
     * Produit un identifiant vers un registre.
     *
     * @param type   le type du registre
     * @param number le numéro du registre parmi son type.
     * @return un identifiant unique.
     */
    private static Identifier identify(final Type type, final int number) {
        return new Identifier(type.toString().toLowerCase() + number);
    }

    /**
     * Libère un registre réservé, qui retourne dans le pool de registres disponibles.
     *
     * @param type le type du registre à libérer.
     */
    public void release(final Type type) {
        final AtomicInteger allocated = reservation.get(type);
        if (allocated.get() > 0) {
            final Identifier register = identify(type, allocated.getAndDecrement());
            log.trace("Released register [{}]", register);
        }
    }

    /**
     * Libère un registre réservé, qui retourne dans le pool de registres disponibles.
     *
     * @param type le type du registre à libérer.
     */
    public Identifier getAndRelease(final Type type) {
        final AtomicInteger allocated = reservation.get(type);
        if (allocated.get() > 0) {
            final Identifier register = identify(type, allocated.getAndDecrement());
            log.trace("Released register [{}]", register);
            return register;
        }
        throw new IllegalStateException(String.format("Cannot get allocated [%s] register", type));
    }

    /**
     * Récupère le nom du dernier registre déclaré dans le type donné.
     *
     * @param type le type du registre à récupérer.
     * @return un identifiant unique vers un registre déjà réservé.
     */
    public Identifier get(final Type type) {
        if (hasAllocation(type)) {
            return identify(type, reservation.get(type).get());
        }
        throw new SpaceAllocationNeeded(String.format("Cannot get allocated register of type [%s]", type));
    }

    /**
     * Vérifie s'il existe des registres déjà réservés en mémoire.
     *
     * @param type le type de registres à sonder.
     * @return vrai s'il y a au moins un registre réservé dans le type donné, faux sinon.
     */
    public boolean hasAllocation(final Type type) {
        return reservation.getOrDefault(type, new AtomicInteger(0)).get() > 0;
    }

    /**
     * Acquiert et libère immédiatement un registre dans le type donné.
     *
     * @param type le type du registre à récupérer.
     * @return un identifiant unique vers un registre libre.
     */
    public Identifier getDisposable(final Type type) {
        if (hasAvailability(type)) {
            final Identifier identifier = identify(type, reservation.get(type).get() + 1);
            log.trace("Assigned disposable register [{}]", identifier);
            return identifier;
        }
        final String message = String.format("Cannot allocate new register of type [%s] (maximum number of [%s] exceeded)"
              , type
              , availability.get(type));
        throw new SpaceAllocationExceeded(message);
    }

    /**
     * Vérifie si le pool de registres du type donné contient encore un registre disponible.
     *
     * @param type le type du pool à sonder.
     * @return vrai si le nombre de registres utilisés est strictement plus petit que le nombre de registres
     *       disponibles, faux sinon.
     */
    public boolean hasAvailability(final Type type) {
        return reservation.getOrDefault(type, new AtomicInteger(0)).get() < availability.getOrDefault(type, 0);
    }

    /**
     * Une exception de dépassement d'espace réservé intervient lorsque l'on essaye d'utiliser plus de registres qu'il y
     * en a de disponibles.
     *
     * @author Hadrien BAILLY
     */
    public static class SpaceAllocationExceeded extends RuntimeException {

        public SpaceAllocationExceeded(final String message) {
            super(message);
        }
    }

    /**
     * Une exception d'allocation nécessaire intervient lorsque l'on essaye d'accéder à un registre réservé qui n'existe
     * pas.
     *
     * @author Hadrien BAILLY
     */
    public static class SpaceAllocationNeeded extends RuntimeException {

        public SpaceAllocationNeeded(final String message) {
            super(message);
        }
    }
}
