package dev.kaldiroglu.dp.structural.adapter.electricity.problem2;

//import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.*;
//import dev.kaldiroglu.dp.structural.adapter.electricity.domain.us.*;

import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.TurkishPowerProvider;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.TurkishPowerSource;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.us.USPowerProvider;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.us.USPowerSource;

public class Test {

	public static void main(String[] args) {
		TurkishPowerSource turkishPowerSource = new TurkishPowerProvider();

		// Can't have a reference of type Appliance anymore
		TurkishHomeApplianceCompatibleWithUSPowerSource turkishMixer = new TurkishHomeApplianceCompatibleWithUSPowerSource("Mixer");
		turkishMixer.setPowerSource(turkishPowerSource);
		turkishMixer.start();
		turkishMixer.stop();

		System.out.println();
		
		USPowerSource usPowerSource = new USPowerProvider();

		turkishMixer.setUSPowerSource(usPowerSource);
		turkishMixer.start();
		turkishMixer.stop();
	}
}
