package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm4;

import java.util.ArrayList;
import java.util.List;

public class HR {

	private EmployeeFactory employeeFactory;

	private List<Employee> employees = new ArrayList<Employee>();

	public HR(EmployeeFactory employeeFactory) {
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
		Employee employee = employeeFactory.createDirector(EmployeeRandomizer.createName(), EmployeeRandomizer.createDepartment(), 5000);
		employees.add(employee);
	}

	public void listEmployees(){
		System.out.println("All Employees");
		for(Employee employee : employees)
			System.out.println(employee);
	}
}
