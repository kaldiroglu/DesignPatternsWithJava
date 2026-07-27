package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.parameterized;

/**
 * <b>Pluggable adapter</b>, parameterized form &ndash; GoF technique (c), p. 143.
 *
 * <p>One adapter class adapts <i>any</i> foreign source to {@link TurkishPowerSource}. The narrow
 * interface is just "turn on" / "turn off", supplied as {@link Runnable} blocks. This replaces the
 * family {@code USTurkishPowerAdapter}, {@code UKTurkishPowerAdapter}, &hellip; with a single
 * parameterizable class &ndash; the answer to the deck's "one adapter per interface" consequence.</p>
 */
public final class PluggablePowerAdapter implements TurkishPowerSource {

    private final Runnable onTurnOn;
    private final Runnable onTurnOff;

    public PluggablePowerAdapter(Runnable onTurnOn, Runnable onTurnOff) {
        this.onTurnOn = onTurnOn;
        this.onTurnOff = onTurnOff;
    }

    @Override
    public void turnOn() {
        onTurnOn.run();
    }

    @Override
    public void turnOff() {
        onTurnOff.run();
    }
}
