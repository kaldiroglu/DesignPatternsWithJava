package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm6;

import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.Employee;
import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.EmployeeRandomizer;

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
        Employee employee = EmployeeFactory.createEmployee(EmployeeRandomizer.createName());
        employees.add(employee);
    }

    public void addManager() {
        Employee employee = EmployeeFactory.createManager(EmployeeRandomizer.createName(),
                EmployeeRandomizer.createDepartment());
        employees.add(employee);
    }

    public void addDirector() {
        Employee employee = EmployeeFactory.createDirector(EmployeeRandomizer.createName(),
                EmployeeRandomizer.createDepartment(), 5000);
        employees.add(employee);
    }

    public void listEmployees() {
        System.out.println("All Employees");
        for (Employee employee : employees)
            System.out.println(employee);
    }
}
