package dev.kaldiroglu.dp.behavioral.iterator.orgchart.solution;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A <b>ConcreteIterator</b>: members first, then each sub-department in turn.
 * <p>
 * It keeps its own position — a stack of departments still to visit, and an iterator over
 * the current department's members — so two of these can walk the same department at the
 * same time without disturbing each other. It copies nothing: it finds the next person only
 * when {@link #next()} is called.
 * <p>
 * This is GoF implementation issue 7 (iterators for composites): an external iterator over
 * a recursive structure must remember where it is in the tree. Here the stack of departments
 * still to visit does that.
 */
final class DepthFirstIterator implements Iterator<Employee> {

    private final Deque<Department> departments = new ArrayDeque<>();   // a stack: push and pop at the same end
    private Iterator<Employee> members;

    DepthFirstIterator(Department root) {
        departments.push(root);
        members = Collections.emptyIterator();
    }

    @Override
    public boolean hasNext() {
        while (!members.hasNext()) {
            if (departments.isEmpty()) {
                return false;
            }
            Department department = departments.pop();
            // Push in reverse, so sub-departments come back in the order they were added.
            for (int i = department.units().size() - 1; i >= 0; i--) {
                departments.push(department.units().get(i));
            }
            members = department.members().iterator();
        }
        return true;
    }

    @Override
    public Employee next() {
        if (!hasNext()) {
            throw new NoSuchElementException("no more employees");
        }
        return members.next();
    }
}
