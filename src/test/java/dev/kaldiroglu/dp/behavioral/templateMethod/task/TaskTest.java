package dev.kaldiroglu.dp.behavioral.templateMethod.task;

import dev.kaldiroglu.dp.behavioral.command.lender.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The repeated task. The Part 3 notes say the client runs a print task ten times, one
 * second apart, and that the wait happens only between repetitions.
 */
class TaskTest {

    private static long count(List<String> lines, String line) {
        return lines.stream().filter(line::equals).count();
    }

    @Test
    @DisplayName("the client runs a print task ten times, one second apart: nine waits")
    void theClientPrintsTenTimesWithNineWaits() {
        long start = System.nanoTime();
        List<String> lines = Printed.by(
                () -> dev.kaldiroglu.dp.behavioral.templateMethod.task.Test.main(new String[0]));
        long millis = (System.nanoTime() - start) / 1_000_000;

        assertEquals(10, count(lines, "Printing task."));
        assertEquals(10, count(lines, "- in before() -"));
        assertEquals(10, count(lines, "- in after() -"));
        assertEquals("*** Preparing printing! ***", lines.getFirst());
        assertEquals("*** Cleaning printing environment. ***", lines.getLast());
        assertTrue(millis >= 9_000, "nine waits of one second, took " + millis + " ms");
        assertTrue(millis < 10_000, "no wait after the last repetition, took " + millis + " ms");
    }

    @Test
    @DisplayName("the steps run in the order the template method fixes: prepare, then before, task, after, then clean")
    void theOrderOfTheSteps() {
        List<String> lines = Printed.by(() -> new Scan("Scanning", 0, 2).run()).stream()
                .filter(line -> !line.isBlank())
                .toList();

        assertEquals(List.of(
                "*** in prepare() ***",
                "- in before() -", "I'm scanning!", "- in after() -",
                "- in before() -", "I'm scanning!", "- in after() -",
                "*** in clean() ***"), lines);
    }

    @Test
    @DisplayName("an interrupt during the wait keeps the interrupt flag, stops repeating, and still cleans up")
    void anInterruptStopsTheLoopAndCleansUp() {
        Thread.currentThread().interrupt();
        List<String> lines;
        boolean stillInterrupted;
        try {
            lines = Printed.by(() -> new Fax("Faxing", 1, 3).run());
        } finally {
            stillInterrupted = Thread.interrupted();   // also clears the flag for later tests
        }

        assertTrue(stillInterrupted);
        assertEquals(1, count(lines, "I'm faxing!"));
        assertEquals("*** in clean() ***", lines.getLast());
    }

    @Test
    @DisplayName("as the exercise says: if doTask throws an exception, clean is never called")
    void aFailingTaskSkipsClean() {
        Task failing = new Task("Failing", 0, 3) {
            @Override
            public void doTask() {
                throw new IllegalStateException("the device is off");
            }
        };

        List<String> lines = Printed.by(() ->
                assertThrows(IllegalStateException.class, failing::run));

        assertTrue(lines.contains("*** in prepare() ***"));
        assertFalse(lines.contains("*** in clean() ***"));
    }

    @Test
    @DisplayName("run is final, doTask is the only abstract step, and the four hooks are public")
    void theKindsOfMethod() throws Exception {
        assertTrue(Modifier.isFinal(Task.class.getDeclaredMethod("run").getModifiers()));

        List<String> abstractSteps = java.util.Arrays.stream(Task.class.getDeclaredMethods())
                .filter(m -> Modifier.isAbstract(m.getModifiers()))
                .map(Method::getName)
                .toList();
        assertEquals(List.of("doTask"), abstractSteps);

        for (String hook : List.of("prepare", "before", "after", "clean")) {
            Method method = Task.class.getDeclaredMethod(hook);
            assertTrue(Modifier.isPublic(method.getModifiers()), hook);
            assertFalse(Modifier.isAbstract(method.getModifiers()), hook);
        }
    }

    @Test
    @DisplayName("Print overrides prepare and clean; Fax and Scan write only doTask")
    void whichHooksEachTaskOverrides() {
        assertEquals(java.util.Set.of("prepare", "clean", "doTask"), declaredNames(Print.class));
        assertEquals(java.util.Set.of("doTask"), declaredNames(Fax.class));
        assertEquals(java.util.Set.of("doTask"), declaredNames(Scan.class));
    }

    private static java.util.Set<String> declaredNames(Class<?> type) {
        return java.util.Arrays.stream(type.getDeclaredMethods())
                .filter(m -> !m.isSynthetic())
                .map(Method::getName)
                .collect(java.util.stream.Collectors.toSet());
    }
}
