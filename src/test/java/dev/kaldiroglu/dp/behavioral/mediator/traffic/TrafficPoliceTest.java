package dev.kaldiroglu.dp.behavioral.mediator.traffic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static dev.kaldiroglu.dp.behavioral.mediator.Printed.by;
import static dev.kaldiroglu.dp.behavioral.mediator.Printed.codeOf;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The traffic police officer. Most tests run on one thread, with a small vehicle that only
 * records what it was told. The last two run real threads.
 */
class TrafficPoliceTest {

    /** A vehicle that records what the officer told it. */
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
        public void stop() {
            told.add(name + " stops");
        }
    }

    private static TrafficPolice officerOf(Junction junction) {
        TrafficPolice[] officer = new TrafficPolice[1];
        by(() -> officer[0] = new TrafficPolice("Ali", junction));
        return officer[0];
    }

    private static Junction junction() {
        Junction[] junction = new Junction[1];
        by(() -> junction[0] = new Junction("Main Square"));
        return junction[0];
    }

    @Test
    @DisplayName("the officer tells the first waiting car to proceed, and that car does not ask again")
    void theOfficerTellsTheNextCar() {
        List<String> told = new ArrayList<>();
        TrafficPolice officer = officerOf(junction());
        Vehicle first = new Recorder("first", told);
        Vehicle second = new Recorder("second", told);

        officer.askPermitToPass(first);
        officer.askPermitToPass(second);
        officer.done(first);

        assertEquals(List.of("first proceeds", "second stops", "second proceeds"), told);
    }

    @Test
    @DisplayName("cars pass in the order they asked")
    void waitingCarsPassInOrder() {
        List<String> told = new ArrayList<>();
        TrafficPolice officer = officerOf(junction());
        Vehicle first = new Recorder("first", told);
        Vehicle second = new Recorder("second", told);
        Vehicle third = new Recorder("third", told);

        officer.askPermitToPass(first);
        officer.askPermitToPass(second);
        officer.askPermitToPass(third);
        officer.done(first);
        officer.done(second);

        assertEquals(List.of("first proceeds", "second stops", "third stops",
                "second proceeds", "third proceeds"), told);
    }

    @Test
    @DisplayName("the junction stays busy from one car to the next, and is free after the last")
    void theJunctionStaysBusyUntilTheLastCar() {
        List<String> told = new ArrayList<>();
        Junction junction = junction();
        TrafficPolice officer = officerOf(junction);
        Vehicle first = new Recorder("first", told);
        Vehicle second = new Recorder("second", told);
        Vehicle late = new Recorder("late", told);

        officer.askPermitToPass(first);
        officer.askPermitToPass(second);
        officer.done(first);
        assertTrue(junction.isBusy());

        officer.askPermitToPass(late);
        assertEquals("late stops", told.getLast());

        officer.done(second);
        officer.done(late);
        assertFalse(junction.isBusy());
    }

    @Test
    @DisplayName("200 threads: every one passes, and never two in the junction at once")
    void oneAtATimeUnderThreads() throws InterruptedException {
        TrafficPolice officer = officerOf(junction());
        AtomicInteger inJunction = new AtomicInteger();
        AtomicInteger mostAtOnce = new AtomicInteger();
        AtomicInteger passed = new AtomicInteger();

        Thread[] threads = new Thread[200];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                CountDownLatch permit = new CountDownLatch(1);
                Vehicle vehicle = new Vehicle() {
                    @Override
                    public void approach() {
                    }

                    @Override
                    public void proceed() {
                        permit.countDown();
                    }

                    @Override
                    public void stop() {
                    }
                };
                officer.askPermitToPass(vehicle);
                try {
                    permit.await();
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
                mostAtOnce.accumulateAndGet(inJunction.incrementAndGet(), Math::max);
                passed.incrementAndGet();
                inJunction.decrementAndGet();
                officer.done(vehicle);
            });
        }
        for (Thread thread : threads) thread.start();
        for (Thread thread : threads) thread.join();

        assertEquals(200, passed.get());
        assertEquals(1, mostAtOnce.get());
    }

    @Test
    @DisplayName("Main: five cars, each asks once and each passes once")
    void mainOutput() {
        List<String> lines = by(() -> {
            try {
                Main.main(new String[0]);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

        assertEquals(5, lines.stream().filter(line -> line.endsWith("is asking permit to pass the junction.")).count());
        assertEquals(5, lines.stream().filter(line -> line.endsWith("is proceeding through the junction.")).count());
    }

    @Test
    @DisplayName("a colleague knows only the mediator: Car holds no Junction and has no loop")
    void aCarKnowsOnlyTheMediator() {
        String car = codeOf("mediator/traffic/Car.java");

        assertFalse(car.contains("Junction "));
        assertFalse(car.contains("while"));
    }
}
