package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm3;

public class ManagerFactoryImpl implements ManagerFactory{

	@Override
	public Manager create(String name, String workingDepartment) {
		Manager manager = new Manager(EmployeeRandomizer.createId(), name,
				EmployeeRandomizer.createYear(), workingDepartment, workingDepartment);
		return manager;
	}

}
