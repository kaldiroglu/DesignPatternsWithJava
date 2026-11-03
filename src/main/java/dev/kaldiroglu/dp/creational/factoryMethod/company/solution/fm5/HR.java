package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm5;

import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.Employee;

import java.util.ArrayList;
import java.util.List;

import static dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm5.EmployeeEnum.*;

public class HR {
	private List<Employee> employees = new ArrayList<Employee>();

	public List<Employee> getEmployees() {
		return employees;
	}

	public int getNumberOfEmployees() {
		return employees.size();
	}

	public void addEmployee() {
//		Employee employee = EmployeeFactory.createFromTypeName("Employee");
		Employee employee = EmployeeFactory.createFromEnum(EMPLOYEE);
		employees.add(employee);
	}

	public void addManager() {
//		Employee employee = EmployeeFactory.createFromTypeName("Manager");
		Employee employee = EmployeeFactory.createFromEnum(MANAGER);
		employees.add(employee);
	}

	public void addDirector() {
//		Employee employee = EmployeeFactory.createFromTypeName("Director");
		Employee employee = EmployeeFactory.createFromEnum(DIRECTOR);
		employees.add(employee);
	}
	
	public void listEmployees(){
		System.out.println("All Employees");
		for(Employee employee : employees)
			System.out.println(employee);
	}
}
