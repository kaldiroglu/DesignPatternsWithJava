package dev.kaldiroglu.dp.behavioral.observer.gof.problem;

import java.util.ArrayList;
import java.util.List;

/**
 * Before the pattern: a timer that draws its own clocks.
 * <p>
 * GoF's sample code has a timer and two clocks that show its time. Here the timer
 * holds both clocks and draws them on every tick. A third clock is an edit to the timer, and
 * a clock cannot be closed while the timer runs.
 */
public final class ClockTimer {

    private int seconds;
    private final List<String> screen = new ArrayList<>();

    public void tick() {
        seconds++;
        drawDigital();
        drawAnalog();
    }

    private void drawDigital() {
        screen.add(String.format("digital %02d:%02d:%02d", seconds / 3600, seconds / 60 % 60, seconds % 60));
    }

    private void drawAnalog() {
        screen.add("analog second hand at " + (seconds % 60) * 6 + " degrees");
    }

    public List<String> screen() {
        return List.copyOf(screen);
    }
}
