package dev.kaldiroglu.dp.behavioral.state.elevator;

import dev.kaldiroglu.dp.behavioral.state.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The elevator: the caller changes the state, and the states only print. The State deck
 * lists this example as the caller-driven one, closer to Strategy.
 */
class ElevatorTest {

    @Test
    @DisplayName("Main prints one trip up and one trip down")
    void mainPrintsBothTrips() {
        assertEquals(List.of(
                "Door is open.",
                "Starting to go up.",
                "Please first stop the elevator.",
                "Please first stop the elevator.",
                "Stopping!",
                "Door is open.",
                "Starting to go down.",
                "Already going down.",
                "Stopping!"), Printed.by(() -> Main.main(new String[0])));
    }

    @Test
    @DisplayName("a request never changes the state; only the caller does")
    void theStatesNeverChangeTheState() {
        ElevatorState stopped = new StoppedState();
        Elevator elevator = new Elevator(stopped);
        Printed.by(() -> {
            elevator.goUp();
            elevator.goDown();
            elevator.stop();
            elevator.openDoor();
        });
        assertSame(stopped, elevator.getState());
    }

    @Test
    @DisplayName("a stopped elevator does not claim to be moving")
    void aStoppedElevatorSaysItIsStarting() {
        Elevator elevator = new Elevator(new StoppedState());
        assertEquals(List.of("Starting to go up."), Printed.by(elevator::goUp));
    }

    @Test
    @DisplayName("the elevator is the context, not a state")
    void theElevatorIsNotAState() {
        assertFalse(ElevatorState.class.isAssignableFrom(Elevator.class));
    }
}
