package dev.kaldiroglu.dp.structural.adapter.electricity.powerAdapter2;

import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.Appliance;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.TurkishHomeAppliance;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.us.*;

public class Test {

	public static void main(String[] args) {
		USPowerSource usPowerSource = new USPowerProvider();
		USTurkishPowerAdapter uSTurkishPowerAdapter = new USTurkishPowerAdapter(usPowerSource);
		
		System.out.println();
		
		Appliance turkishMixer = new TurkishHomeAppliance("Mixer");
		turkishMixer.setPowerSource(uSTurkishPowerAdapter);
		turkishMixer.start();
		turkishMixer.stop();
		
		System.out.println();
		
		Appliance turkishBroom = new TurkishHomeAppliance("Broom");
		turkishBroom.setPowerSource(uSTurkishPowerAdapter);
		turkishBroom.start();
		turkishBroom.stop();
	}
}
