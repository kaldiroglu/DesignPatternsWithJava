package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.delegateObject;


public class Main {
    public static void main(String[] args) {
        HomeAppliance turkishHomeAppliance = new TurkishHomeAppliance("Iron");

        USPowerSource uSPowerSource = new USPowerSource();
        USPowerDelivery uSPowerDelivery = new USPowerDelivery(uSPowerSource);
        DelegatingPowerAdapter adapter1 = new DelegatingPowerAdapter(uSPowerDelivery);
        turkishHomeAppliance.setPowerSource(adapter1);
        runAppliance(turkishHomeAppliance);

        UKPowerSource uKPowerSource = new UKPowerSource();
        UKPowerDelivery uKPowerDelivery = new UKPowerDelivery(uKPowerSource);
        DelegatingPowerAdapter adapter2 = new DelegatingPowerAdapter(uKPowerDelivery);
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
