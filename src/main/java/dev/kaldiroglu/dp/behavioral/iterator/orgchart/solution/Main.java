package dev.kaldiroglu.dp.behavioral.iterator.orgchart.solution;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

/**
 * Builds a small company, walks it in both orders, and compares it with a reorganized copy
 * in which three people are new.
 */
public final class Main {

    static Department company(String salesHead) {
        return company(salesHead, "Can", "Elif");
    }

    static Department company(String salesHead, String exporter, String supporter) {
        Department export = new Department("Export")
                .add(new Employee(exporter, "export"));
        Department sales = new Department("Sales")
                .add(new Employee(salesHead, "head of sales"))
                .add(new Employee("Ali", "sales"))
                .add(export);
        Department support = new Department("Support")
                .add(new Employee(supporter, "support"));
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

        // three people are new after the reorganization
        Department reorganized = company("Zeynep", "Burak", "Ece");
        ChangeReport report = new ChangeReport();
        System.out.println("First change: "
                + report.firstDifference(company, reorganized).orElse("none"));
        System.out.println("All changes:");
        for (String change : report.allDifferences(company, reorganized)) {
            System.out.println("  " + change);
        }
    }
}
