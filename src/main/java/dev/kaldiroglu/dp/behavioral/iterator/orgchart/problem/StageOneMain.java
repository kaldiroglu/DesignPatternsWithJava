package dev.kaldiroglu.dp.behavioral.iterator.orgchart.problem;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

/**
 * Stage one: the department gives out its internal lists, so a caller can change them.
 */
public final class StageOneMain {

    public static void main(String[] args) {
        Employee ali = new Employee("Ali", "sales");
        OpenDepartment open = new OpenDepartment("Sales")
                .add(new Employee("Deniz", "head of sales")).add(ali);
        open.members().remove(ali);
        System.out.println("Stage one: a caller removed Ali from the real list. Payroll: "
                + new PayrollRun().payslips(open));
    }
}
