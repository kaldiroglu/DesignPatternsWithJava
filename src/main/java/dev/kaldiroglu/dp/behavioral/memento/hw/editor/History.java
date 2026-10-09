package dev.kaldiroglu.dp.behavioral.memento.hw.editor;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The <b>Caretaker</b>: undo and redo with two stacks of mementos.
 * <p>
 * Before every change, the current snapshot goes on the undo stack. Undo moves the current
 * snapshot to the redo stack and restores the last one; redo does the opposite. A new change
 * clears the redo stack.
 */
public final class History {

    private final TextEditor editor;
    private final Deque<TextEditor.Snapshot> undo = new ArrayDeque<>();
    private final Deque<TextEditor.Snapshot> redo = new ArrayDeque<>();

    public History(TextEditor editor) {
        this.editor = editor;
    }

    public void type(String words) {
        undo.push(editor.save());
        redo.clear();
        editor.type(words);
    }

    public void undo() {
        if (!undo.isEmpty()) {
            redo.push(editor.save());
            editor.restore(undo.pop());
        }
    }

    public void redo() {
        if (!redo.isEmpty()) {
            undo.push(editor.save());
            editor.restore(redo.pop());
        }
    }
}
