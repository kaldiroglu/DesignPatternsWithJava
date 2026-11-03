package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm5;

import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.Employee;
import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.EmployeeRandomizer;
import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.PayrollOffice;

import java.util.List;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		EmployeeRandomizer.setGenerator(new Random(EmployeeRandomizer.SEED));

		HR hr = new HR();

		// Add more employees
		hr.addEmployee();
		hr.addEmployee();
		hr.addEmployee();
		hr.addManager();
		hr.addManager();
		hr.addDirector();


		// Now pay time!
		List<Employee> employees = hr.getEmployees();
		PayrollOffice po = new PayrollOffice();
		for (Employee employee : employees)
			po.paySalary(employee);
	}
}
