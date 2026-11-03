package dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageTwo;

import java.util.ArrayList;
import java.util.List;

public class HR {

    private final List<Employee> employees = new ArrayList<Employee>();

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

    public void addManager(int no, String name, int year, String department, int teamSize) {
        Employee employee = new Manager(no, name, year, department, teamSize);
        employees.add(employee);
    }

    public void addDirector(int no, String name, int year, String department, int teamSize,
                            double bonus) {
        Employee employee = new Director(no, name, year, department, teamSize, bonus);
        employees.add(employee);
    }
}

