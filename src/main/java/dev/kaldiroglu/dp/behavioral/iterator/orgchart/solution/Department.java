package dev.kaldiroglu.dp.behavioral.iterator.orgchart.solution;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The <b>Aggregate</b>: a department that gives out iterators instead of its lists.
 * <p>
 * Compare the three classes in {@code problem}. This one has no getter for its members or
 * its sub-departments, it never copies them, and it never calls the caller back. It creates
 * an iterator, and the caller decides when to ask for the next person and when to stop.
 * <p>
 * Because it implements {@link Iterable}, a caller can write
 * {@code for (Employee e : department)}. That loop gets its iterator from {@link #iterator()}.
 * A second order is one more method that returns an {@code Iterable}: {@link #byLevel()}.
 */
public final class Department implements Iterable<Employee> {

    private final String name;
    private final List<Employee> members = new ArrayList<>();
    private final List<Department> units = new ArrayList<>();

    public Department(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public Department add(Employee employee) {
        members.add(employee);
        return this;
    }

    public Department add(Department unit) {
        units.add(unit);
        return this;
    }

    /** Members first, then each sub-department in turn: the order of the org chart. */
    @Override
    public Iterator<Employee> iterator() {
        return new DepthFirstIterator(this);
    }

    /** The top level first, then the level below it: the order of the phone book. */
    public Iterable<Employee> byLevel() {
        return () -> new LevelOrderIterator(this);
    }

    // Package-private: only the iterators in this package may see the structure.
    List<Employee> members() {
        return members;
    }

    List<Department> units() {
        return units;
    }
}
