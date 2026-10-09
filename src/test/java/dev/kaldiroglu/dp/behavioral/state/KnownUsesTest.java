package dev.kaldiroglu.dp.behavioral.state;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

/** The Java rows of the State deck's known-uses table, checked against the running JDK. */
class KnownUsesTest {

    private static List<String> names(Enum<?>[] constants) {
        return Arrays.stream(constants).map(Enum::name).toList();
    }

    @Test
    @DisplayName("Thread.State is an enum of six names, starting with NEW and RUNNABLE")
    void threadState() {
        List<String> names = names(Thread.State.values());
        assertEquals(6, names.size());
        assertEquals(List.of("NEW", "RUNNABLE"), names.subList(0, 2));
    }

    @Test
    @DisplayName("Future.State is an enum starting with RUNNING and SUCCESS")
    void futureState() {
        assertEquals(List.of("RUNNING", "SUCCESS"), names(Future.State.values()).subList(0, 2));
    }

    @Test
    @DisplayName("FutureTask keeps its state in a private int field")
    void futureTask() throws Exception {
        var state = java.util.concurrent.FutureTask.class.getDeclaredField("state");
        assertEquals(int.class, state.getType());
        assertTrue(java.lang.reflect.Modifier.isPrivate(state.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isVolatile(state.getModifiers()));
    }
}
