package dev.kaldiroglu.dp.behavioral.state.order.problem;

/**
 * Stage three: enum constants. After three failed deliveries the parcel goes back to the
 * warehouse. Then it is shipped again and fails five times. The count was not reset, so
 * the failures are attempts 4 to 8, and the parcel never goes back.
 */
public final class StageThreeMain {
    public static void main(String[] args) {
        EnumOrder order = SampleOrder.backFromTheFirstShipment();
        System.out.println("Stage three, first shipment:  " + order.events() + " -> " + order.status());

        int start = order.events().size();
        order.ship("TR-2");
        for (int i = 0; i < 5; i++) {
            order.failDelivery();
        }
        System.out.println("Stage three, second shipment: "
                + order.events().subList(start, order.events().size()) + " -> " + order.status());
        System.out.println("The count was not reset, so it never equals 3 again: the courier has no limit.");
    }
}
