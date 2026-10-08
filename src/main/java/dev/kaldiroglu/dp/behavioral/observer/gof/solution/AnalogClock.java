package dev.kaldiroglu.dp.behavioral.observer.gof.solution;

import java.util.List;

/** A <b>ConcreteObserver</b>: GoF's {@code AnalogClock}. Same timer, different drawing. */
public final class AnalogClock implements Observer {

    private final ClockTimer timer;
    private final List<String> screen;

    public AnalogClock(ClockTimer timer, List<String> screen) {
        this.timer = timer;
        this.screen = screen;
        timer.attach(this);
    }

    @Override
    public void update(Subject changed) {
        if (changed == timer) {
            screen.add("analog second hand at " + timer.second() * 6 + " degrees");
        }
    }

    public void close() {
        timer.detach(this);
    }
}
