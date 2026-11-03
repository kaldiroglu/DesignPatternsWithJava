package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm4;

public class EmployeeFactoryImpl implements EmployeeFactory{

	@Override
	public Employee createEmployee(String name) {
		Employee employee = new Employee(EmployeeRandomizer.createId(), name,
				EmployeeRandomizer.createYear(), EmployeeRandomizer.createDepartment());
		return employee;
	}
	
	@Override
	public Manager createManager(String name, String department) {
		Manager manager = new Manager(EmployeeRandomizer.createId(), name,
				EmployeeRandomizer.createYear(), department, 8);
		return manager;
	}
	
	@Override
	public Director createDirector(String name, String department, int bonus) {
		Director director = new Director(EmployeeRandomizer.createId(), name,
				EmployeeRandomizer.createYear(), department, 40, bonus);
		return director;
	}
}
