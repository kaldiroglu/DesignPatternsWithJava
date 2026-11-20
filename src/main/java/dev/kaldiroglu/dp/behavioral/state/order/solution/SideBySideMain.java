package dev.kaldiroglu.dp.behavioral.state.order.solution;

import dev.kaldiroglu.dp.behavioral.state.order.problem.EnumOrder;

import java.util.List;

/**
 * The whole story with both designs: ship, fail three times, ship again, fail three times.
 * It prints what happens to the second shipment, first with stage three's enum, then with
 * State objects.
 */
public final class SideBySideMain {

    public static void main(String[] args) {
        EnumOrder before = new EnumOrder();
        before.pay();
        before.ship("TR-1");
        failThreeTimes(before::failDelivery);
        before.ship("TR-2");
        failThreeTimes(before::failDelivery);
        System.out.println("Enum constants: " + secondShipment(before.events()) + " -> " + before.status());

        Order after = new Order();
        after.pay();
        after.ship("TR-1");
        failThreeTimes(after::failDelivery);
        after.ship("TR-2");
        failThreeTimes(after::failDelivery);
        System.out.println("State objects:  " + secondShipment(after.events()) + " -> " + after.state());
    }

    private static void failThreeTimes(Runnable failDelivery) {
        for (int i = 0; i < 3; i++) {
            failDelivery.run();
        }
    }

    private static List<String> secondShipment(List<String> events) {
        return events.subList(events.indexOf("shipped TR-2"), events.size());
    }
}
