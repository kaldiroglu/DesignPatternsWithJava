package dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain;

/** A person in the company. A value: two employees with the same name and role are equal. */
public record Employee(String name, String role) {

    @Override
    public String toString() {
        return name + " (" + role + ")";
    }
}
