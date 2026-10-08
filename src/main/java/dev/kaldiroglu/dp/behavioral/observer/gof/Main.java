package dev.kaldiroglu.dp.behavioral.observer.gof;

import dev.kaldiroglu.dp.behavioral.observer.gof.solution.AnalogClock;
import dev.kaldiroglu.dp.behavioral.observer.gof.solution.ClockTimer;
import dev.kaldiroglu.dp.behavioral.observer.gof.solution.DigitalClock;

import java.util.ArrayList;
import java.util.List;

/** Two ticks with both clocks, then the analog clock is closed and the timer ticks once more. */
public final class Main {

    public static void main(String[] args) {
        var before = new dev.kaldiroglu.dp.behavioral.observer.gof.problem.ClockTimer();
        before.tick();
        before.tick();
        System.out.println("Timer draws its clocks: " + before.screen());

        ClockTimer timer = new ClockTimer();
        List<String> screen = new ArrayList<>();
        new DigitalClock(timer, screen);
        AnalogClock analog = new AnalogClock(timer, screen);
        timer.tick();
        timer.tick();
        analog.close();
        timer.tick();
        System.out.println("Clocks observe the timer: " + screen);
        System.out.println("Observers left: " + timer.observerCount());
    }
}
