package dev.kaldiroglu.dp.behavioral.state.order;

import dev.kaldiroglu.dp.behavioral.state.Printed;
import dev.kaldiroglu.dp.behavioral.state.order.solution.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.state.Printed.codeOf;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The order with State objects. Every figure the Part 3 slides quote about the order is
 * asserted here.
 */
class SolutionTest {

    private static final String SOURCE =
            "src/main/java/dev/kaldiroglu/dp/behavioral/state/order/solution/";

    private static Order secondShipmentFailedThreeTimes() {
        Order order = new Order();
        order.pay();
        order.ship("TR-1");
        order.failDelivery();
        order.failDelivery();
        order.failDelivery();
        order.ship("TR-2");
        order.failDelivery();
        order.failDelivery();
        order.failDelivery();
        return order;
    }

    @Test
    @DisplayName("State objects count the second shipment's failures as 1, 2 and 3, and the parcel goes back")
    void theSecondShipmentHasThreeAttempts() {
        Order order = secondShipmentFailedThreeTimes();

        List<String> secondShipment = order.events()
                .subList(order.events().indexOf("shipped TR-2") + 1, order.events().size());
        assertEquals(List.of("delivery attempt 1 failed", "delivery attempt 2 failed",
                "delivery attempt 3 failed", "back to the warehouse"), secondShipment);
        assertEquals(new Paid(), order.state());
    }

    @Test
    @DisplayName("a new shipment is a new Shipped with zero attempts")
    void aNewShipmentStartsAtZero() {
        Order order = new Order();
        order.pay();
        order.ship("TR-1");
        assertEquals(new Shipped("TR-1", 0), order.state());

        order.failDelivery();
        assertEquals(new Shipped("TR-1", 1), order.state());
        assertEquals(3, Shipped.MAX_ATTEMPTS);
    }

    @Test
    @DisplayName("an order that is paid, shipped and delivered goes through the right states")
    void theHappyPath() {
        Order order = new Order();
        assertEquals(new Placed(), order.state());
        order.pay();
        order.ship("TR-1");
        order.deliver();

        assertEquals(new Delivered(), order.state());
        assertEquals(List.of("paid", "shipped TR-1", "delivered"), order.events());
    }

    @Test
    @DisplayName("cancelling a placed order gives no refund, and a paid one gives a refund")
    void cancelling() {
        Order placed = new Order();
        placed.cancel();
        assertEquals(List.of("cancelled"), placed.events());
        assertEquals(new Cancelled(), placed.state());

        Order paid = new Order();
        paid.pay();
        paid.cancel();
        assertEquals(List.of("paid", "cancelled, refund issued"), paid.events());
    }

    @Test
    @DisplayName("every operation is refused until a state allows it, and the state does not change")
    void refusedByDefault() {
        Order order = new Order();
        order.pay();
        order.ship("TR-1");

        IllegalStateException refused = assertThrows(IllegalStateException.class, order::cancel);
        assertEquals("cannot cancel a shipped order", refused.getMessage());
        assertEquals(new Shipped("TR-1", 0), order.state());

        order.deliver();
        for (Runnable request : List.<Runnable>of(order::pay, () -> order.ship("TR-2"),
                order::failDelivery, order::deliver, order::cancel)) {
            assertThrows(IllegalStateException.class, request::run);
        }
    }

    @Test
    @DisplayName("Delivered and Cancelled override nothing at all")
    void theEndStatesOverrideNothing() {
        for (Class<?> end : List.of(Delivered.class, Cancelled.class)) {
            List<String> declared = Arrays.stream(end.getDeclaredMethods())
                    .map(Method::getName)
                    .filter(name -> List.of("pay", "ship", "failDelivery", "deliver", "cancel")
                            .contains(name))
                    .toList();
            assertEquals(List.of(), declared, end.getSimpleName());
        }
    }

    @Test
    @DisplayName("the state is a sealed interface that permits exactly five records")
    void fiveSealedStates() {
        assertTrue(OrderState.class.isSealed());
        List<Class<?>> permitted = List.of(OrderState.class.getPermittedSubclasses());

        assertEquals(List.of(Placed.class, Paid.class, Shipped.class, Delivered.class,
                Cancelled.class), permitted);
        assertTrue(permitted.stream().allMatch(Class::isRecord));
        assertEquals(List.of("trackingNumber", "failedAttempts"),
                Arrays.stream(Shipped.class.getRecordComponents()).map(c -> c.getName()).toList(),
                "only Shipped has data");
        assertTrue(permitted.stream().filter(c -> c != Shipped.class)
                .allMatch(c -> c.getRecordComponents().length == 0));
    }

    @Test
    @DisplayName("the order has no flags, no switch and no field that belongs to one status")
    void theContextOnlyForwards() {
        String code = codeOf(SOURCE + "Order.java");

        assertFalse(code.contains("switch"));
        assertFalse(code.contains("boolean"));
        assertFalse(code.contains("trackingNumber;"));
        assertFalse(code.contains("failedAttempts"));
        assertTrue(code.contains("state = state.ship(this, trackingNumber);"));
    }

    @Test
    @DisplayName("Main prints attempt 4 for the enum and attempt 1 for State objects")
    void mainOutput() {
        List<String> lines = Printed.by(() -> Main.main(new String[0]));

        assertEquals(5, lines.size());
        assertTrue(lines.get(0).startsWith("Enum constants: "));
        assertTrue(lines.get(0).endsWith("shipped TR-2, delivery attempt 4 failed]"));
        assertEquals("  status now: SHIPPED", lines.get(1));
        assertTrue(lines.get(2).endsWith("shipped TR-2, delivery attempt 1 failed]"));
        assertEquals("  state now: Shipped[trackingNumber=TR-2, failedAttempts=1]", lines.get(3));
        assertEquals("Cancel a shipped order: cannot cancel a shipped order", lines.get(4));
    }
}
