package dev.kaldiroglu.dp.behavioral.templateMethod.gof.solution;

import java.util.List;

/**
 * GoF's {@code Document}. Opening is the same for every kind of document; reading is not,
 * so {@link #doRead} is a primitive operation that each kind writes.
 * <p>
 * GoF's naming convention: primitive operations start with "Do" — {@code DoRead},
 * {@code DoCreateDocument} — so that a reader can see which methods a subclass must write.
 * That is GoF implementation issue 3 (naming conventions).
 */
public abstract class Document {

    private final String name;
    private boolean open;

    protected Document(String name) {
        this.name = name;
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

    /** A primitive operation: read the file's contents in this document's own way. */
    protected abstract void doRead(List<String> events);
}
