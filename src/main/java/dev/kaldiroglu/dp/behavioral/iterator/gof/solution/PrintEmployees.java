package dev.kaldiroglu.dp.behavioral.iterator.gof.solution;

import dev.kaldiroglu.dp.behavioral.iterator.gof.Employee;

import java.util.ArrayList;

/**
 * GoF's {@code PrintEmployees}: a client written against the {@link Iterator} interface.
 * <p>
 * It works with a {@link ListIterator}, a {@link ReverseListIterator} or a
 * {@link ChainListIterator}, and does not know which one it has. It returns the printed
 * lines instead of printing them, so a caller can check them.
 */
public final class PrintEmployees {

    public static java.util.List<String> print(Iterator<Employee> employees) {
        java.util.List<String> lines = new ArrayList<>();
        for (employees.first(); !employees.isDone(); employees.next()) {
            lines.add(employees.currentItem().name());
        }
        return lines;
    }

    private PrintEmployees() {
    }
}
