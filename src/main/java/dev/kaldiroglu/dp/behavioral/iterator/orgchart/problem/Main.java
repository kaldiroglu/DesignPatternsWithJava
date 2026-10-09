package dev.kaldiroglu.dp.behavioral.iterator.orgchart.problem;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs the three stages: give out the lists, copy them, call back, and shows what each one
 * costs.
 */
public final class Main {

    static CallbackDepartment company(String salesHead) {
        CallbackDepartment sales = new CallbackDepartment("Sales")
                .add(new Employee(salesHead, "head of sales"))
                .add(new Employee("Ali", "sales"))
                .add(new CallbackDepartment("Export").add(new Employee("Can", "export")));
        CallbackDepartment operations = new CallbackDepartment("Operations")
                .add(new Employee("Mert", "head of operations"))
                .add(new CallbackDepartment("Support").add(new Employee("Elif", "support")));
        return new CallbackDepartment("Head office")
                .add(new Employee("Ayse", "CEO")).add(sales).add(operations);
    }

    public static void main(String[] args) {
        Employee ali = new Employee("Ali", "sales");
        OpenDepartment open = new OpenDepartment("Sales")
                .add(new Employee("Deniz", "head of sales")).add(ali);
        open.members().remove(ali);
        System.out.println("Stage one: a caller removed Ali from the real list. Payroll: "
                + new PayrollRun().payslips(open));

        CopyingDepartment copying = new CopyingDepartment("Sales")
                .add(new Employee("Deniz", "head of sales"));
        System.out.println("Stage two: every call makes a new copy: "
                + (copying.everyone() != copying.everyone()));

        CallbackDepartment before = company("Deniz");
        List<Employee> byLevel = new ArrayList<>();
        before.forEachMemberByLevel(byLevel::add);
        System.out.println("Stage three, level by level: " + byLevel);

        System.out.println("HR compares two charts: "
                + new ChangeReport().firstDifference(before, company("Zeynep")).orElse("none"));
        System.out.println("A callback walks one chart at a time,"
                + " so the report first copied both charts whole.");
    }
}
