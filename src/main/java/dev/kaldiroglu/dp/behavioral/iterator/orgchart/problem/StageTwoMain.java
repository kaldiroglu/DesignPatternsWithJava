package dev.kaldiroglu.dp.behavioral.iterator.orgchart.problem;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

/**
 * Stage two: the department copies everyone into a new list, on every call.
 */
public final class StageTwoMain {

    public static void main(String[] args) {
        CopyingDepartment copying = new CopyingDepartment("Sales")
                .add(new Employee("Deniz", "head of sales"));
        System.out.println("Stage two: every call makes a new copy: "
                + (copying.everyone() != copying.everyone()));
    }
}
