package dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageThree;

import dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageTwo.Director;
import dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageTwo.Employee;
import dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageTwo.Manager;

/**
 * One method creates every kind of employee, and a switch chooses the class. Its signature
 * must suit all three kinds, so it has no parameter for a team size or a bonus, and every
 * manager and director gets the same fixed values.
 */
public class EmployeeCreator {

    public static final int TEAM_SIZE = 8;
    public static final int DIRECTOR_TEAM_SIZE = 40;
    public static final double BONUS = 5000;

    /** @throws IllegalArgumentException if the type is not one of the three names */
    public static Employee create(String type, int no, String name, int year, String department) {
        return switch (type) {
            case "Employee" -> new Employee(no, name, year, department);
            case "Manager" -> new Manager(no, name, year, department, TEAM_SIZE);
            case "Director" -> new Director(no, name, year, department, DIRECTOR_TEAM_SIZE, BONUS);
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }
}
