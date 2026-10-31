package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm1;

import java.util.ArrayList;
import java.util.List;

public class HR {
	private Factory employeeFactory;
	private Factory managerFactory;
	private Factory directorFactory;

	private List<Employee> employees = new ArrayList<Employee>();

	public HR(Factory employeeFactory, Factory managerFactory, Factory directorFactory) {
		this.employeeFactory = employeeFactory;
		this.managerFactory = managerFactory;
		this.directorFactory = directorFactory;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public int getNumberOfEmployees() {
		return employees.size();
	}

	public void addEmployee() {
		Employee employee = employeeFactory.create();
		employees.add(employee);
	}

	public void addManager() {
		Employee employee = managerFactory.create();
		employees.add(employee);
	}

	public void addDirector() {
		Employee employee = directorFactory.create();
		employees.add(employee);
	}
	
	public void listEmployees(){
		System.out.println("All Employees");
		for(Employee employee : employees)
			System.out.println(employee);
	}
}
