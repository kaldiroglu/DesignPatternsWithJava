package dev.kaldiroglu.dp.structural.adapter.electricity.classAdapter;

/**
 * <b>Target</b> participant for the electricity domain (matches the course slides): the interface
 * a Turkish appliance knows how to drive.
 *
 * <p>This package is the self-contained <b>class-adapter</b> example. For the pluggable
 * (parameterized) technique in the same domain, see
 * {@link dev.kaldiroglu.adapter.gof.pluggable.electricity}.</p>
 */
public interface TurkishPowerSource {

    void turnOn();

    void turnOff();
}
