package dev.kaldiroglu.dp.behavioral.iterator.orgchart.problem;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * One caller of {@link OpenDepartment}. It needs everyone in the department, so it writes
 * the recursion itself. The phone book, the roll call and every other caller write the same
 * recursion again.
 */
public final class PayrollRun {

    public List<String> payslips(OpenDepartment department) {
        List<String> payslips = new ArrayList<>();
        collect(department, payslips);
        return payslips;
    }

    private void collect(OpenDepartment department, List<String> payslips) {
        for (Employee employee : department.members()) {
            payslips.add("payslip for " + employee.name());
        }
        for (OpenDepartment unit : department.units()) {
            collect(unit, payslips);
        }
    }
}
