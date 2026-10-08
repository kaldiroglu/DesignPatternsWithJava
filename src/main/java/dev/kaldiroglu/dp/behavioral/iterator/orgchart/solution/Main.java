package dev.kaldiroglu.dp.behavioral.iterator.orgchart.solution;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

/** Builds a small company, walks it in both orders, and compares it with a reorganized copy. */
public final class Main {

    static Department company(String salesHead) {
        Department export = new Department("Export")
                .add(new Employee("Can", "export"));
        Department sales = new Department("Sales")
                .add(new Employee(salesHead, "head of sales"))
                .add(new Employee("Ali", "sales"))
                .add(export);
        Department support = new Department("Support")
                .add(new Employee("Elif", "support"));
        Department operations = new Department("Operations")
                .add(new Employee("Mert", "head of operations"))
                .add(support);
        return new Department("Head office")
                .add(new Employee("Ayse", "CEO"))
                .add(sales)
                .add(operations);
    }

    public static void main(String[] args) {
        Department company = company("Deniz");

        System.out.println("Department by department:");
        for (Employee employee : company) {
            System.out.println("  " + employee);
        }

        System.out.println("Level by level:");
        for (Employee employee : company.byLevel()) {
            System.out.println("  " + employee);
        }

        Department reorganized = company("Zeynep");
        System.out.println("First change: "
                + new ChangeReport().firstDifference(company, reorganized).orElse("none"));
    }
}
