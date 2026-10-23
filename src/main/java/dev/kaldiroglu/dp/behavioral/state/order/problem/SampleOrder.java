package dev.kaldiroglu.dp.behavioral.state.order.problem;

/**
 * The first shipment of stage three: paid, shipped as TR-1, and three failed delivery
 * attempts, so the parcel is back at the warehouse.
 */
final class SampleOrder {

    private SampleOrder() {
    }

    static EnumOrder backFromTheFirstShipment() {
        EnumOrder order = new EnumOrder();
        order.pay();
        order.ship("TR-1");
        for (int i = 0; i < 3; i++) {
            order.failDelivery();
        }
        return order;
    }
}
