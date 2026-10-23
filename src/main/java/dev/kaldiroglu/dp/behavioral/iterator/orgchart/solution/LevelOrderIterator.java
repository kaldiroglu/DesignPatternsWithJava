package dev.kaldiroglu.dp.behavioral.iterator.orgchart.solution;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * A second <b>ConcreteIterator</b> over the same department: level by level.
 * <p>
 * The only difference from {@link DepthFirstIterator} is a queue instead of a stack. The
 * department did not change at all to get a second order of walking.
 */
final class LevelOrderIterator implements Iterator<Employee> {

    private final Queue<Department> waiting = new ArrayDeque<>();   // add at the end, poll from the front
    private Iterator<Employee> members = Collections.emptyIterator();

    LevelOrderIterator(Department root) {
        waiting.add(root);
    }

    @Override
    public boolean hasNext() {
        while (!members.hasNext()) {
            if (waiting.isEmpty()) {
                return false;
            }
            Department department = waiting.poll();
            waiting.addAll(department.units());
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
