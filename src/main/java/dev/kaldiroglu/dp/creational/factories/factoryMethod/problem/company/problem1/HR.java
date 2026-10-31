package dev.kaldiroglu.dp.creational.factories.factoryMethod.problem.company.problem1;

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

    public void addEmployee(int no, String name, int year, String department, String type, String departmentManaged,
                            double bonus) {
        Employee employee = switch (type) {
            case "Employee":
                employee = new Employee(no, name, year, department, type);
                yield employee;

            case "Manager":
                employee = new Employee(no, name, year, department, type, departmentManaged);
                yield employee;

            case "Director":
                employee = new Employee(no, name, year, department, type, departmentManaged, bonus);
                yield employee;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        };
        employees.add(employee);
    }
}
