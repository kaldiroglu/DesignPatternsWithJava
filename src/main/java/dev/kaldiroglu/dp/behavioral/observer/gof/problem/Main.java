package dev.kaldiroglu.dp.behavioral.observer.gof.problem;

/**
 * Two ticks of a timer that draws its own two clocks. A third clock, or closing one,
 * would be an edit to the timer.
 */
public final class Main {

    public static void main(String[] args) {
        ClockTimer timer = new ClockTimer();
        timer.tick();
        timer.tick();
        for (String line : timer.screen()) {
            System.out.println(line);
        }
    }
}
