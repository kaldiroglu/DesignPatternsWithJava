package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm6;

import dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm1.Employee;
import dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm1.PayrollOffice;
import dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm5.HR;

import java.util.List;

public class Test {

	public static void main(String[] args) {
		System.out.println("*** Main in fm6 ***\n");

		HR hr = new HR();

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
		PayrollOffice po = new PayrollOffice();
		for (Employee employee : employees)
			po.paySalary(employee);
	}
}
