package dev.kaldiroglu.dp.structural.adapter.hw.telemetry;

/** A second Adaptee, unrelated to the first: a laboratory instrument reporting kelvin. */
public class KelvinThermometer {

    private final String serial;
    private double kelvin;

    public KelvinThermometer(String serial, double kelvin) {
        this.serial = serial;
        this.kelvin = kelvin;
    }

    public String serial() {
        return serial;
    }

    public double kelvin() {
        return kelvin;
    }

    public void setKelvin(double kelvin) {
        this.kelvin = kelvin;
    }
}
