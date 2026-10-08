package dev.kaldiroglu.dp.behavioral.iterator.gof;

/** GoF's sample code walks a list of employees and prints each one. */
public record Employee(String name) {

    @Override
    public String toString() {
        return name;
    }
}
