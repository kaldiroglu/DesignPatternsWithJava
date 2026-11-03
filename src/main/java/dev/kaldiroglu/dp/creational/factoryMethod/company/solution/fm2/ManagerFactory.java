package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm2;

public class ManagerFactory implements Factory {

	@Override
	public Manager create(String name) {
		String department = EmployeeRandomizer.createDepartment();
		Manager manager = new Manager(EmployeeRandomizer.createId(), name,
				EmployeeRandomizer.createYear(), department, 8);
		return manager;
	}
}
