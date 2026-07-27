package dev.kaldiroglu.dp.structural.adapter.electricity.classAdapter;

/**
 * <b>Client</b>: a Turkish appliance that works only through {@link TurkishPowerSource} and never
 * learns that its power really comes from a {@link USPowerSource} behind the adapter.
 */
public class TurkishHomeAppliance {

    private final String name;
    private final TurkishPowerSource power;

    public TurkishHomeAppliance(String name, TurkishPowerSource power) {
        this.name = name;
        this.power = power;
    }

    public void start() {
        System.out.println(name + " starting");
        power.turnOn();
    }

    public void stop() {
        System.out.println(name + " stopping");
        power.turnOff();
    }
}
