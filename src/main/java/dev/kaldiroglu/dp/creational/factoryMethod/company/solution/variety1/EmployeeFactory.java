package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.variety1;

public class EmployeeFactory {

	public Employee create() {
        return new Employee(EmployeeRandomizer.createId(), EmployeeRandomizer.createName(),
                EmployeeRandomizer.createYear(), EmployeeRandomizer.createDepartment());
	}
}
