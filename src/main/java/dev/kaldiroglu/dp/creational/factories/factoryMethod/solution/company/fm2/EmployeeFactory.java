package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm2;

public class EmployeeFactory implements Factory {

	@Override
	public Employee create(String name) {
		return new Employee(EmployeeRandomizer.createId(), name,
				EmployeeRandomizer.createYear(), EmployeeRandomizer.createDepartment());
	}
}
