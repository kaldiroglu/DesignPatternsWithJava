package dev.kaldiroglu.dp.behavioral.state.door;

import dev.kaldiroglu.dp.behavioral.state.Printed;
import dev.kaldiroglu.dp.behavioral.state.door.pattern2.DoorStateManager;
import dev.kaldiroglu.dp.behavioral.state.door.pattern2.Request;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The door: no pattern (problem), the states decide the next state (pattern1), or a manager
 * decides it from the state and the request (pattern2). The Part 3 slides quote these checks.
 */
class DoorTest {

    private static final List<String> TEST_OUTPUT = List.of(
            "Initial: false",
            "Door is already closed.",
            "After close(): false",
            "After open(): true",
            "Door is already open.",
            "After open(): true",
            "After close(): false",
            "After open(): true",
            "After close(): false",
            "Door is already closed.",
            "After close(): false");

    private static List<String> methodNames(Class<?> type) {
        return Arrays.stream(type.getDeclaredMethods()).filter(m -> !m.isSynthetic())
                .map(Method::getName).sorted().toList();
    }

    @Test
    @DisplayName("the three versions print the same story, starting from a closed door")
    void allVersionsBehaveTheSame() {
        assertEquals(TEST_OUTPUT, Printed.by(() ->
                dev.kaldiroglu.dp.behavioral.state.door.problem.Main.main(new String[0])));
        assertEquals(TEST_OUTPUT, Printed.by(() ->
                dev.kaldiroglu.dp.behavioral.state.door.pattern1.Main.main(new String[0])));
        assertEquals(TEST_OUTPUT, Printed.by(() ->
                dev.kaldiroglu.dp.behavioral.state.door.pattern2.Main.main(new String[0])));
    }

    @Test
    @DisplayName("in pattern1 the closed state knows the open state and changes the door itself")
    void theStatesDecide() {
        var door = new dev.kaldiroglu.dp.behavioral.state.door.pattern1.Door();
        assertFalse(door.isOpen());
        door.open();
        assertTrue(door.isOpen());
        assertEquals(List.of("Door is already open."), Printed.by(door::open));
        door.close();
        assertFalse(door.isOpen());

        List<Class<?>> fieldTypes = Arrays.stream(
                        dev.kaldiroglu.dp.behavioral.state.door.pattern1.AbstractDoorState.class
                                .getDeclaredFields())
                .<Class<?>>map(Field::getType).toList();
        assertTrue(fieldTypes.contains(dev.kaldiroglu.dp.behavioral.state.door.pattern1.DoorState.class),
                "a pattern1 state holds the other states");
    }

    @Test
    @DisplayName("pattern1: the state interface has only the requests, and no state stores a boolean")
    void pattern1StatesAreClean() {
        assertEquals(List.of("close", "isOpen", "open"),
                methodNames(dev.kaldiroglu.dp.behavioral.state.door.pattern1.DoorState.class));
        for (Class<?> state : List.of(
                dev.kaldiroglu.dp.behavioral.state.door.pattern1.AbstractDoorState.class,
                dev.kaldiroglu.dp.behavioral.state.door.pattern1.OpenDoor.class,
                dev.kaldiroglu.dp.behavioral.state.door.pattern1.ClosedDoor.class)) {
            assertTrue(Arrays.stream(state.getDeclaredFields()).noneMatch(f -> f.getType() == boolean.class),
                    state.getSimpleName());
        }
    }

    @Test
    @DisplayName("pattern1 makes two state objects for every door")
    void twoStateObjectsPerDoor() {
        long stateFields = Arrays.stream(
                        dev.kaldiroglu.dp.behavioral.state.door.pattern1.Door.class.getDeclaredFields())
                .filter(f -> dev.kaldiroglu.dp.behavioral.state.door.pattern1.DoorState.class
                        .isAssignableFrom(f.getType()))
                .filter(f -> !f.getName().equals("state"))
                .count();
        assertEquals(2, stateFields);
    }

    @Test
    @DisplayName("pattern2: the manager decides the next state from the state and the request")
    void theManagerDecides() {
        DoorStateManager manager = new DoorStateManager();
        var closed = manager.initial();
        var open = manager.next(closed, Request.OPEN);
        assertTrue(open.isOpen());
        assertSame(closed, manager.next(open, Request.CLOSE));
        assertSame(closed, manager.next(closed, Request.CLOSE), "a request with no line keeps the state");
        assertSame(open, manager.next(open, Request.OPEN));
    }

    @Test
    @DisplayName("pattern2: the states hold nothing, so they know no other state and cannot change the door")
    void pattern2StatesHoldNothing() {
        for (Class<?> state : List.of(
                dev.kaldiroglu.dp.behavioral.state.door.pattern2.OpenDoor.class,
                dev.kaldiroglu.dp.behavioral.state.door.pattern2.ClosedDoor.class)) {
            assertEquals(0, state.getDeclaredFields().length, state.getSimpleName());
        }
        assertEquals(List.of("close", "isOpen", "open"),
                methodNames(dev.kaldiroglu.dp.behavioral.state.door.pattern2.DoorState.class));
    }

    @Test
    @DisplayName("pattern2: the door asks the manager after each request")
    void theDoorAsksTheManager() {
        var door = new dev.kaldiroglu.dp.behavioral.state.door.pattern2.Door();
        assertFalse(door.isOpen());
        assertEquals(List.of("Door is already closed."), Printed.by(door::close));
        assertFalse(door.isOpen());
        door.open();
        assertTrue(door.isOpen());
        assertEquals(List.of("Door is already open."), Printed.by(door::open));
        door.close();
        assertFalse(door.isOpen());
    }

    @Test
    @DisplayName("the version without the pattern: one boolean, two states, one if in each method")
    void theProblemVersion() {
        String code = Printed.codeOf("src/main/java/dev/kaldiroglu/dp/behavioral/state/door/problem/Door.java");
        assertEquals(2, code.split("\\bif \\(", -1).length - 1, "open and close each have one if");
        assertEquals(1, Arrays.stream(dev.kaldiroglu.dp.behavioral.state.door.problem.Door.class
                .getDeclaredFields()).filter(f -> f.getType() == boolean.class).count());

        var door = new dev.kaldiroglu.dp.behavioral.state.door.problem.Door(false);
        assertEquals(List.of("Door is already closed."), Printed.by(door::close));
        door.open();
        assertTrue(door.isOpen());
        assertEquals(List.of("Door is already open."), Printed.by(door::open));
    }
}
