package dev.kaldiroglu.dp.behavioral.iterator.orgchart.problem;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

/**
 * Stage three: <b>the department walks itself and calls the caller back.</b>
 * <p>
 * The best of the three. Nothing is copied. The department hides how it stores people. And
 * it offers two orders: department by department ({@link #forEachMember}), and level by
 * level ({@link #forEachMemberByLevel}), which the phone book wants.
 * <p>
 * What it cannot do: the department decides when the walk starts, how fast it goes and
 * when it ends. The caller only receives one person at a time. So a caller cannot walk two
 * departments side by side. After a reorganization, HR asks "what changed?", and
 * {@link ChangeReport} has to copy both departments into lists to answer. The copy from
 * stage two is back.
 * <p>
 * GoF implementation issue 1 (who controls the iteration?) says the same: comparing two
 * collections is easy with an external iterator and "practically impossible" with an
 * internal one.
 */
public final class CallbackDepartment {

    private final String name;
    private final List<Employee> members = new ArrayList<>();
    private final List<CallbackDepartment> units = new ArrayList<>();

    public CallbackDepartment(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public CallbackDepartment add(Employee employee) {
        members.add(employee);
        return this;
    }

    public CallbackDepartment add(CallbackDepartment unit) {
        units.add(unit);
        return this;
    }

    /** Members first, then each sub-department in turn. */
    public void forEachMember(Consumer<Employee> action) {
        members.forEach(action);
        for (CallbackDepartment unit : units) {
            unit.forEachMember(action);
        }
    }

    /** The top level first, then the level below it, and so on. */
    public void forEachMemberByLevel(Consumer<Employee> action) {
        Deque<CallbackDepartment> waiting = new ArrayDeque<>();
        waiting.add(this);
        while (!waiting.isEmpty()) {
            CallbackDepartment department = waiting.poll();
            department.members.forEach(action);
            waiting.addAll(department.units);
        }
    }
}
