package dev.kaldiroglu.dp.creational.singleton.enumeration;

import java.io.Serializable;
import java.util.Calendar;

public enum EnumSingleton implements Serializable{

	SINGLETON;

	// No need for default constructor. It is already defined as private
	private EnumSingleton() {
		System.out.println("in EnumSingleton()");
	}

	// In fact no need for this method because SINGLETON is already public.
	public static EnumSingleton getInstance() {
		return SINGLETON;
	}
	Calendar cal1 = Calendar.getInstance();
	Calendar cal2 = Calendar.getInstance();
}
