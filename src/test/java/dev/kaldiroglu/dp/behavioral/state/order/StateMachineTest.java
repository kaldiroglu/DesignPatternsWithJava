package dev.kaldiroglu.dp.behavioral.state.order;

import dev.kaldiroglu.dp.behavioral.state.Printed;
import dev.kaldiroglu.dp.behavioral.state.order.problem.Status;
import dev.kaldiroglu.dp.behavioral.state.order.problem.SwitchingOrder;
import dev.kaldiroglu.dp.behavioral.state.order.solution.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static dev.kaldiroglu.dp.behavioral.state.Printed.codeOf;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The order as a finite state machine. Every claim the state-machine slides make about the
 * order is asserted here: the table, the start and end states, the guard, and where each
 * design keeps the table.
 */
class StateMachineTest {

    private static final String PROBLEM =
            "src/main/java/dev/kaldiroglu/dp/behavioral/state/order/problem/";

    @Test
    @DisplayName("TransitionTableMain prints the order's table: five states, five events")
    void theTable() {
        assertEquals(List.of(
                "           pay   ship     failDelivery                deliver    cancel",
                "placed     paid  -        -                           -          cancelled",
                "paid       -     shipped  -                           -          cancelled",
                "shipped    -     -        shipped; paid on attempt 3  delivered  -",
                "delivered  -     -        -                           -          -",
                "cancelled  -     -        -                           -          -"),
                Printed.by(() -> TransitionTableMain.main(new String[0])));
    }

    @Test
    @DisplayName("six of the twenty-five cells have a next state; the rest are refusals")
    void mostOfTheTableIsRefusals() {
        List<String> rows = Printed.by(() -> TransitionTableMain.main(new String[0])).subList(1, 6);
        List<String> cells = rows.stream()
                .flatMap(row -> Arrays.stream(row.split("\\s{2,}")).skip(1))
                .toList();
        assertEquals(25, cells.size());
        assertEquals(6, cells.stream().filter(cell -> !cell.equals("-")).count());
    }

    @Test
    @DisplayName("an order starts placed, and no event leaves delivered or cancelled")
    void oneStartTwoEnds() {
        assertEquals(new Placed(), new Order().state());
        assertEquals(5, OrderState.class.getPermittedSubclasses().length);

        for (Order end : List.of(delivered(), cancelled())) {
            OrderState before = end.state();
            for (Runnable event : eventsOf(end)) {
                assertThrows(IllegalStateException.class, event::run);
            }
            assertEquals(before, end.state());
        }
    }

    @Test
    @DisplayName("the guard: failDelivery keeps the order shipped twice, and the third sends it back to paid")
    void theGuard() {
        Order order = new Order();
        order.pay();
        order.ship("TR-1");
        order.failDelivery();
        assertEquals("shipped", order.state().name());
        order.failDelivery();
        assertEquals("shipped", order.state().name());
        order.failDelivery();
        assertEquals(new Paid(), order.state());
    }

    @Test
    @DisplayName("a shipped order refuses cancel")
    void anEventWithNoTransitionIsRefused() {
        Order order = new Order();
        order.pay();
        order.ship("TR-1");
        IllegalStateException refused = assertThrows(IllegalStateException.class, order::cancel);
        assertEquals("cannot cancel a shipped order", refused.getMessage());
    }

    @Test
    @DisplayName("what an event does depends on the state too: cancel writes a refund only when paid")
    void theActionDependsOnTheStateAndTheEvent() {
        Order placed = new Order();
        placed.cancel();
        Order paid = new Order();
        paid.pay();
        paid.cancel();

        assertEquals("cancelled", placed.events().getLast());
        assertEquals("cancelled, refund issued", paid.events().getLast());
        assertEquals(placed.state(), paid.state());
    }

    @Test
    @DisplayName("stage two's switch codes the same machine for the four events it has")
    void theSwitchCodesTheSameMachine() {
        Map<String, String> expected = Map.ofEntries(
                Map.entry("placed pay", "paid"), Map.entry("placed cancel", "cancelled"),
                Map.entry("paid ship", "shipped"), Map.entry("paid cancel", "cancelled"),
                Map.entry("shipped deliver", "delivered"));

        for (String state : List.of("placed", "paid", "shipped", "delivered", "cancelled")) {
            for (String event : List.of("pay", "ship", "deliver", "cancel")) {
                SwitchingOrder order = switchingOrderIn(state);
                String next;
                try {
                    send(order, event);
                    next = order.status().name().toLowerCase();
                } catch (IllegalStateException refused) {
                    next = "-";
                }
                assertEquals(expected.getOrDefault(state + " " + event, "-"), next, state + " " + event);
            }
        }
    }

    @Test
    @DisplayName("in the switch, each method is one column: one event, with a case for every state")
    void eachMethodIsOneColumn() {
        String code = codeOf(PROBLEM + "SwitchingOrder.java");
        Matcher method = Pattern.compile(
                "public void (\\w+)\\([^)]*\\) \\{\\s*status = switch \\(status\\) \\{(.*?)\\};",
                Pattern.DOTALL).matcher(code);

        int switches = 0;
        while (method.find()) {
            switches++;
            String cases = method.group(2);
            for (Status status : Status.values()) {
                assertTrue(cases.contains(status.name()), method.group(1) + " has no case for " + status);
            }
        }
        assertEquals(4, switches);
    }

    @Test
    @DisplayName("in the State pattern, each class is one row: it overrides exactly the events its state allows")
    void eachClassIsOneRow() {
        Map<Class<?>, Set<String>> rows = Map.of(
                Placed.class, Set.of("pay", "cancel"),
                Paid.class, Set.of("ship", "cancel"),
                Shipped.class, Set.of("failDelivery", "deliver"),
                Delivered.class, Set.of(),
                Cancelled.class, Set.of());

        Set<String> events = Arrays.stream(OrderState.class.getDeclaredMethods())
                .filter(m -> m.isDefault())
                .map(Method::getName)
                .collect(Collectors.toSet());
        assertEquals(Set.of("pay", "ship", "failDelivery", "deliver", "cancel"), events);

        rows.forEach((state, allowed) -> {
            Set<String> overridden = Arrays.stream(state.getDeclaredMethods())
                    .filter(m -> Modifier.isPublic(m.getModifiers()))
                    .map(Method::getName)
                    .filter(events::contains)
                    .collect(Collectors.toSet());
            assertEquals(allowed, overridden, state.getSimpleName());
        });

        assertEquals(List.of("trackingNumber", "failedAttempts"),
                Arrays.stream(Shipped.class.getRecordComponents()).map(RecordComponent::getName).toList());
    }

    private static Order delivered() {
        Order order = new Order();
        order.pay();
        order.ship("TR-1");
        order.deliver();
        return order;
    }

    private static Order cancelled() {
        Order order = new Order();
        order.cancel();
        return order;
    }

    private static List<Runnable> eventsOf(Order order) {
        return List.of(order::pay, () -> order.ship("TR-2"), order::failDelivery,
                order::deliver, order::cancel);
    }

    private static SwitchingOrder switchingOrderIn(String state) {
        SwitchingOrder order = new SwitchingOrder();
        switch (state) {
            case "placed" -> { }
            case "paid" -> order.pay();
            case "shipped" -> { order.pay(); order.ship("TR-1"); }
            case "delivered" -> { order.pay(); order.ship("TR-1"); order.deliver(); }
            case "cancelled" -> order.cancel();
            default -> throw new IllegalArgumentException(state);
        }
        return order;
    }

    private static void send(SwitchingOrder order, String event) {
        switch (event) {
            case "pay" -> order.pay();
            case "ship" -> order.ship("TR-1");
            case "deliver" -> order.deliver();
            case "cancel" -> order.cancel();
            default -> throw new IllegalArgumentException(event);
        }
    }
}
