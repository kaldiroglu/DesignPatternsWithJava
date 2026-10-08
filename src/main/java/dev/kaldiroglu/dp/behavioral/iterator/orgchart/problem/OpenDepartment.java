package dev.kaldiroglu.dp.behavioral.iterator.orgchart.problem;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Stage one: <b>the department gives out its own lists.</b>
 * <p>
 * A department has members and sub-departments. Payroll, the phone book and every other
 * program need "everyone in this department", so the department returns its two internal
 * lists and each caller walks them.
 * <p>
 * It works. What it costs:
 * <ul>
 *   <li>Every caller writes the same recursion. See {@link PayrollRun}.</li>
 *   <li>Callers get the real lists, so a caller can add or remove people by mistake.</li>
 *   <li>The department can never change how it stores people. Every caller depends on
 *       {@code List}.</li>
 * </ul>
 */
public final class OpenDepartment {

    private final String name;
    private final List<Employee> members = new ArrayList<>();
    private final List<OpenDepartment> units = new ArrayList<>();

    public OpenDepartment(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public OpenDepartment add(Employee employee) {
        members.add(employee);
        return this;
    }

    public OpenDepartment add(OpenDepartment unit) {
        units.add(unit);
        return this;
    }

    /** The internal list itself. */
    public List<Employee> members() {
        return members;
    }

    /** The internal list itself. */
    public List<OpenDepartment> units() {
        return units;
    }
}
