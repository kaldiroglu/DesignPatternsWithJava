package dev.kaldiroglu.dp.structural.adapter.hw.telemetry;

import java.util.Objects;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

/**
 * A <strong>pluggable adapter</strong> — GoF implementation issue 3, technique (c), p. 143.
 * <p>
 * The obvious design is one adapter class per instrument: {@code FahrenheitProbeAdapter},
 * {@code KelvinThermometerAdapter}, and another next quarter. That is the "one adapter per
 * adaptee" consequence, and it is the thing worth avoiding here.
 * <p>
 * Instead the narrow interface — a name and a reading in Celsius — is supplied as two
 * functions, so <em>one</em> class adapts every instrument that exists or ever will. The
 * conversion arithmetic lives at the call site, where the instrument is known, rather than in
 * a class hierarchy that has to grow to hold it.
 * <p>
 * The trade is real and worth arguing about: nothing now names the adaptee, so the compiler
 * cannot check that the right conversion was paired with the right instrument.
 */
public final class PluggableSensorAdapter implements CelsiusSensor {

    private final Supplier<String> nameFn;
    private final DoubleSupplier celsiusFn;

    public PluggableSensorAdapter(Supplier<String> nameFn, DoubleSupplier celsiusFn) {
        this.nameFn = Objects.requireNonNull(nameFn);
        this.celsiusFn = Objects.requireNonNull(celsiusFn);
    }

    /** The conversions, kept here so the arithmetic is written and tested once. */
    public static CelsiusSensor of(FahrenheitProbe probe) {
        return new PluggableSensorAdapter(probe::getLabel,
                () -> (probe.currentF() - 32) * 5 / 9);
    }

    public static CelsiusSensor of(KelvinThermometer thermometer) {
        return new PluggableSensorAdapter(thermometer::serial,
                () -> thermometer.kelvin() - 273.15);
    }

    @Override
    public String name() {
        return nameFn.get();
    }

    @Override
    public double readCelsius() {
        return celsiusFn.getAsDouble();
    }
}
