package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.delegateObject;

public interface Appliance {
	
	public void setPowerSource(TurkishPowerSource powerSource);
	
	public void start();
	
	public void stop();

}
