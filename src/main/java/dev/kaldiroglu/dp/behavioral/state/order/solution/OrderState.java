package dev.kaldiroglu.dp.behavioral.state.order.solution;

/**
 * The <b>State</b>: what an order may do in one status.
 * <p>
 * Each operation returns the order's next state, so the states decide the transitions. By
 * default every operation is refused; a state overrides only the operations it allows. GoF
 * implementation issue 1 (who defines the state transitions?): here, the states.
 * <p>
 * The interface is {@code sealed}: these five states are all there are, and the compiler
 * knows it.
 */
public sealed interface OrderState permits Placed, Paid, Shipped, Delivered, Cancelled {

    String name();

    default OrderState pay(Order order) {
        throw reject("pay");
    }

    default OrderState ship(Order order, String trackingNumber) {
        throw reject("ship");
    }

    default OrderState failDelivery(Order order) {
        throw reject("record a failed delivery for");
    }

    default OrderState deliver(Order order) {
        throw reject("deliver");
    }

    default OrderState cancel(Order order) {
        throw reject("cancel");
    }

    private IllegalStateException reject(String action) {
        return new IllegalStateException("cannot " + action + " a " + name() + " order");
    }
}
