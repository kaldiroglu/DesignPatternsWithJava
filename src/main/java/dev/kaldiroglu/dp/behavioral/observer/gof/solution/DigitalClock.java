package dev.kaldiroglu.dp.behavioral.observer.gof.solution;

import java.util.List;

/**
 * A <b>ConcreteObserver</b>: GoF's {@code DigitalClock}. It attaches itself when it is made
 * and detaches when it is closed, so the timer never holds a closed clock. GoF
 * implementation issue 4 (dangling references to deleted subjects) is the other side of
 * the same rule.
 */
public final class DigitalClock implements Observer {

    private final ClockTimer timer;
    private final List<String> screen;

    public DigitalClock(ClockTimer timer, List<String> screen) {
        this.timer = timer;
        this.screen = screen;
        timer.attach(this);
    }

    @Override
    public void update(Subject changed) {
        if (changed == timer) {
            screen.add(String.format("digital %02d:%02d:%02d", timer.hour(), timer.minute(), timer.second()));
        }
    }

    public void close() {
        timer.detach(this);
    }
}
