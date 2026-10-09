package dev.kaldiroglu.dp.behavioral.memento.hw.editor;

/**
 * Homework 1: the <b>Originator</b>. A text and a cursor; it can save both in a memento and
 * take them back.
 */
public final class TextEditor {

    /** The <b>Memento</b>: no getters, so only the editor reads it. */
    public static final class Snapshot {
        private final String text;
        private final int cursor;

        private Snapshot(String text, int cursor) {
            this.text = text;
            this.cursor = cursor;
        }
    }

    private String text = "";
    private int cursor;

    public void type(String words) {
        text = text.substring(0, cursor) + words + text.substring(cursor);
        cursor += words.length();
    }

    public void moveCursor(int position) {
        cursor = Math.max(0, Math.min(position, text.length()));
    }

    public Snapshot save() {
        return new Snapshot(text, cursor);
    }

    public void restore(Snapshot snapshot) {
        text = snapshot.text;
        cursor = snapshot.cursor;
    }

    @Override
    public String toString() {
        return text.substring(0, cursor) + "|" + text.substring(cursor);
    }
}
