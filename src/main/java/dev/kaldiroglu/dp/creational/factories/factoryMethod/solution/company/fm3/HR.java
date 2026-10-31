package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm3;


import java.util.ArrayList;
import java.util.List;

public class HR {
	private EmployeeFactory employeeFactory;
	private ManagerFactory managerFactory;
	private DirectorFactory directorFactory;

	private List<Employee> employees = new ArrayList<Employee>();

	public HR(EmployeeFactory employeeFactory, ManagerFactory managerFactory, DirectorFactory directorFactory) {
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
		Employee employee = employeeFactory.create(EmployeeRandomizer.createName());
		employees.add(employee);
	}

	public void addManager() {
		Employee employee = managerFactory.create(EmployeeRandomizer.createName(), EmployeeRandomizer.createDepartment());
		employees.add(employee);
	}

	public void addDirector() {
		Employee employee = directorFactory.create(EmployeeRandomizer.createName(), EmployeeRandomizer.createDepartment(), 100_000);
		employees.add(employee);
	}

	public void listEmployees(){
		System.out.println("All Employees");
		for(Employee employee : employees)
			System.out.println(employee);
	}
}
