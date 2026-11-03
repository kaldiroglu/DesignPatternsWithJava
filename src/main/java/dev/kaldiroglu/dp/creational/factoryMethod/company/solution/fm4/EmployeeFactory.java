package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm4;

public interface EmployeeFactory{

	public Employee createEmployee(String name);
	
	public Manager createManager(String name, String department);
	
	public Director createDirector(String name, String department, int bonus);
}
