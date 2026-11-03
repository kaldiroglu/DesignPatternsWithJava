package dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageThree;

import dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageTwo.Employee;

import java.util.ArrayList;
import java.util.List;

/** HR no longer calls new. It passes a type name to one shared method. */
public class HR {

    private final List<Employee> employees = new ArrayList<>();

    public List<Employee> getEmployees() {
        return employees;
    }

    public void add(String type, int no, String name, int year, String department) {
        employees.add(EmployeeCreator.create(type, no, name, year, department));
    }
}
