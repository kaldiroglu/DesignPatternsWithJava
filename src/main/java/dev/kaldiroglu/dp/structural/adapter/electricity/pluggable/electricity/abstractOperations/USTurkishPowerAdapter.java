package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.abstractOperations;

public class USTurkishPowerAdapter extends PowerAdapter {
	private final USPowerSource usPowerSource;

	public USTurkishPowerAdapter(USPowerSource usPowerSource){
		System.out.println("\nUSTurkishPowerAdapter: Converting from USPowerSource to TurkishPowerSource");
		this.usPowerSource = usPowerSource;
	}

	@Override
	protected void deliver() {
		usPowerSource.pushSwitch();
	}

	@Override
	protected void cut() {
		usPowerSource.pushSwitch();
	}
}
