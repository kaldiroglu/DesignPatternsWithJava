package dev.kaldiroglu.dp.behavioral.state.order.problem;

/**
 * Stage three: enum constants. After three failed deliveries the parcel goes back to the
 * warehouse.
 */
public final class StageThreeMain {

    public static void main(String[] args) {
        EnumOrder order = SampleOrder.backFromTheFirstShipment();
        System.out.println("Stage three, first shipment:  " + order.events() + " -> " + order.status());
    }
}
