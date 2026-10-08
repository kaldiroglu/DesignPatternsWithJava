package dev.kaldiroglu.dp.behavioral.observer.gof.solution;

/**
 * A <b>ConcreteSubject</b>: GoF's {@code ClockTimer}. It keeps the time and notifies its
 * observers on every tick. It knows nothing about clocks.
 * <p>
 * The observers pull what they need — hour, minute, second — after they are told something
 * changed. This is the pull model, from GoF implementation issue 6 (avoiding
 * observer-specific update protocols: the push and pull models).
 */
public final class ClockTimer extends Subject {

    private int seconds;

    public void tick() {
        seconds++;
        notifyObservers();
    }

    public int hour() {
        return seconds / 3600;
    }

    public int minute() {
        return seconds / 60 % 60;
    }

    public int second() {
        return seconds % 60;
    }
}
