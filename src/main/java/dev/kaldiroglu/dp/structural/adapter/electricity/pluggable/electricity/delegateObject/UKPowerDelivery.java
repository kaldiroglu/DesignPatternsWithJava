package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.delegateObject;

/** A {@link PowerDelivery} delegate for {@link UKPowerSource} (GoF technique (b)). */
public final class UKPowerDelivery implements PowerDelivery {

    private final UKPowerSource uk;

    public UKPowerDelivery(UKPowerSource uk) {
        this.uk = uk;
    }

    @Override
    public void deliver() {
        uk.flipToggle();
    }

    @Override
    public void cut() {
        uk.flipToggle();
    }
}
