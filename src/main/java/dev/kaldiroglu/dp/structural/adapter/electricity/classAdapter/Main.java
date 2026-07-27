package dev.kaldiroglu.dp.structural.adapter.electricity.classAdapter;

/**
 * Runnable demo for the electricity <b>class adapter</b>. The appliance drives a US source through
 * the Turkish interface without ever knowing the adaptee's type.
 */
public class Main {

    public static void main(String[] args) {
        // The class adapter presents a US source as a Turkish one.
        TurkishPowerSource adapter = new USTurkishPowerAdapter();

        TurkishHomeAppliance mixer = new TurkishHomeAppliance("Mixer", adapter);
        mixer.start();
        mixer.stop();

        System.out.println();

        // Because the class adapter IS-A USPowerSource, it can also be used as one directly.
        USTurkishPowerAdapter asUsSource = new USTurkishPowerAdapter();
        System.out.println("Used as a raw US source; isLive=" + asUsSource.isLive());
        asUsSource.pushSwitch();
        System.out.println("after pushSwitch(); isLive=" + asUsSource.isLive());
    }
}
