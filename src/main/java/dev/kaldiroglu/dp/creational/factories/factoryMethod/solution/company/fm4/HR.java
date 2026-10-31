package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm4;

import java.util.ArrayList;
import java.util.List;

public class HR {

	private EmployeeFactory employeeFactory;

	private List<Employee> employees = new ArrayList<Employee>();

	public HR(EmployeeFactoryImpl employeeFactory) {
		this.employeeFactory = employeeFactory;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public int getNumberOfEmployees() {
		return employees.size();
	}

	public void addEmployee() {
		Employee employee = employeeFactory.createEmployee(EmployeeRandomizer.createName());
		employees.add(employee);
	}

	public void addManager() {
		Employee employee = employeeFactory.createManager(EmployeeRandomizer.createName(), EmployeeRandomizer.createDepartment());
		employees.add(employee);
	}

	public void addDirector() {
		Employee employee = employeeFactory.createDirector(EmployeeRandomizer.createName(), EmployeeRandomizer.createDepartment(), 100_000);
		employees.add(employee);
	}

	public void listEmployees(){
		System.out.println("All Employees");
		for(Employee employee : employees)
			System.out.println(employee);
	}
}
