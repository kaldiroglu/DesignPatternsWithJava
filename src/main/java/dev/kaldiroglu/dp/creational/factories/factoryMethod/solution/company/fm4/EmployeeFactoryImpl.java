package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm4;

public class EmployeeFactoryImpl implements EmployeeFactory{

	@Override
	public Employee createEmployee(String name) {
		Employee employee = new Employee(EmployeeRandomizer.createId(), name,
				EmployeeRandomizer.createYear(), EmployeeRandomizer.createDepartment());
		return employee;
	}
	
	@Override
	public Manager createManager(String name, String workingDepartment) {
		Manager manager = new Manager(EmployeeRandomizer.createId(), name,
				EmployeeRandomizer.createYear(), workingDepartment, workingDepartment);
		return manager;
	}
	
	@Override
	public Director createDirector(String name, String workingDepartment, int bonus) {
		Director director = new Director(EmployeeRandomizer.createId(), name,
				EmployeeRandomizer.createYear(), workingDepartment, workingDepartment, bonus);
		return director;
	}
}
