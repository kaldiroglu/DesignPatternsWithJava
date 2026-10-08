package dev.kaldiroglu.dp.behavioral.iterator.orgchart.solution;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

import java.util.Iterator;
import java.util.Optional;

/**
 * "What changed after the reorganization?" — answered with two iterators.
 * <p>
 * Compare {@code problem.ChangeReport}, which had to copy both departments into lists. Here
 * the report holds one iterator for each chart and moves them forward together. It stops at
 * the first difference, so it never reads the rest of either chart.
 */
public final class ChangeReport {

    public Optional<String> firstDifference(Department before, Department after) {
        Iterator<Employee> old = before.iterator();
        Iterator<Employee> current = after.iterator();

        while (old.hasNext() && current.hasNext()) {
            Employee was = old.next();
            Employee is = current.next();
            if (!was.equals(is)) {
                return Optional.of(was + " -> " + is);
            }
        }
        if (old.hasNext() || current.hasNext()) {
            return Optional.of("the charts have different sizes");
        }
        return Optional.empty();
    }
}
