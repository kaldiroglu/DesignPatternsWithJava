package dev.kaldiroglu.dp.creational.factories.factoryMethod.problem.company.problem1;

import java.util.List;

public class Main {

	public static void main(String[] args) {
		System.out.println("*** Main in problem1 ***");

		HR hr = new HR();

		hr.addEmployee(1, "Ahmet", 10, "Production", "Employee", "None", 0);
		hr.addEmployee(2, "Zeynep", 3, "Sales", "Employee", "None", 0);
		hr.addEmployee(3, "Kemal", 7, "Production", "Employee", "None", 0);
		hr.addEmployee(4, "Mehmet", 14, "Production", "Manager", "Production", 0);
		hr.addEmployee(5, "Sami", 2, "Production", "Employee", null, 0);
		hr.addEmployee(6, "Ozlem", 4, "Marketing", "Director", "Marketing", 100_000);
		hr.addEmployee(7, "Ahmet", 19, "Production", "Director", "Production", 5000);

		// Now pay time!
		PayrollOffice po = new PayrollOffice();
		List<Employee> employees = hr.getEmployees();

		for (Employee employee : employees)
			po.paySalary(employee);
	}
}
