package dev.kaldiroglu.dp.structural.adapter.electricity.problem1;

import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.TurkishPowerProvider;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.TurkishPowerSource;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.us.USPowerProvider;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.us.USPowerSource;

public class Main {

	public static void main(String[] args) {
		TurkishPowerSource turkishPowerSource = new TurkishPowerProvider();

		// Can't have a reference of type Appliance anymore
		TurkishHomeAppliance turkishShaver = new TurkishHomeAppliance("Shaver");
		turkishShaver.setPowerSource(turkishPowerSource);
		turkishShaver.start();
		turkishShaver.stop();

		System.out.println();
		
		USPowerSource usPowerSource = new USPowerProvider();

		turkishShaver.setUSPowerSource(usPowerSource);
		turkishShaver.start();
		turkishShaver.stop();
	}
}
