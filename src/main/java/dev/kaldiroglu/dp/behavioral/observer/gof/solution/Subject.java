package dev.kaldiroglu.dp.behavioral.observer.gof.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * The <b>Subject</b>: GoF's {@code Subject}. It keeps a list of observers and tells them
 * all when something changes.
 * <p>
 * It knows them only as {@link Observer}s. That is the whole decoupling: a subject and its
 * observers can be in different layers, and each can change without the other.
 */
public abstract class Subject {

    private final List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    /** Called by the subject itself after its state has changed. */
    protected void notifyObservers() {
        for (Observer observer : List.copyOf(observers)) {
            observer.update(this);
        }
    }

    public int observerCount() {
        return observers.size();
    }
}
