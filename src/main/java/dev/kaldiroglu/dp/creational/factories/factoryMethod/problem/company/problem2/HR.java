package dev.kaldiroglu.dp.creational.factories.factoryMethod.problem.company.problem2;

import java.util.ArrayList;
import java.util.List;

public class HR {

    private List<Employee> employees = new ArrayList<Employee>();

    public HR() {
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public int getNumberOfEmployees() {
        return employees.size();
    }

    public void addEmployee(int no, String name, int year, String department) {
        Employee employee = new Employee(no, name, year, department);
        employees.add(employee);
    }

    public void addManager(int no, String name, int year, String department, String departmentManaged) {
        Employee employee = new Manager(no, name, year, department, departmentManaged);
        employees.add(employee);
    }

    public void addDirector(int no, String name, int year, String department, String departmentManaged,
                            double bonus) {
        Employee employee = new Director(no, name, year, department, departmentManaged, bonus);
        employees.add(employee);
    }
}

