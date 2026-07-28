package dev.kaldiroglu.dp.structural.adapter.hw.telemetry;

/** The Target: what the dashboard reads. Celsius, and a name to label the reading with. */
public interface CelsiusSensor {

    String name();

    double readCelsius();
}
