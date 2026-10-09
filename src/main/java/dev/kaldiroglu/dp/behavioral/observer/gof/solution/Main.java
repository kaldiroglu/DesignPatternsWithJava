package dev.kaldiroglu.dp.behavioral.observer.gof.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Two clocks observe a timer. After two ticks the analog clock is closed, and the third
 * tick draws only the digital clock. The timer knows neither clock by class.
 */
public final class Main {

    public static void main(String[] args) {
        ClockTimer timer = new ClockTimer();
        List<String> screen = new ArrayList<>();
        new DigitalClock(timer, screen);
        AnalogClock analog = new AnalogClock(timer, screen);
        System.out.println("Observers: " + timer.observerCount());
        timer.tick();
        timer.tick();
        analog.close();
        timer.tick();
        for (String line : screen) {
            System.out.println(line);
        }
        System.out.println("Observers after the analog clock is closed: " + timer.observerCount());
    }
}
