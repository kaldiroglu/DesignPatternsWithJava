package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.parameterized;


import java.util.List;

/**
 * Runnable demo of the pluggable adapter: ONE adapter class serves THREE differently-shaped
 * adaptees, with the adaptation injected as lambdas (GoF technique (c)).
 */
public final class PluggableDemo {

    private PluggableDemo() {
    }

    public static void main(String[] args) {

        USPowerSource us = new USPowerSource();
        TurkishPowerSource fromUS = new PluggablePowerAdapter(us::pushSwitch, us::pushSwitch);

        UKPowerSource uk = new UKPowerSource();
        TurkishPowerSource fromUK = new PluggablePowerAdapter(uk::flipToggle, uk::flipToggle);

        KenyaPowerSource kenya = new KenyaPowerSource();
        TurkishPowerSource fromKenya = new PluggablePowerAdapter(kenya::hakunaMatata, kenya::hakunaMatata);

        for (TurkishPowerSource source : List.of(fromUS, fromUK, fromKenya)) {
            source.turnOn();
            source.turnOff();
            System.out.println();
        }
    }
}
