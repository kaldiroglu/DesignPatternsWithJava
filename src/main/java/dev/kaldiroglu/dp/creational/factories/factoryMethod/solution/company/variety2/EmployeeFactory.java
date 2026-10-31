package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.variety2;

public class EmployeeFactory {

	public Employee create1(String type) {
		Employee employee = null;
		int id = EmployeeRandomizer.createId();
		String name = EmployeeRandomizer.createName();
		int year = EmployeeRandomizer.createYear();

		String department = EmployeeRandomizer.createDepartment();
		switch (type) {
			case "Employee":
				employee = new Employee(id, name, year, department);
				break;

			case "Manager":
				employee = new Manager(id, name, year, department, department);
				break;

			case "Director":
				employee = new Director(id, name, year, "Management", "Management", 5000);
				break;
		}
		return employee;
	}

	public Employee create2(EmployeeEnum type) {
		Employee employee = null;
		int id = EmployeeRandomizer.createId();
		String name = EmployeeRandomizer.createName();
		int year = EmployeeRandomizer.createYear();

		String department = EmployeeRandomizer.createDepartment();
		switch (type) {
			case EMPLOYEE:
				employee = new Employee(id, name, year, department);
				break;

			case MANAGER:
				employee = new Manager(id, name, year, department, department);
				break;

			case DIRECTOR:
				employee = new Director(id, name, year, "Management", "Management", 100_000);
				break;
		}
		return employee;
	}
}
