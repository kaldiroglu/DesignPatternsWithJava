package dev.kaldiroglu.dp.behavioral.memento.pattern1;

import dev.kaldiroglu.dp.behavioral.memento.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** The caretaker keeps the mementos; each undo goes back exactly one save. */
class CaretakerTest {

    @Test
    @DisplayName("Main undoes to the last save, then to the one before it")
    void mainUndoesOneSaveAtATime() {
        assertEquals(List.of(
                "New state: state-1",
                "Caretaker: Saving state: state-1",
                "New state: state-2",
                "New state: state-3",
                "Caretaker: Saving state: state-3",
                "New state: state-4",
                "Caretaker: Undoing to: state-3",
                "Caretaker: Undoing to: state-1",
                "Caretaker: Nothing to undo.",
                "Final: Originator [state=state-1]"), Printed.by(() -> Main.main(new String[0])));
    }

    @Test
    @DisplayName("one undo after one save gives back the saved state, not an older one")
    void undoRestoresTheLastSave() {
        Originator originator = new Originator("a");
        Caretaker caretaker = new Caretaker(originator);
        Printed.by(() -> {
            originator.setState("b");
            caretaker.save();
            originator.setState("c");
            caretaker.undo();
        });
        assertEquals("Originator [state=b]", originator.toString());
    }

    @Test
    @DisplayName("the example runs on one thread")
    void noThreads() {
        assertFalse(Thread.class.isAssignableFrom(Caretaker.class));
        assertFalse(Printed.codeOf("memento/pattern1/Main.java").contains("Thread"));
    }
}
