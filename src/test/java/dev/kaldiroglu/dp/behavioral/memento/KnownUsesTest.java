package dev.kaldiroglu.dp.behavioral.memento;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;
import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;

/** The JDK known use quoted in the Part 4 notes: StateEdit keeps the state an object wrote. */
class KnownUsesTest {

    /** An object that writes its own state into the table, and reads it back. */
    private static final class Point implements StateEditable {
        int x = 10;

        @Override
        public void storeState(Hashtable<Object, Object> state) {
            state.put("x", x);
        }

        @Override
        public void restoreState(Hashtable<?, ?> state) {
            Object saved = state.get("x");
            if (saved != null) {
                x = (Integer) saved;
            }
        }
    }

    @Test
    @DisplayName("x set to 99, undo, and x is 10 again")
    void aStateEdit() {
        Point point = new Point();
        StateEdit edit = new StateEdit(point);
        point.x = 99;
        edit.end();

        edit.undo();

        assertEquals(10, point.x);
    }
}
