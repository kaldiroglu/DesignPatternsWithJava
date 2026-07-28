package dev.kaldiroglu.dp.structural.composite.hw.orgchart;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The Composite: someone with reports, who is also somebody's report.
 * <p>
 * A manager <strong>counts their own salary</strong> as well as their reports'. That is a
 * decision, not an accident, and it is the first thing to settle: if a manager did not count
 * themselves, the cost of the company would not include the chief executive.
 */
public final class Manager implements Employee {

    private final String name;
    private final String role;
    private final long salary;
    private final List<Employee> reports = new ArrayList<>();

    public Manager(String name, String role, long salary) {
        this.name = Objects.requireNonNull(name);
        this.role = Objects.requireNonNull(role);
        this.salary = salary;
    }

    public Manager add(Employee report) {
        if (report == this) {
            throw new IllegalArgumentException("nobody reports to themselves");
        }
        if (reachesFrom(report, this)) {
            throw new IllegalArgumentException(
                    "that would make a cycle: " + report.getName() + " already manages " + name);
        }
        reports.add(report);
        return this;
    }

    public List<Employee> getReports() {
        return List.copyOf(reports);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long totalCost() {
        return salary + reports.stream().mapToLong(Employee::totalCost).sum();
    }

    @Override
    public int headcount() {
        return 1 + reports.stream().mapToInt(Employee::headcount).sum();
    }

    @Override
    public String render(String indent) {
        StringBuilder out = new StringBuilder(indent + name + " — " + role + " (" + salary + ")");
        for (Employee report : reports) {
            out.append(System.lineSeparator()).append(report.render(indent + "    "));
        }
        return out.toString();
    }

    /**
     * Counts how many distinct people are below this manager.
     * <p>
     * This is the honest answer to the second half of the exercise. {@link #headcount()} adds
     * up the tree, so anyone reachable by two routes is counted twice. Identity-based
     * de-duplication gives the true number — and the gap between the two is the exercise.
     */
    public int distinctHeadcount() {
        Map<Employee, Boolean> seen = new IdentityHashMap<>();
        collect(this, seen);
        return seen.size();
    }

    private static void collect(Employee employee, Map<Employee, Boolean> seen) {
        if (seen.put(employee, Boolean.TRUE) != null) {
            return;
        }
        if (employee instanceof Manager manager) {
            manager.reports.forEach(report -> collect(report, seen));
        }
    }

    private static boolean reachesFrom(Employee start, Employee target) {
        if (start == target) {
            return true;
        }
        return start instanceof Manager manager
                && manager.reports.stream().anyMatch(r -> reachesFrom(r, target));
    }
}
