package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.delegateObject;

/** A {@link PowerDelivery} delegate for {@link USPowerSource} (GoF technique (b)). */
public final class USPowerDelivery implements PowerDelivery {

    private final USPowerSource us;

    public USPowerDelivery(USPowerSource us) {
        this.us = us;
    }

    @Override
    public void deliver() {
        us.pushSwitch();
    }

    @Override
    public void cut() {
        us.pushSwitch();
    }
}
