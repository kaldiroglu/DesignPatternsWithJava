package dev.kaldiroglu.dp.behavioral.iterator.gof.solution;

import dev.kaldiroglu.dp.behavioral.iterator.gof.Employee;

import java.util.ArrayList;

/** GoF's {@code PrintNEmployees}: an internal iterator that stops after the first n. */
public final class PrintNEmployees extends ListTraverser<Employee> {

    private final int total;
    private final java.util.List<String> lines = new ArrayList<>();

    public PrintNEmployees(AbstractList<Employee> list, int total) {
        super(list);
        this.total = total;
    }

    @Override
    protected boolean processItem(Employee employee) {
        lines.add(employee.name());
        return lines.size() < total;
    }

    public java.util.List<String> lines() {
        return java.util.List.copyOf(lines);
    }
}
