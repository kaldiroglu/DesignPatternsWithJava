package dev.kaldiroglu.dp.structural.adapter.electricity.domain.us;

public class Main {

	public static void main(String[] args) {
		USPowerSource usPowerSource = new USPowerProvider();

		USHomeAppliance usMixer = new USHomeAppliance("Mixer");
		usMixer.setPowerSource(usPowerSource);
		usMixer.start();
		usMixer.stop();
	}
}
