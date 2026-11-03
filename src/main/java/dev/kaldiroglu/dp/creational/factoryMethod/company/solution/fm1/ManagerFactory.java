package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1;

public class ManagerFactory implements Factory {

	@Override
	public Manager create() {
		String department = EmployeeRandomizer.createDepartment();
		Manager manager = new Manager(EmployeeRandomizer.createId(), EmployeeRandomizer.createName(),
				EmployeeRandomizer.createYear(), department, 8);
		return manager;
	}
}
