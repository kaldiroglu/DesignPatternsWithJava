package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm3;

public class EmployeeFactoryImpl implements EmployeeFactory{

	@Override
	public Employee create(String name) {
		Employee employee = new Employee(EmployeeRandomizer.createId(), name,
				EmployeeRandomizer.createYear(), EmployeeRandomizer.createDepartment());
		return employee;
	}
}
