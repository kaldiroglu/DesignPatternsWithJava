package dev.kaldiroglu.dp.structural.adapter.hw.telemetry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** One adapter class, any number of instruments. */
class PluggableSensorAdapterTest {

    @Test
    @DisplayName("Fahrenheit becomes Celsius")
    void fahrenheit() {
        CelsiusSensor sensor = PluggableSensorAdapter.of(new FahrenheitProbe("outside", 212));

        assertEquals("outside", sensor.name());
        assertEquals(100.0, sensor.readCelsius(), 1e-9);
    }

    @Test
    @DisplayName("kelvin becomes Celsius")
    void kelvin() {
        CelsiusSensor sensor = PluggableSensorAdapter.of(new KelvinThermometer("K-9", 373.15));

        assertEquals("K-9", sensor.name());
        assertEquals(100.0, sensor.readCelsius(), 1e-9);
    }

    @Test
    @DisplayName("the adapter holds the instrument, so later readings show through")
    void readingsAreLive() {
        FahrenheitProbe probe = new FahrenheitProbe("outside", 212);
        CelsiusSensor sensor = PluggableSensorAdapter.of(probe);

        assertEquals(100.0, sensor.readCelsius(), 1e-9);
        probe.set(32);
        assertEquals(0.0, sensor.readCelsius(), 1e-9);
    }

    @Test
    @DisplayName("two unrelated instruments read uniformly")
    void uniform() {
        List<CelsiusSensor> dashboard = List.of(
                PluggableSensorAdapter.of(new FahrenheitProbe("outside", 32)),
                PluggableSensorAdapter.of(new KelvinThermometer("K-9", 273.15)));

        for (CelsiusSensor sensor : dashboard) {
            assertEquals(0.0, sensor.readCelsius(), 1e-9);
        }
    }

    @Test
    @DisplayName("one adapter class, and it names no instrument")
    void oneClassForAll() {
        // The point of the pluggable form: no FahrenheitProbeAdapter, no
        // KelvinThermometerAdapter, and none needed for the next instrument either.
        for (var field : PluggableSensorAdapter.class.getDeclaredFields()) {
            String type = field.getType().getSimpleName();
            assertTrue(type.equals("Supplier") || type.equals("DoubleSupplier"),
                    "no adaptee type is named in the adapter, but found " + type);
        }
    }
}
