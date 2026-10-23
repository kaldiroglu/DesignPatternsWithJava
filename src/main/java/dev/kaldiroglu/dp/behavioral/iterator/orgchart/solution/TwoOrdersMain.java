package dev.kaldiroglu.dp.behavioral.iterator.orgchart.solution;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

/**
 * Walks one department in two orders: department by department, then level by level.
 */
public final class TwoOrdersMain {

    public static void main(String[] args) {
        Department company = SampleCompany.build("Deniz");

        System.out.println("Department by department:");
        for (Employee employee : company) {
            System.out.println("  " + employee);
        }

        System.out.println("Level by level:");
        for (Employee employee : company.byLevel()) {
            System.out.println("  " + employee);
        }
    }
}
