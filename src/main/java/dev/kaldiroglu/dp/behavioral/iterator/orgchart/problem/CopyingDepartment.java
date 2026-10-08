package dev.kaldiroglu.dp.behavioral.iterator.orgchart.problem;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Stage two: <b>the department copies everyone into a new list.</b>
 * <p>
 * A real improvement on stage one. The recursion is written once, here. Callers get a copy
 * they cannot change, and they no longer see how the department stores people.
 * <p>
 * What it costs:
 * <ul>
 *   <li>Every call copies the whole department, even when the caller needs only the first
 *       person who matches.</li>
 *   <li>The copy has one order: members first, then each sub-department in turn. A caller
 *       that needs another order must ask for another method.</li>
 * </ul>
 */
public final class CopyingDepartment {

    private final String name;
    private final List<Employee> members = new ArrayList<>();
    private final List<CopyingDepartment> units = new ArrayList<>();

    public CopyingDepartment(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public CopyingDepartment add(Employee employee) {
        members.add(employee);
        return this;
    }

    public CopyingDepartment add(CopyingDepartment unit) {
        units.add(unit);
        return this;
    }

    /** Everyone in this department and below it, as a new list. */
    public List<Employee> everyone() {
        List<Employee> everyone = new ArrayList<>();
        collect(this, everyone);
        return List.copyOf(everyone);
    }

    private static void collect(CopyingDepartment department, List<Employee> everyone) {
        everyone.addAll(department.members);
        for (CopyingDepartment unit : department.units) {
            collect(unit, everyone);
        }
    }
}
