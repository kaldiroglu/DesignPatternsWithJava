package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.parameterized;

/**
 * <b>Pluggable adapter</b>, parameterized form &ndash; GoF technique (c), p. 143.
 *
 * <p>One adapter class adapts <i>any</i> foreign source to {@link TurkishPowerSource}. The narrow
 * interface is just "turn on" / "turn off", supplied as {@link Runnable} blocks. This replaces the
 * family {@code USTurkishPowerAdapter}, {@code UKTurkishPowerAdapter}, &hellip; with a single
 * parameterizable class &ndash; the answer to the deck's "one adapter per interface" consequence.</p>
 *
 * <p>The sources here have one toggle, so both blocks may be the same call. The {@code on} field
 * keeps the meaning of the two operations: a second {@code turnOn} does not switch the power off.</p>
 */
public final class PluggablePowerAdapter implements TurkishPowerSource {

    private final Runnable onTurnOn;
    private final Runnable onTurnOff;
    private boolean on;

    public PluggablePowerAdapter(Runnable onTurnOn, Runnable onTurnOff) {
        this.onTurnOn = onTurnOn;
        this.onTurnOff = onTurnOff;
    }

    @Override
    public void turnOn() {
        if (!on) {
            onTurnOn.run();
            on = true;
        }
    }

    @Override
    public void turnOff() {
        if (on) {
            onTurnOff.run();
            on = false;
        }
    }
}
