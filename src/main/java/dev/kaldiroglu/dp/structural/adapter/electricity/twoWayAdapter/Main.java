package dev.kaldiroglu.dp.structural.adapter.electricity.twoWayAdapter;


import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.Appliance;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.TurkishHomeAppliance;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.TurkishPowerProvider;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.TurkishPowerSource;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.us.USHomeAppliance;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.us.USPowerProvider;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.us.USPowerSource;

public class Main {

	public static void main(String[] args) {
		// In US with Turkish home appliance
		USPowerSource usPowerSource = new USPowerProvider();
		TwoWayUSTurkishPowerAdapter twoWayUSTurkishPowerAdapter1 = new TwoWayUSTurkishPowerAdapter(usPowerSource);
		
		System.out.println();
		
		Appliance turkishMixer = new TurkishHomeAppliance("Mixer");
		turkishMixer.setPowerSource(twoWayUSTurkishPowerAdapter1);
		turkishMixer.start();
		turkishMixer.stop();
		
		System.out.println();
		
		// In Turkey with US home appliance
		TurkishPowerSource turkishPowerSource = new TurkishPowerProvider();
		TwoWayUSTurkishPowerAdapter twoWayUSTurkishPowerAdapter2 = new TwoWayUSTurkishPowerAdapter(turkishPowerSource);
		
		System.out.println();
		
		USHomeAppliance usBroom = new USHomeAppliance("Broom");
		usBroom.setPowerSource(twoWayUSTurkishPowerAdapter2);
		usBroom.start();
		usBroom.stop();
	}
}
