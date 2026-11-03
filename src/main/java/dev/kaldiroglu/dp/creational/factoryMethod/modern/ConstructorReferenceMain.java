package dev.kaldiroglu.dp.creational.factoryMethod.modern;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A constructor reference replaces a factory class that has one method. HR takes a
 * Supplier, so a new type needs no factory class and no change in HR.
 */
public class ConstructorReferenceMain {

    static class Employee {
        String title() {
            return "Employee";
        }
    }

    static class Manager extends Employee {
        @Override
        String title() {
            return "Manager";
        }
    }

    static class Director extends Manager {
        @Override
        String title() {
            return "Director";
        }
    }

    static class HR {
        private final Supplier<Employee> supplier;
        private final List<Employee> employees = new ArrayList<>();

        HR(Supplier<Employee> supplier) {
            this.supplier = supplier;
        }

        Employee hire() {
            Employee employee = supplier.get();
            employees.add(employee);
            return employee;
        }
    }

    public static void main(String[] args) {
        for (Supplier<Employee> supplier : List.<Supplier<Employee>>of(Employee::new, Manager::new, Director::new))
            System.out.println("HR hired: " + new HR(supplier).hire().title());
    }
}
