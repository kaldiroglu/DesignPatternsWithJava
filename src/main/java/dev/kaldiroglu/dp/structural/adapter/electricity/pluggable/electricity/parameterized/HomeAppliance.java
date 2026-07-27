package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.parameterized;

public abstract class HomeAppliance implements Appliance {
	protected TurkishPowerSource powerSource;

	@Override
	public void setPowerSource(TurkishPowerSource powerSource) {
		this.powerSource = powerSource;
	}
}
