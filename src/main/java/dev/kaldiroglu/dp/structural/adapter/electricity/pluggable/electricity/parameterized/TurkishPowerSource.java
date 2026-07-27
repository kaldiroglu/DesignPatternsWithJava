package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.parameterized;

/**
 * <b>Target</b> interface for the electricity domain (matches the course slides): the appliance
 * knows only how to {@code turnOn}/{@code turnOff} a Turkish power source.
 */
public interface TurkishPowerSource {

    void turnOn();

    void turnOff();
}
