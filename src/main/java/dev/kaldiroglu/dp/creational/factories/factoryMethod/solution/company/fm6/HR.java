package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm6;

import dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm1.Employee;
import dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm5.EmployeeFactory;

import java.util.ArrayList;
import java.util.List;

public class HR {
    private List<Employee> employees = new ArrayList<Employee>();

    public List<Employee> getEmployees() {
        return employees;
    }

    public int getNumberOfEmployees() {
        return employees.size();
    }

    public void addEmployee() {
        Employee employee = EmployeeFactory.create1("Employee");
//		Employee employee = EmployeeFactory.create1(EMPLOYEE);
        employees.add(employee);
    }

    public void addManager() {
        Employee employee = EmployeeFactory.create1("Manager");
//		Employee employee = EmployeeFactory.create1(MANAGER);
        employees.add(employee);
    }

    public void addDirector() {
        Employee employee = EmployeeFactory.create1("Director");
//		Employee employee = EmployeeFactory.create1(DIRECTOR);
        employees.add(employee);
    }

    public void listEmployees() {
        System.out.println("All Employees");
        for (Employee employee : employees)
            System.out.println(employee);
    }
}