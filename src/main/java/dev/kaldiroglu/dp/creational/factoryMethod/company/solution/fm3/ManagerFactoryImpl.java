package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm3;

public class ManagerFactoryImpl implements ManagerFactory{

	@Override
	public Manager create(String name, String department) {
		Manager manager = new Manager(EmployeeRandomizer.createId(), name,
				EmployeeRandomizer.createYear(), department, 8);
		return manager;
	}

}
