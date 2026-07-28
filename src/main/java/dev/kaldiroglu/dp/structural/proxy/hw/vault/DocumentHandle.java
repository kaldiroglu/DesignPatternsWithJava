package dev.kaldiroglu.dp.structural.proxy.hw.vault;

import java.util.Objects;

/**
 * A <em>smart reference</em> — GoF's fourth kind (p. 208): "a replacement for a bare pointer
 * that performs additional actions when an object is accessed".
 * <p>
 * GoF list three such actions and this class does all three:
 * <ul>
 *   <li><strong>counting references</strong>, so the document is closed only when the last
 *       holder lets go;</li>
 *   <li><strong>loading a persistent object on first access</strong>, which is the virtual
 *       proxy hiding inside this one;</li>
 *   <li><strong>locking</strong>, so a second writer is refused rather than silently
 *       overwriting the first.</li>
 * </ul>
 * <p>
 * The lesson worth taking is the first. {@code close()} on this proxy does not mean close —
 * it means <em>I am finished with it</em>. Two holders, two closes, one actual close. Get
 * that wrong and you either leak the handle or shut it under somebody's feet.
 */
public class DocumentHandle implements Document {

    private final String name;
    private final String initialText;
    private StoredDocument document;
    private int holders;
    private String writeLockOwner;

    public DocumentHandle(String name, String initialText) {
        this.name = Objects.requireNonNull(name);
        this.initialText = initialText;
    }

    /** Registers a holder. The document is opened on the first one, not before. */
    public DocumentHandle acquire() {
        holders++;
        return this;
    }

    private StoredDocument document() {
        if (document == null) {
            document = new StoredDocument(name, initialText);
        }
        return document;
    }

    @Override
    public String read() {
        requireHolder();
        return document().read();
    }

    @Override
    public void write(String text) {
        requireHolder();
        if (writeLockOwner != null) {
            throw new IllegalStateException(name + " is being edited by " + writeLockOwner);
        }
        document().write(text);
    }

    public void lockForWriting(String owner) {
        if (writeLockOwner != null) {
            throw new IllegalStateException(name + " is already locked by " + writeLockOwner);
        }
        writeLockOwner = owner;
    }

    public void writeAs(String owner, String text) {
        requireHolder();
        if (!owner.equals(writeLockOwner)) {
            throw new IllegalStateException(owner + " does not hold the lock on " + name);
        }
        document().write(text);
    }

    public void unlock(String owner) {
        if (owner.equals(writeLockOwner)) {
            writeLockOwner = null;
        }
    }

    /** Not "close it" — "I am finished with it". The last one out shuts the door. */
    @Override
    public void close() {
        if (holders == 0) {
            return;
        }
        holders--;
        if (holders == 0 && document != null) {
            document.close();
            document = null;
        }
    }

    public int holders() {
        return holders;
    }

    public boolean isOpen() {
        return document != null && document.isOpen();
    }

    private void requireHolder() {
        if (holders == 0) {
            throw new IllegalStateException("acquire " + name + " before using it");
        }
    }
}
