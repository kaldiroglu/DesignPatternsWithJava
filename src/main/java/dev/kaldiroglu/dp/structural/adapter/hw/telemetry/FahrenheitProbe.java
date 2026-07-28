package dev.kaldiroglu.dp.structural.adapter.hw.telemetry;

/** An Adaptee: an American probe. Fahrenheit, and a different method name. */
public class FahrenheitProbe {

    private final String label;
    private double fahrenheit;

    public FahrenheitProbe(String label, double fahrenheit) {
        this.label = label;
        this.fahrenheit = fahrenheit;
    }

    public String getLabel() {
        return label;
    }

    public double currentF() {
        return fahrenheit;
    }

    public void set(double fahrenheit) {
        this.fahrenheit = fahrenheit;
    }
}
