package dev.kaldiroglu.dp.behavioral.state.order.problem;

/**
 * The problem with stage three: the parcel is shipped again and fails three more times. The
 * count was not reset, so the failures are attempts 4, 5 and 6 and the order stays shipped.
 */
public final class SecondShipmentMain {

    public static void main(String[] args) {
        EnumOrder order = SampleOrder.backFromTheFirstShipment();
        order.ship("TR-2");
        for (int i = 0; i < 3; i++) {
            order.failDelivery();
        }
        System.out.println("Stage three, second shipment: " + order.events().subList(
                order.events().indexOf("shipped TR-2"), order.events().size()) + " -> " + order.status());
        System.out.println("The count was not reset, so it never equals 3 again: the courier has no limit.");
    }
}
