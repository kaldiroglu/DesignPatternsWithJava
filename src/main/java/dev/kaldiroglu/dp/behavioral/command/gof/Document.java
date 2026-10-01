package dev.kaldiroglu.dp.behavioral.command.gof;

/**
 * GoF's {@code Document}: a <b>Receiver</b>. It knows how to open, copy and paste, and it
 * knows nothing about menus.
 * <p>
 * Design Patterns, p. 233: "Document objects... are receivers of commands like
 * PasteCommand".
 */
public final class Document {

    private final String name;
    private final Clipboard clipboard;
    private final StringBuilder text = new StringBuilder();
    private boolean open;

    public Document(String name, Clipboard clipboard) {
        this.name = name;
        this.clipboard = clipboard;
    }

    public String name() {
        return name;
    }

    public void open() {
        open = true;
    }

    public boolean isOpen() {
        return open;
    }

    public void type(String words) {
        text.append(words);
    }

    public void copy() {
        clipboard.put(text.toString());
    }

    public void paste() {
        text.append(clipboard.contents());
    }

    public String text() {
        return text.toString();
    }
}
