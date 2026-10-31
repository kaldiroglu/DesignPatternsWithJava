package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.variety2;

import java.util.List;

public class Test {

	public static void main(String[] args) {
		System.out.println("*** Main in variety2 ***\n");

		HR hr = new HR(new EmployeeFactory());
		PayrollOffice po = new PayrollOffice();

		// Add more employees
		hr.addEmployee();
		hr.addEmployee();
		hr.addEmployee();
		hr.addManager();
		hr.addDirector();
		
		hr.listEmployees();

		System.out.println();
		
		// Now pay time!
		List<Employee> employees = hr.getEmployees();

		for (Employee employee : employees)
			po.paySalary(employee);
	}
}
