package dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr;

public interface Appliance {
	
	public void setPowerSource(TurkishPowerSource powerSource);
	
	public void start();
	
	public void stop();

}
