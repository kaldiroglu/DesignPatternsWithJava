package dev.kaldiroglu.dp.behavioral.state.door;

import dev.kaldiroglu.dp.behavioral.state.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The door: the states decide the next state (pattern1), or a manager decides (pattern2).
 * The Part 3 slides say both versions behave the same; this class checks it.
 */
class DoorTest {

    private static final List<String> TEST_OUTPUT = List.of(
            "Initial: false",
            "Door is already closed!",
            "After close(): false",
            "After open(): true",
            "Door is already open!",
            "After open(): true",
            "After close(): false",
            "After open(): true",
            "After close(): false",
            "Door is already closed!",
            "After close(): false");

    @Test
    @DisplayName("pattern1 and pattern2 print the same story, apart from the end of the messages")
    void bothVersionsBehaveTheSame() {
        List<String> first = Printed.by(() ->
                dev.kaldiroglu.dp.behavioral.state.door.pattern1.Test.main(new String[0]));
        List<String> second = Printed.by(() ->
                dev.kaldiroglu.dp.behavioral.state.door.pattern2.Test.main(new String[0]));

        assertEquals(TEST_OUTPUT, first);
        assertEquals(TEST_OUTPUT.stream().map(line -> line.replace('!', '.')).toList(), second);
    }

    @Test
    @DisplayName("in pattern1 the closed state knows the open state and changes the door itself")
    void theStatesDecide() {
        var door = new dev.kaldiroglu.dp.behavioral.state.door.pattern1.Door();
        assertFalse(door.isOpen());
        door.open();
        assertTrue(door.isOpen());
        assertEquals(List.of("Door is already open!"), Printed.by(door::open));
        door.close();
        assertFalse(door.isOpen());
    }

    @Test
    @DisplayName("in pattern2 the states only ask the manager, and the manager knows both states")
    void theManagerDecides() {
        var door = new dev.kaldiroglu.dp.behavioral.state.door.pattern2.Door();
        assertFalse(door.isOpen());
        door.open();
        assertTrue(door.isOpen());
        door.close();
        assertFalse(door.isOpen());

        List<String> managerFields = Arrays.stream(
                        dev.kaldiroglu.dp.behavioral.state.door.pattern2.DoorStateManager.class
                                .getDeclaredFields())
                .map(Field::getName).toList();
        assertTrue(managerFields.containsAll(List.of("openState", "closedState")));

        List<Class<?>> stateFieldTypes = Arrays.stream(
                        dev.kaldiroglu.dp.behavioral.state.door.pattern2.AbstractDoor.class
                                .getDeclaredFields())
                .<Class<?>>map(Field::getType).toList();
        assertFalse(stateFieldTypes.contains(
                dev.kaldiroglu.dp.behavioral.state.door.pattern2.DoorState.class),
                "a pattern2 state does not hold another state");
    }

    @Test
    @DisplayName("pattern1 makes two state objects for every door")
    void twoStateObjectsPerDoor() {
        long stateFields = Arrays.stream(
                        dev.kaldiroglu.dp.behavioral.state.door.pattern1.Door.class.getDeclaredFields())
                .filter(f -> f.getType() == dev.kaldiroglu.dp.behavioral.state.door.pattern1.DoorState.class)
                .filter(f -> !f.getName().equals("state"))
                .count();
        assertEquals(2, stateFields);
    }

    @Test
    @DisplayName("the version without the pattern behaves the same with a boolean and an if")
    void theProblemVersion() {
        var door = new dev.kaldiroglu.dp.behavioral.state.door.problem.Door(false);
        assertEquals(List.of("Door is already closed."), Printed.by(door::close));
        door.open();
        assertTrue(door.isOpen());
        assertEquals(List.of("Door is already open."), Printed.by(door::open));
    }
}
