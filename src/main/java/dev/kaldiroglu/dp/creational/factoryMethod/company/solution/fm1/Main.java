package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1;

import java.util.List;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		EmployeeRandomizer.setGenerator(new Random(EmployeeRandomizer.SEED));

		HR hr = new HR(new EmployeeFactory(), new ManagerFactory(), new DirectorFactory());
		PayrollOffice po = new PayrollOffice();

		// Add more employees
		hr.addEmployee();
		hr.addEmployee();
		hr.addEmployee();
		hr.addManager();
		hr.addManager();
		hr.addDirector();

		// Now pay time!
		List<Employee> employees = hr.getEmployees();

		for (Employee employee : employees)
			po.paySalary(employee);

		for (Employee employee : employees)
			IO.println(employee.toString());
	}
}
