package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm2;

import java.util.List;

public class Test {

	public static void main(String[] args) {
		System.out.println("*** Main in fm2 ***\n");

		HR hr = new HR(new EmployeeFactory(), new ManagerFactory(), new DirectorFactory());
		PayrollOffice po = new PayrollOffice();

		// Add more employees
		hr.addEmployee();
		hr.addEmployee();
		hr.addEmployee();
		hr.addManager();
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
