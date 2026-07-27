package dev.kaldiroglu.dp.structural.adapter.electricity.problem1;

import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.HomeAppliance;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.TurkishPowerSource;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.us.USPowerSource;

public class TurkishHomeAppliance extends HomeAppliance {
	private String name;
	private USPowerSource usPowerSource;
	private boolean turkishPowerSource;

	public TurkishHomeAppliance(String name) {
		this.name = name;
	}

	@Override
	public void setPowerSource(TurkishPowerSource powerSource) {
		this.powerSource = powerSource;
		turkishPowerSource = true;
	}

	public void setUSPowerSource(USPowerSource usPowerSource) {
		this.usPowerSource = usPowerSource;
		turkishPowerSource = false;
	}

	@Override
	public void start() {
		System.out.println("TurkishHomeAppliance " + name + " is starting!");
		if (turkishPowerSource)
			powerSource.turnOn();
		else
			usPowerSource.pushSwitch();
	}

	@Override
	public void stop() {
		System.out.println("TurkishHomeAppliance " + name + " stoping!");
		if (turkishPowerSource)
			powerSource.turnOff();
		else
			usPowerSource.pushSwitch();
	}
}
