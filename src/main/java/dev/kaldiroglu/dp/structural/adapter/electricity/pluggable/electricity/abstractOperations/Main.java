package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.abstractOperations;

public class Main {
    public static void main(String[] args) {
        HomeAppliance turkishHomeAppliance = new TurkishHomeAppliance("Iron");

        USPowerSource uSPowerSource = new USPowerSource();
        PowerAdapter adapter1 = new USTurkishPowerAdapter(uSPowerSource);
        turkishHomeAppliance.setPowerSource(adapter1);
        runAppliance(turkishHomeAppliance);

        UKPowerSource ukPowerSource = new UKPowerSource();
        PowerAdapter adapter2 = new UKTurkishPowerAdapter(ukPowerSource);
        turkishHomeAppliance.setPowerSource(adapter2);
        runAppliance(turkishHomeAppliance);
    }

    static void runAppliance(HomeAppliance turkishHomeAppliance ) {
        System.out.println("\n*** Starting ***");
        turkishHomeAppliance.start();
        System.out.println("\n*** Stopping ***");
        turkishHomeAppliance.stop();
    }
}
