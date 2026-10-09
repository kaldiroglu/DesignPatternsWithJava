package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.maintenance;

/**
 * Homework 1: the <b>Handler</b>. A developer takes the requests it is suited for and
 * passes the rest to the next developer in line.
 */
public abstract class Developer {

    private final String name;
    private Developer next;

    protected Developer(String name) {
        this.name = name;
    }

    public Developer then(Developer next) {
        this.next = next;
        return next;
    }

    public final String take(Request request) {
        if (suits(request)) {
            return request.title() + " -> " + name;
        }
        if (next == null) {
            return request.title() + " -> nobody: back to the team lead";
        }
        return next.take(request);
    }

    protected abstract boolean suits(Request request);
}
