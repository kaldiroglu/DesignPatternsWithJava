package dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageOne;

import java.util.ArrayList;
import java.util.List;

/**
 * HR creates each person with new and then calls a setter for each field. HR must know which
 * setters each class needs. addDirector does not call setDepartment, and the program still
 * compiles and runs.
 */
public class HR {

    private final List<Employee> employees = new ArrayList<>();

    public List<Employee> getEmployees() {
        return employees;
    }

    public void addEmployee(int no, String name, int year, String department) {
        Employee employee = new Employee();
        employee.setNo(no);
        employee.setName(name);
        employee.setYear(year);
        employee.setDepartment(department);
        employees.add(employee);
    }

    public void addManager(int no, String name, int year, String department, int teamSize) {
        Manager manager = new Manager();
        manager.setNo(no);
        manager.setName(name);
        manager.setYear(year);
        manager.setDepartment(department);
        manager.setTeamSize(teamSize);
        employees.add(manager);
    }

    public void addDirector(int no, String name, int year, String department, int teamSize,
                            double bonus) {
        Director director = new Director();
        director.setNo(no);
        director.setName(name);
        director.setYear(year);
        director.setTeamSize(teamSize);
        director.setBonus(bonus);
        employees.add(director);
    }
}
