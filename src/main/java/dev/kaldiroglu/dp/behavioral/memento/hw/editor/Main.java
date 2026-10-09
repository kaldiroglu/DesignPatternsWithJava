package dev.kaldiroglu.dp.behavioral.memento.hw.editor;

/**
 * Types two words, undoes twice and redoes once. The bar shows where the cursor is.
 */
public final class Main {

    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        History history = new History(editor);

        history.type("Hello");
        history.type(" world");
        System.out.println("After typing:     " + editor);
        history.undo();
        System.out.println("After one undo:   " + editor);
        history.undo();
        System.out.println("After two undos:  " + editor);
        history.redo();
        System.out.println("After one redo:   " + editor);
    }
}
