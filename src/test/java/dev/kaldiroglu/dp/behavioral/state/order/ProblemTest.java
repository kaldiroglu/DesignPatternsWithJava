package dev.kaldiroglu.dp.behavioral.state.order;

import dev.kaldiroglu.dp.behavioral.state.order.problem.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.state.Printed.codeOf;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The three designs of Part 1: flags, a switch, and enum constants. Every figure the Part 1
 * slides quote about them is asserted here.
 */
class ProblemTest {

    private static final String SOURCE =
            "src/main/java/dev/kaldiroglu/dp/behavioral/state/order/problem/";

    @Test
    @DisplayName("stage one keeps four boolean flags, which make sixteen combinations for five statuses")
    void fourFlagsSixteenCombinations() {
        List<Field> flags = Arrays.stream(FlagOrder.class.getDeclaredFields())
                .filter(f -> f.getType() == boolean.class)
                .toList();

        assertEquals(4, flags.size());
        assertEquals(16, 1 << flags.size());
        assertEquals(5, Status.values().length, "only five of the combinations are real statuses");
    }

    @Test
    @DisplayName("stage one works: an order is paid, shipped and delivered")
    void flagsWork() {
        FlagOrder order = new FlagOrder();
        order.pay();
        order.ship("TR-1");
        order.deliver();

        assertEquals(List.of("paid", "shipped TR-1", "delivered"), order.events());
        assertThrows(IllegalStateException.class, order::cancel);
    }

    @Test
    @DisplayName("stage one refunds a paid order that is cancelled")
    void flagsRefund() {
        FlagOrder order = new FlagOrder();
        order.pay();
        order.cancel();

        assertEquals(List.of("paid", "cancelled, refund issued"), order.events());
        assertThrows(IllegalStateException.class, () -> order.ship("TR-1"));
    }

    @Test
    @DisplayName("stage two has exactly one status, and refuses what that status does not allow")
    void switchingOrder() {
        SwitchingOrder order = new SwitchingOrder();
        assertEquals(Status.PLACED, order.status());

        order.pay();
        order.ship("TR-1");
        assertEquals(Status.SHIPPED, order.status());

        IllegalStateException refused = assertThrows(IllegalStateException.class, order::cancel);
        assertEquals("cannot cancel a shipped order", refused.getMessage());

        order.deliver();
        assertEquals(Status.DELIVERED, order.status());
        assertEquals(List.of("paid", "shipped TR-1", "delivered"), order.events());
    }

    @Test
    @DisplayName("stage two's switches have no default, so a missing status does not compile")
    void switchesHaveNoDefault() {
        String code = codeOf(SOURCE + "SwitchingOrder.java");

        assertEquals(4, code.split("switch \\(status\\)", -1).length - 1,
                "pay, ship, deliver and cancel each switch on the status");
        assertFalse(code.contains("default"));
    }

    @Test
    @DisplayName("stage three works for the first shipment: three failures send the parcel back")
    void enumFirstShipment() {
        EnumOrder order = new EnumOrder();
        order.pay();
        order.ship("TR-1");
        order.failDelivery();
        order.failDelivery();
        assertEquals(OrderStatus.SHIPPED, order.status());

        order.failDelivery();

        assertEquals(OrderStatus.PAID, order.status());
        assertEquals("back to the warehouse", order.events().getLast());
    }

    @Test
    @DisplayName("stage three counts the second shipment's failures as 4, 5 and 6, and the parcel never goes back")
    void enumSecondShipmentHasNoLimit() {
        EnumOrder order = new EnumOrder();
        order.pay();
        order.ship("TR-1");
        order.failDelivery();
        order.failDelivery();
        order.failDelivery();
        order.ship("TR-2");
        order.failDelivery();
        order.failDelivery();
        order.failDelivery();

        List<String> secondShipment = order.events()
                .subList(order.events().indexOf("shipped TR-2") + 1, order.events().size());
        assertEquals(List.of("delivery attempt 4 failed", "delivery attempt 5 failed",
                "delivery attempt 6 failed"), secondShipment);
        assertEquals(OrderStatus.SHIPPED, order.status(), "still shipped: no limit");

        // And it goes on: the check is == 3, so no later failure reaches it either.
        order.failDelivery();
        assertEquals(OrderStatus.SHIPPED, order.status());
    }

    @Test
    @DisplayName("stage three keeps the shipment's data in the order, because a constant is shared")
    void theDataLivesInTheOrder() {
        List<String> orderFields = Arrays.stream(EnumOrder.class.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .map(Field::getName)
                .toList();

        assertTrue(orderFields.containsAll(List.of("trackingNumber", "failedAttempts")));
        assertEquals(5, OrderStatus.values().length);
        assertTrue(Arrays.stream(OrderStatus.class.getDeclaredFields())
                .allMatch(f -> Modifier.isStatic(f.getModifiers())), "the enum has no instance fields of its own");
    }
}
