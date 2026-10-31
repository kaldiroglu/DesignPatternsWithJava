package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm5;

import dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm1.Employee;

import java.util.ArrayList;
import java.util.List;

import static dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm5.EmployeeEnum.*;

public class HR {
	private List<Employee> employees = new ArrayList<Employee>();

	public List<Employee> getEmployees() {
		return employees;
	}

	public int getNumberOfEmployees() {
		return employees.size();
	}

	public void addEmployee() {
//		Employee employee = EmployeeFactory.create1("Employee");
		Employee employee = EmployeeFactory.create2(EMPLOYEE);
		employees.add(employee);
	}

	public void addManager() {
//		Employee employee = EmployeeFactory.create1("Manager");
		Employee employee = EmployeeFactory.create2(MANAGER);
		employees.add(employee);
	}

	public void addDirector() {
//		Employee employee = EmployeeFactory.create1("Director");
		Employee employee = EmployeeFactory.create2(DIRECTOR);
		employees.add(employee);
	}
	
	public void listEmployees(){
		System.out.println("All Employees");
		for(Employee employee : employees)
			System.out.println(employee);
	}
}
