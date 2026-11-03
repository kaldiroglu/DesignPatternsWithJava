package dev.kaldiroglu.dp.creational.factoryMethod.modern;

import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.Director;
import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.Employee;
import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.Manager;
import dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.PayrollOffice;

/**
 * The kind of employee is a sealed interface, and each record holds the data that kind
 * needs. The switch lists every record, so the compiler rejects it if a kind is missing.
 */
public class SealedSwitchMain {

    sealed interface EmployeeKind {}

    record Staff(String department) implements EmployeeKind {}

    record ManagerOf(String department, int teamSize) implements EmployeeKind {}

    record DirectorOf(String department, int teamSize, double bonus) implements EmployeeKind {}

    static Employee create(int no, String name, int year, EmployeeKind kind) {
        return switch (kind) {
            case Staff(String department) -> new Employee(no, name, year, department);
            case ManagerOf(String department, int teamSize) ->
                    new Manager(no, name, year, department, teamSize);
            case DirectorOf(String department, int teamSize, double bonus) ->
                    new Director(no, name, year, department, teamSize, bonus);
        };
    }

    public static void main(String[] args) {
        PayrollOffice office = new PayrollOffice();
        office.paySalary(create(1, "Ali", 10, new Staff("Sales")));
        office.paySalary(create(2, "Elif", 14, new ManagerOf("Production", 12)));
        office.paySalary(create(3, "Cem", 19, new DirectorOf("Marketing", 40, 5000)));
    }
}
