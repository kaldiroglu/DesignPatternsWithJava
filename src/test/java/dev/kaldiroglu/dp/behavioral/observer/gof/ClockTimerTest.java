package dev.kaldiroglu.dp.behavioral.observer.gof;

import dev.kaldiroglu.dp.behavioral.observer.Printed;
import dev.kaldiroglu.dp.behavioral.observer.gof.solution.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** GoF's clock timer (Design Patterns, pp. 293-303), before and after the pattern. */
class ClockTimerTest {

    @Test
    @DisplayName("before the pattern, the timer draws both of its clocks on every tick")
    void theTimerDrawsItsClocks() {
        var timer = new dev.kaldiroglu.dp.behavioral.observer.gof.problem.ClockTimer();
        timer.tick();
        timer.tick();

        assertEquals(List.of("digital 00:00:01", "analog second hand at 6 degrees",
                "digital 00:00:02", "analog second hand at 12 degrees"), timer.screen());
    }

    @Test
    @DisplayName("with the pattern, the clocks draw the same screen")
    void theClocksDrawTheSameScreen() {
        var before = new dev.kaldiroglu.dp.behavioral.observer.gof.problem.ClockTimer();
        ClockTimer timer = new ClockTimer();
        List<String> screen = new ArrayList<>();
        new DigitalClock(timer, screen);
        new AnalogClock(timer, screen);

        for (int i = 0; i < 3; i++) {
            before.tick();
            timer.tick();
        }

        assertEquals(before.screen(), screen);
    }

    @Test
    @DisplayName("after the analog clock is closed, on the third tick only the digital clock draws")
    void closingAClockDetachesIt() {
        ClockTimer timer = new ClockTimer();
        List<String> screen = new ArrayList<>();
        new DigitalClock(timer, screen);
        AnalogClock analog = new AnalogClock(timer, screen);
        assertEquals(2, timer.observerCount());

        timer.tick();
        timer.tick();
        analog.close();
        timer.tick();

        assertEquals(1, timer.observerCount());
        assertEquals("digital 00:00:03", screen.getLast());
        assertEquals(5, screen.size());
    }

    @Test
    @DisplayName("the clocks pull the time: the timer passes only itself")
    void thePullModel() throws Exception {
        assertEquals(List.of(Subject.class),
                List.of(Observer.class.getMethod("update", Subject.class).getParameterTypes()));

        ClockTimer timer = new ClockTimer();
        for (int i = 0; i < 3725; i++) {
            timer.tick();
        }
        assertEquals(1, timer.hour());
        assertEquals(2, timer.minute());
        assertEquals(5, timer.second());
    }

    @Test
    @DisplayName("the timer knows nothing about clocks")
    void theTimerKnowsNoClock() {
        for (Class<?> type = ClockTimer.class; type != Object.class; type = type.getSuperclass()) {
            List<Class<?>> fieldTypes = Arrays.stream(type.getDeclaredFields())
                    .<Class<?>>map(Field::getType).toList();
            assertFalse(fieldTypes.contains(DigitalClock.class));
            assertFalse(fieldTypes.contains(AnalogClock.class));
        }
    }

    @Test
    @DisplayName("problem.Main prints two ticks of both clocks, drawn by the timer")
    void problemMainOutput() {
        assertEquals(List.of("digital 00:00:01", "analog second hand at 6 degrees",
                        "digital 00:00:02", "analog second hand at 12 degrees"),
                Printed.by(() -> dev.kaldiroglu.dp.behavioral.observer.gof.problem.Main.main(new String[0])));
    }

    @Test
    @DisplayName("solution.Main prints two observers, three ticks with the analog clock closed after two, and one observer left")
    void solutionMainOutput() {
        assertEquals(List.of("Observers: 2",
                        "digital 00:00:01", "analog second hand at 6 degrees",
                        "digital 00:00:02", "analog second hand at 12 degrees",
                        "digital 00:00:03",
                        "Observers after the analog clock is closed: 1"),
                Printed.by(() -> Main.main(new String[0])));
    }
}
