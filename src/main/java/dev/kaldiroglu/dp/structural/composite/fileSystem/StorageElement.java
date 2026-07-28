package dev.kaldiroglu.dp.structural.composite.fileSystem;

import java.util.Objects;

/**
 * Shared state and behavior for anything that can sit in a directory.
 * <p>
 * Note the type of {@code parent}: {@link Directory}, not {@code Storage}. The first version
 * declared it as {@code Storage} and then cast to {@code Directory} in three places — and a
 * cast that always succeeds is a field with the wrong type.
 */
public abstract class StorageElement implements Storage {

    private String name;
    private Directory parent;

    protected StorageElement(String name, Directory parent) {
        this.name = Objects.requireNonNull(name, "every element needs a name");
        this.parent = parent;
    }

    /**
     * Adds this element to its parent.
     * <p>
     * Called by the concrete constructors rather than by this one, and deliberately so: a
     * subclass's own fields are not initialized until after {@code super(..)} returns, so
     * publishing {@code this} from here would hand the parent a half-built object.
     */
    protected final void attach() {
        if (parent != null) {
            parent.add(this);
        }
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final void rename(String newName) {
        this.name = Objects.requireNonNull(newName);
    }

    @Override
    public void save() {
        System.out.println("Saving " + name);
    }

    /** Null-safe: the first version threw a NullPointerException on a root directory. */
    @Override
    public final void delete() {
        if (parent != null) {
            parent.remove(this);
            parent = null;
        }
    }

    /**
     * Moves properly, which neither of the first two versions did.
     * <p>
     * {@code StorageElement.move} used to remove the element from its old parent without
     * updating {@code parent}, so the element still believed it lived where it no longer did.
     * {@code Directory.move} used to do the opposite — set the new parent and add itself to
     * the target, while leaving the old directory still listing it, so one directory appeared
     * in two places at once. Doing both, once, in one place, fixes both.
     */
    @Override
    public final void move(Directory target) {
        Objects.requireNonNull(target, "move needs somewhere to move to");
        if (target == this) {
            throw new IllegalArgumentException("an element cannot be moved into itself");
        }
        if (parent != null) {
            parent.remove(this);
        }
        parent = target;
        target.add(this);
    }

    public Directory getParent() {
        return parent;
    }

    /** Package-private: used by {@link Directory} when it adopts an element. */
    final void setParent(Directory parent) {
        this.parent = parent;
    }

    /** The path from the root down to this element. */
    public String path() {
        return parent == null ? name : parent.path() + "/" + name;
    }

    @Override
    public String toString() {
        return name;
    }
}
