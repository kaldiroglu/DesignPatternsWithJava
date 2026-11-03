package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm5;

import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.*;

public class EmployeeFactory {

	/**
	 * Creates the employee named by a string. The compiler cannot check a string, so an
	 * unknown type fails at run time.
	 *
	 * @throws IllegalArgumentException if the type is not one of the three names
	 */
	public static Employee createFromTypeName(String type) {
		int id = EmployeeRandomizer.createId();
		String name = EmployeeRandomizer.createName();
		int year = EmployeeRandomizer.createYear();
		String department = EmployeeRandomizer.createDepartment();

		return switch (type) {
			case "Employee" -> new Employee(id, name, year, department);
			case "Manager" -> new Manager(id, name, year, department, 8);
			case "Director" -> new Director(id, name, year, "Management", 40, 5000);
			default -> throw new IllegalArgumentException("Invalid type: " + type);
		};
	}

	/** Creates the employee named by an enum constant, so an unknown type does not compile. */
	public static Employee createFromEnum(EmployeeEnum type) {
		int id = EmployeeRandomizer.createId();
		String name = EmployeeRandomizer.createName();
		int year = EmployeeRandomizer.createYear();
		String department = EmployeeRandomizer.createDepartment();

		return switch (type) {
			case EMPLOYEE -> new Employee(id, name, year, department);
			case MANAGER -> new Manager(id, name, year, department, 8);
			case DIRECTOR -> new Director(id, name, year, "Management", 40, 5000);
		};
	}
}
