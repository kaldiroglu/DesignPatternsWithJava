package dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageOne;

import java.util.List;

public class Main {

	public static void main(String[] args) {
		HR hr = new HR();

		hr.addEmployee(1, "Ahmet", 10, "Production");
		hr.addEmployee(2, "Zeynep", 3, "Sales");
		hr.addEmployee(3, "Kemal", 7, "Production");

		hr.addManager(4, "Mehmet", 14, "Production", 12);
		hr.addManager(5, "Sami", 2, "Marketing", 6);

		hr.addDirector(7, "Ahmet", 19, "Production", 40, 5000);

		// Now pay time!
		PayrollOffice po = new PayrollOffice();
		List<Employee> employees = hr.getEmployees();
		for (Employee employee : employees)
			po.paySalary(employee);
	}
}
