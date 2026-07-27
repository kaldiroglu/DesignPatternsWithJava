package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.delegateObject;

/**
 * <b>Pluggable adapter via a delegate object</b> &ndash; GoF technique (b), p. 143. One adapter
 * class serves any adaptee: swap the {@link PowerDelivery} delegate to re-target, no subclassing.
 */
public final class DelegatingPowerAdapter implements TurkishPowerSource {

    private final PowerDelivery delivery;

    public DelegatingPowerAdapter(PowerDelivery delivery) {
        this.delivery = delivery;
    }

    @Override
    public void turnOn() {
       delivery.deliver();
    }

    @Override
    public void turnOff() {
        delivery.cut();
    }
}
