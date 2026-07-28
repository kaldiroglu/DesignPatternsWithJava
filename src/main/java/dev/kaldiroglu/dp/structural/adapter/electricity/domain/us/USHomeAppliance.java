package dev.kaldiroglu.dp.structural.adapter.electricity.domain.us;

public class USHomeAppliance {
	private String name;
	private USPowerSource usPowerSource;

	public USHomeAppliance(String name){
		this.name = name;
	}
	
	public void setPowerSource(USPowerSource usPowerSource) {
		this.usPowerSource = usPowerSource;
	}

	public void start() {
		System.out.println("USHomeAppliance " + name + " is turning on!");
		usPowerSource.pushSwitch();
	}

	public void stop() {
		System.out.println("USHomeAppliance " + name + " turning off!");
		usPowerSource.pushSwitch();
	}
}
