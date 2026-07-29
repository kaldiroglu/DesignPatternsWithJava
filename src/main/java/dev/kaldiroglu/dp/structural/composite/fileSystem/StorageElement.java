package dev.kaldiroglu.dp.structural.composite.fileSystem;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Shared state and behavior for anything that can sit in a directory.
 * <p>
 * Note the type of {@code parent}: {@link Directory}, not {@code Storage}. Only a directory
 * can hold anything, so that is the type the field should have — declaring it as
 * {@code Storage} would only buy casts back to {@code Directory} at every use.
 */
public abstract class StorageElement implements Storage {

    private String name;
    private Directory parent;
    private Instant modified = Instant.EPOCH;

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

    /** Null-safe: a root has no parent to be detached from, and that is not an error. */
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
     * Both halves happen here, in one place: leave the old parent, then join the new one.
     * Doing only one of them is how an element ends up believing it lives where it does not,
     * or appearing in two directories at once.
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

    /** When this element itself last changed. A directory takes the newest of its subtree. */
    @Override
    public Instant lastModified() {
        return modified;
    }

    /** Records a modification time. Bubbles nothing: a directory asks its children instead. */
    public final void touch(Instant when) {
        this.modified = Objects.requireNonNull(when);
    }

    /** A leaf is one element. {@link Directory} adds its children. */
    @Override
    public int count() {
        return 1;
    }

    /** A leaf is its own largest. {@link Directory} asks its children. */
    @Override
    public Optional<Storage> largest() {
        return Optional.of(this);
    }

    @Override
    public Optional<Storage> find(String wanted) {
        return name.equals(wanted) ? Optional.of(this) : Optional.empty();
    }

    @Override
    public List<Storage> findAll(Predicate<Storage> test) {
        return test.test(this) ? List.of(this) : List.of();
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
