package dev.kaldiroglu.dp.behavioral.memento.hw.editor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Homework 1: undo and redo with two stacks of snapshots. */
class HistoryTest {

    @Test
    @DisplayName("undo goes back one step at a time, and redo goes forward again")
    void undoAndRedo() {
        TextEditor editor = new TextEditor();
        History history = new History(editor);
        history.type("Hello");
        history.type(" world");
        assertEquals("Hello world|", editor.toString());

        history.undo();
        assertEquals("Hello|", editor.toString());
        history.undo();
        assertEquals("|", editor.toString());
        history.redo();
        assertEquals("Hello|", editor.toString());
    }

    @Test
    @DisplayName("a new change clears the redo stack")
    void aNewChangeClearsRedo() {
        TextEditor editor = new TextEditor();
        History history = new History(editor);
        history.type("Hello");
        history.undo();
        history.type("Hi");
        history.redo();
        assertEquals("Hi|", editor.toString());
    }

    @Test
    @DisplayName("the snapshot keeps the cursor as well as the text")
    void theCursorIsSaved() {
        TextEditor editor = new TextEditor();
        History history = new History(editor);
        history.type("Hello");
        editor.moveCursor(0);
        history.type(">");
        assertEquals(">|Hello", editor.toString());
        history.undo();
        assertEquals("|Hello", editor.toString());
    }
}
