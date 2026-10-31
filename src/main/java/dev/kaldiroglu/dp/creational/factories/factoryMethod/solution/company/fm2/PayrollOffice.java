package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm2;

public class PayrollOffice {

	public void paySalary(Employee employee) {
		String name = employee.getName();
		String department = employee.getDepartment();
		double salary = employee.calculateSalary();
		
		System.out.println("Paying " + salary + " to " + name + " in " + department);
	}
}
