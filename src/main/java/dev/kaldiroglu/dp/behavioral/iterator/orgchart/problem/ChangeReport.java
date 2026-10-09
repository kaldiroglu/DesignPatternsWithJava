package dev.kaldiroglu.dp.behavioral.iterator.orgchart.problem;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * "What changed after the reorganization?" — answered with stage three.
 * <p>
 * The report must walk the old chart and the new chart side by side and stop at the first
 * place they differ. A callback cannot do that: each department runs its own walk to the
 * end. So the report copies both departments into lists first, and then compares the lists.
 * This is the copy that stage three was built to avoid.
 */
public final class ChangeReport {

    public Optional<String> firstDifference(CallbackDepartment before, CallbackDepartment after) {
        List<Employee> old = new ArrayList<>();
        before.forEachMember(old::add);          // copy the whole old chart
        List<Employee> current = new ArrayList<>();
        after.forEachMember(current::add);       // copy the whole new chart

        int shared = Math.min(old.size(), current.size());
        for (int i = 0; i < shared; i++) {
            if (!old.get(i).equals(current.get(i))) {
                return Optional.of(old.get(i) + " -> " + current.get(i));
            }
        }
        if (old.size() != current.size()) {
            return Optional.of("the charts have different sizes");
        }
        return Optional.empty();
    }

    /** Every difference. Again both charts are copied whole before anything is compared. */
    public List<String> allDifferences(CallbackDepartment before, CallbackDepartment after) {
        List<Employee> old = new ArrayList<>();
        before.forEachMember(old::add);
        List<Employee> current = new ArrayList<>();
        after.forEachMember(current::add);

        List<String> changes = new ArrayList<>();
        int shared = Math.min(old.size(), current.size());
        for (int i = 0; i < shared; i++) {
            if (!old.get(i).equals(current.get(i))) {
                changes.add(old.get(i) + " -> " + current.get(i));
            }
        }
        if (old.size() != current.size()) {
            changes.add("the charts have different sizes");
        }
        return changes;
    }
}
