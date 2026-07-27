package dev.kaldiroglu.dp.structural.adapter.electricity.powerAdapter1;

import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.Appliance;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.TurkishHomeAppliance;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.us.USPowerProvider;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.us.USPowerSource;

public class Test {

	public static void main(String[] args) {
		USPowerSource usPowerSource = new USPowerProvider();
		USTurkishPowerAdapter uSTurkishPowerAdapter = new USTurkishPowerAdapter(usPowerSource);
		
		System.out.println();
		
		Appliance shaver = new TurkishHomeAppliance("Shaver");
		shaver.setPowerSource(uSTurkishPowerAdapter);
		shaver.start();
		shaver.stop();
		
		System.out.println();
//		
//		Appliance turkishBroom = new TurkishHomeAppliance("Broom");
//		turkishBroom.setPowerSource(uSTurkishPowerAdapter);
//		turkishBroom.start();
//		turkishBroom.stop();
	}
}
