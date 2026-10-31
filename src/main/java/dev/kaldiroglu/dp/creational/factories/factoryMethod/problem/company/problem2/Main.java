package dev.kaldiroglu.dp.creational.factories.factoryMethod.problem.company.problem2;

import java.util.List;

public class Main {

	public static void main(String[] args) {
		System.out.println("*** Main in problem2 ***");
		HR hr = new HR();

		hr.addEmployee(1, "Ahmet", 10, "Production");
		hr.addEmployee(2, "Zeynep", 3, "Sales");
		hr.addEmployee(3, "Kemal", 7, "Production");

		hr.addManager(4, "Mehmet", 14, "Production",  "Production");
		hr.addManager(5, "Sami", 2, "Marketing", "Marketing" );

		hr.addDirector(6, "Ozlem", 16, "Marketing", "Director", 100_000);
		hr.addDirector(7, "Ahmet", 19, "Production", "Director", 5000);

		// Now pay time!
		PayrollOffice po = new PayrollOffice();
		List<Employee> employees = hr.getEmployees();

		for (Employee employee : employees)
			po.paySalary(employee);
	}
}
