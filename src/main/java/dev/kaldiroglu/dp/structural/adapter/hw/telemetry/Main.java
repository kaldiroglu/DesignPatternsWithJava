package dev.kaldiroglu.dp.structural.adapter.hw.telemetry;

import java.util.List;

/** Homework 3 — two unrelated instruments, one adapter class. */
public class Main {

    public static void main(String[] args) {
        FahrenheitProbe probe = new FahrenheitProbe("outside", 212);
        KelvinThermometer lab = new KelvinThermometer("K-9", 273.15);

        List<CelsiusSensor> dashboard = List.of(
                PluggableSensorAdapter.of(probe),
                PluggableSensorAdapter.of(lab));

        for (CelsiusSensor sensor : dashboard) {
            System.out.printf("%-8s %6.2f C%n", sensor.name(), sensor.readCelsius());
        }

        // The adapters hold the instruments, so a new reading shows through.
        probe.set(32);
        lab.setKelvin(373.15);
        System.out.println("\nafter the instruments move:");
        for (CelsiusSensor sensor : dashboard) {
            System.out.printf("%-8s %6.2f C%n", sensor.name(), sensor.readCelsius());
        }

        System.out.println("""

                Two instruments with nothing in common — different names, different
                units, no shared supertype — behind one interface and ONE adapter
                class. A third instrument is two lambdas, not a new class.

                The price: nothing names the adaptee any more, so the compiler
                cannot check that the right conversion went with the right probe.""");
    }
}
