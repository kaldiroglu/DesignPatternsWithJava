package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.parameterized;

public class Main {
    public static void main(String[] args) {
        HomeAppliance shaver = new TurkishHomeAppliance("Shaver");

        USPowerSource uSPowerSource = new USPowerSource();
        PluggablePowerAdapter adapter1 = new PluggablePowerAdapter(uSPowerSource::pushSwitch, uSPowerSource::pushSwitch);
        shaver.setPowerSource(adapter1);
        runAppliance(shaver);

        KenyaPowerSource kenyaPowerSource = new KenyaPowerSource();
        PluggablePowerAdapter adapter2 = new PluggablePowerAdapter(kenyaPowerSource::hakunaMatata, kenyaPowerSource::hakunaMatata);
        shaver.setPowerSource(adapter2);
        runAppliance(shaver);
    }

    static void runAppliance(HomeAppliance turkishHomeAppliance ) {
        System.out.println("\n*** Starting ***");
        turkishHomeAppliance.start();
        System.out.println("\n*** Stopping ***");
        turkishHomeAppliance.stop();
    }
}
