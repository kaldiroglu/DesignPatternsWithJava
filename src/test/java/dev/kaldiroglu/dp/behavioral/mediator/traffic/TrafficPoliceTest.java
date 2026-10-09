package dev.kaldiroglu.dp.behavioral.mediator.traffic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.mediator.Printed.by;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The traffic police officer, on one thread and without sleeping. Car sleeps for a second when
 * it waits, so this test uses its own small vehicle that only records what it was told.
 */
class TrafficPoliceTest {

    /** A vehicle that records what the officer told it, and leaves the junction only when asked. */
    private static final class Recorder implements Vehicle {
        private final String name;
        private final List<String> told;

        Recorder(String name, List<String> told) {
            this.name = name;
            this.told = told;
        }

        @Override
        public void approach() {
            told.add(name + " approaches");
        }

        @Override
        public void proceed() {
            told.add(name + " proceeds");
        }

        @Override
        public void stopp() {
            told.add(name + " stops");
        }

        @Override
        public void waitForAWhile() {
            told.add(name + " waits");
        }
    }

    @Test
    @DisplayName("one vehicle in the junction at a time: the second waits until the first is done")
    void oneAtATime() {
        List<String> told = new ArrayList<>();
        Junction[] junctions = new Junction[1];
        TrafficPolice[] officer = new TrafficPolice[1];
        assertEquals(List.of("Junction Main Square created.", "TrafficPolice Ali created."), by(() -> {
            junctions[0] = new Junction("Main Square");
            officer[0] = new TrafficPolice("Ali", junctions[0]);
        }));
        Junction junction = junctions[0];
        Vehicle first = new Recorder("first", told);
        Vehicle second = new Recorder("second", told);

        officer[0].receive(first);
        officer[0].receive(second);
        officer[0].askPermitToPass(first);
        assertTrue(junction.isBusy());
        officer[0].askPermitToPass(second);
        officer[0].done(first);
        assertFalse(junction.isBusy());
        officer[0].askPermitToPass(second);

        assertEquals(List.of(
                "first stops",
                "second stops",
                "first proceeds",
                "second waits",
                "second proceeds"), told);
    }
}
