package be.unamur.info.b314.compiler.mappers;

import be.unamur.info.b314.compiler.nbc.symbols.Identifier;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class MemoryManagerTest {

    private MemoryManager manager;
    private Map<Type, AtomicInteger> reservation;
    private Map<Type, Integer> availability;

    @Before
    public void before() throws Exception {
        manager = new MemoryManager();
        reservation = manager.getReservation();
        availability = manager.getAvailability();
    }

    @Test
    public void manager_when_allocate_then_expect_0_availability() {

        assertThat(availability).isNotNull();
        assertThat(availability.size()).isEqualTo(3);
        assertThat(availability.get(Type.INTEGER)).isNotNull().isEqualTo(0);
        assertThat(availability.get(Type.BOOLEAN)).isNotNull().isEqualTo(0);
        assertThat(availability.get(Type.SQUARE)).isNotNull().isEqualTo(0);

        assertThat(reservation).isNotNull();
        assertThat(reservation.size()).isEqualTo(3);
        assertThat(reservation.get(Type.INTEGER)).isNotNull();
        assertThat(reservation.get(Type.INTEGER).get()).isEqualTo(0);
        assertThat(reservation.get(Type.BOOLEAN)).isNotNull();
        assertThat(reservation.get(Type.BOOLEAN).get()).isEqualTo(0);
        assertThat(reservation.get(Type.SQUARE)).isNotNull();
        assertThat(reservation.get(Type.SQUARE).get()).isEqualTo(0);

        assertThat(manager.hasAvailability(Type.INTEGER)).isFalse();
        assertThat(manager.hasAvailability(Type.BOOLEAN)).isFalse();
        assertThat(manager.hasAvailability(Type.SQUARE)).isFalse();

        assertThat(manager.hasAllocation(Type.INTEGER)).isFalse();
        assertThat(manager.hasAllocation(Type.BOOLEAN)).isFalse();
        assertThat(manager.hasAllocation(Type.SQUARE)).isFalse();
    }

    @Test
    public void given_empty_requirement_when_allocate_then_expect_0_availability() {

        manager.allocate(SpaceRequirement.NONE);

        assertThat(availability).isNotNull();
        assertThat(availability.size()).isEqualTo(3);
        assertThat(availability.get(Type.INTEGER)).isNotNull().isEqualTo(0);
        assertThat(availability.get(Type.BOOLEAN)).isNotNull().isEqualTo(0);
        assertThat(availability.get(Type.SQUARE)).isNotNull().isEqualTo(0);

        assertThat(reservation).isNotNull();
        assertThat(reservation.size()).isEqualTo(3);
        assertThat(reservation.get(Type.INTEGER)).isNotNull();
        assertThat(reservation.get(Type.INTEGER).get()).isEqualTo(0);
        assertThat(reservation.get(Type.BOOLEAN)).isNotNull();
        assertThat(reservation.get(Type.BOOLEAN).get()).isEqualTo(0);
        assertThat(reservation.get(Type.SQUARE)).isNotNull();
        assertThat(reservation.get(Type.SQUARE).get()).isEqualTo(0);

        assertThat(manager.hasAvailability(Type.INTEGER)).isFalse();
        assertThat(manager.hasAvailability(Type.BOOLEAN)).isFalse();
        assertThat(manager.hasAvailability(Type.SQUARE)).isFalse();

        assertThat(manager.hasAllocation(Type.INTEGER)).isFalse();
        assertThat(manager.hasAllocation(Type.BOOLEAN)).isFalse();
        assertThat(manager.hasAllocation(Type.SQUARE)).isFalse();
    }

    @Test
    public void given_small_requirement_when_allocate_then_expect_availability() {

        final SpaceRequirement requirement = SpaceRequirement.builder()
              .integer(1)
              .bool(2)
              .square(3)
              .build();

        manager.allocate(requirement);

        assertThat(availability).isNotNull();
        assertThat(availability.size()).isEqualTo(3);
        assertThat(availability.get(Type.INTEGER)).isNotNull().isEqualTo(1);
        assertThat(availability.get(Type.BOOLEAN)).isNotNull().isEqualTo(2);
        assertThat(availability.get(Type.SQUARE)).isNotNull().isEqualTo(3);

        assertThat(reservation).isNotNull();
        assertThat(reservation.size()).isEqualTo(3);
        assertThat(reservation.get(Type.INTEGER)).isNotNull();
        assertThat(reservation.get(Type.INTEGER).get()).isEqualTo(0);
        assertThat(reservation.get(Type.BOOLEAN)).isNotNull();
        assertThat(reservation.get(Type.BOOLEAN).get()).isEqualTo(0);
        assertThat(reservation.get(Type.SQUARE)).isNotNull();
        assertThat(reservation.get(Type.SQUARE).get()).isEqualTo(0);

        assertThat(manager.hasAvailability(Type.INTEGER)).isTrue();
        assertThat(manager.hasAvailability(Type.BOOLEAN)).isTrue();
        assertThat(manager.hasAvailability(Type.SQUARE)).isTrue();

        assertThat(manager.hasAllocation(Type.INTEGER)).isFalse();
        assertThat(manager.hasAllocation(Type.BOOLEAN)).isFalse();
        assertThat(manager.hasAllocation(Type.SQUARE)).isFalse();
    }

    @Test
    public void given_small_requirement_when_reserveAndGet_then_expect_identifier_and_allocation_and_no_availability() {

        final SpaceRequirement requirement = SpaceRequirement.builder()
              .integer(1)
              .bool(2)
              .square(3)
              .build();

        manager.allocate(requirement);

        final Identifier identifier = manager.reserveAndGet(Type.INTEGER);

        assertThat(identifier).isNotNull();
        assertThat(identifier.isValid()).isTrue();
        assertThat(identifier.getName()).isEqualTo("integer1");

        assertThat(availability.size()).isEqualTo(3);
        assertThat(availability.get(Type.INTEGER)).isNotNull().isEqualTo(1);
        assertThat(availability.get(Type.BOOLEAN)).isNotNull().isEqualTo(2);
        assertThat(availability.get(Type.SQUARE)).isNotNull().isEqualTo(3);

        assertThat(reservation.size()).isEqualTo(3);
        assertThat(reservation.get(Type.INTEGER)).isNotNull();
        assertThat(reservation.get(Type.INTEGER).get()).isEqualTo(1);
        assertThat(reservation.get(Type.BOOLEAN)).isNotNull();
        assertThat(reservation.get(Type.BOOLEAN).get()).isEqualTo(0);
        assertThat(reservation.get(Type.SQUARE)).isNotNull();
        assertThat(reservation.get(Type.SQUARE).get()).isEqualTo(0);

        assertThat(manager.hasAvailability(Type.INTEGER)).isFalse();
        assertThat(manager.hasAvailability(Type.BOOLEAN)).isTrue();
        assertThat(manager.hasAvailability(Type.SQUARE)).isTrue();

        assertThat(manager.hasAllocation(Type.INTEGER)).isTrue();
        assertThat(manager.hasAllocation(Type.BOOLEAN)).isFalse();
        assertThat(manager.hasAllocation(Type.SQUARE)).isFalse();

    }

    @Test
    public void given_small_requirement_and_allocation_when_release_then_expect_identifier_and_allocation_and_no_availability() {

        final SpaceRequirement requirement = SpaceRequirement.builder()
              .integer(1)
              .bool(2)
              .square(3)
              .build();

        manager.allocate(requirement);

        manager.reserveAndGet(Type.INTEGER);
        manager.release(Type.INTEGER);

        assertThat(availability.size()).isEqualTo(3);
        assertThat(availability.get(Type.INTEGER)).isNotNull().isEqualTo(1);
        assertThat(availability.get(Type.BOOLEAN)).isNotNull().isEqualTo(2);
        assertThat(availability.get(Type.SQUARE)).isNotNull().isEqualTo(3);

        assertThat(reservation.size()).isEqualTo(3);
        assertThat(reservation.get(Type.INTEGER)).isNotNull();
        assertThat(reservation.get(Type.INTEGER).get()).isEqualTo(0);
        assertThat(reservation.get(Type.BOOLEAN)).isNotNull();
        assertThat(reservation.get(Type.BOOLEAN).get()).isEqualTo(0);
        assertThat(reservation.get(Type.SQUARE)).isNotNull();
        assertThat(reservation.get(Type.SQUARE).get()).isEqualTo(0);

        assertThat(manager.hasAvailability(Type.INTEGER)).isTrue();
        assertThat(manager.hasAvailability(Type.BOOLEAN)).isTrue();
        assertThat(manager.hasAvailability(Type.SQUARE)).isTrue();

        assertThat(manager.hasAllocation(Type.INTEGER)).isFalse();
        assertThat(manager.hasAllocation(Type.BOOLEAN)).isFalse();
        assertThat(manager.hasAllocation(Type.SQUARE)).isFalse();

    }
}