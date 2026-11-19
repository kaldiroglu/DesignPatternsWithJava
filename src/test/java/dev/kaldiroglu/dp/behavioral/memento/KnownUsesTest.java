package dev.kaldiroglu.dp.behavioral.memento;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** The JDK known uses on the Part 4 slide: StateEdit, serialization, and what a record copy shares. */
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

    /** A class with a private field and no getter for it. */
    private static final class Secret implements Serializable {
        private final int code;

        Secret(int code) {
            this.code = code;
        }

        boolean hasCode(int value) {
            return code == value;
        }
    }

    @Test
    @DisplayName("ObjectOutputStream writes a private field, and reading the bytes back gives it again")
    void serializationWritesPrivateFields() throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(new Secret(42));
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            Secret back = (Secret) in.readObject();
            assertTrue(back.hasCode(42));
        }
    }

    /** A record that holds a list. */
    private record Snapshot(String place, List<String> items) {
    }

    @Test
    @DisplayName("a record is shallow: a record that holds a list shares that list with whoever made it")
    void aRecordCopyIsShallow() {
        List<String> items = new ArrayList<>(List.of("sword"));
        Snapshot snapshot = new Snapshot("bridge", items);
        items.add("potion");
        assertEquals(List.of("sword", "potion"), snapshot.items());
    }
}
