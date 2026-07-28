package dev.kaldiroglu.dp.structural.composite.hw.orgchart;

import java.util.Objects;

/** A Leaf: someone with no reports. */
public final class IndividualContributor implements Employee {

    private final String name;
    private final String role;
    private final long salary;

    public IndividualContributor(String name, String role, long salary) {
        this.name = Objects.requireNonNull(name);
        this.role = Objects.requireNonNull(role);
        this.salary = salary;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long totalCost() {
        return salary;
    }

    @Override
    public int headcount() {
        return 1;
    }

    @Override
    public String render(String indent) {
        return indent + name + " — " + role + " (" + salary + ")";
    }
}
