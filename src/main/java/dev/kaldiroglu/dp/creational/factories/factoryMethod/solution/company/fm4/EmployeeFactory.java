package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm4;

public interface EmployeeFactory{

	public Employee createEmployee(String name);
	
	public Manager createManager(String name, String workingDepartment);
	
	public Director createDirector(String name, String workingDepartment, int bonus);
}
