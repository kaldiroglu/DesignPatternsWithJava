package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.variety2;

import java.util.ArrayList;
import java.util.List;

import static dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.variety2.EmployeeEnum.*;

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
//		Employee employee = employeeFactory.create1("Employee");
		Employee employee = employeeFactory.create2(EMPLOYEE);
		employees.add(employee);
	}

	public void addManager() {
//		Employee employee = employeeFactory.create1("Manager");
		Employee employee = employeeFactory.create2(MANAGER);
		employees.add(employee);
	}

	public void addDirector() {
//		Employee employee = employeeFactory.create1("Director");
		Employee employee = employeeFactory.create2(DIRECTOR);
		employees.add(employee);
	}
	
	public void listEmployees(){
		System.out.println("All Employees");
		for(Employee employee : employees)
			System.out.println(employee);
	}
}
