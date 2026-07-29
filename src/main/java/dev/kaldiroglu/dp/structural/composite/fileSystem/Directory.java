package dev.kaldiroglu.dp.structural.composite.fileSystem;

import dev.kaldiroglu.dp.structural.composite.fileSystem.iterator.DirectoryIterator;
import dev.kaldiroglu.dp.structural.composite.fileSystem.iterator.StorageIterator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * The Composite: a directory is storage, and it holds storage.
 * <p>
 * Every operation here is the same two lines — do this directory's part, then ask the
 * children to do theirs. The recursion is the pattern; there is nothing else to it.
 */
public class Directory extends StorageElement implements StorageContainer {

    private static final long DIRECTORY_BYTES = 256;

    private final List<Storage> elements = new ArrayList<>();

    /**
     * The cached total, and the number of times any directory has had to work one out.
     *
     * <p>GoF's implementation issue 8: "the Composite class can cache traversal or search
     * information about its children". Without it, {@code size()} walks the whole subtree on
     * every call, and a report asking four questions walks it four times.</p>
     *
     * <p>The cache is what finally makes the {@code parent} reference load-bearing. A change
     * anywhere invalidates every ancestor's total, and the only way to reach them is upward —
     * see {@link #invalidate()}.</p>
     */
    private long cachedSize = UNCACHED;
    private static final long UNCACHED = -1;
    private static int recomputations;

    public Directory(String name) {
        this(name, null);
    }

    public Directory(String name, Directory parent) {
        super(name, parent);
        attach();
    }

    /**
     * The payoff.
     * <p>
     * One call, one number, any depth — and no client ever writes a loop or asks what kind of
     * element it is holding.
     */
    @Override
    public long size() {
        if (cachedSize == UNCACHED) {
            recomputations++;
            cachedSize = DIRECTORY_BYTES + elements.stream().mapToLong(Storage::size).sum();
        }
        return cachedSize;
    }

    /**
     * Throws this directory's total away, and every ancestor's with it.
     *
     * <p>Upward, because a child's change invalidates the totals of everything above it and
     * nothing below. This is the half of caching people forget, and the half that makes it
     * wrong when they do.</p>
     */
    void invalidate() {
        if (cachedSize == UNCACHED) {
            return;                       // already dirty, so the ancestors are too
        }
        cachedSize = UNCACHED;
        if (getParent() != null) {
            getParent().invalidate();
        }
    }

    /** How many times any directory has actually computed a total. For the tests. */
    public static int recomputations() {
        return recomputations;
    }

    public static void resetRecomputations() {
        recomputations = 0;
    }

    /** A deep copy: the directory and everything under it, detached from any parent. */
    @Override
    public Storage copy() {
        Directory copy = new Directory(getName(), null);
        for (Storage element : elements) {
            Storage childCopy = element.copy();
            copy.add(childCopy);
        }
        return copy;
    }

    /**
     * Renders the subtree.
     * <p>
     * Each element renders itself, and this method neither knows nor asks what kind it is.
     * A type test here would defeat the one thing the pattern is for.
     */
    @Override
    public String render(String indent) {
        StringBuilder out = new StringBuilder(indent + getName() + "/");
        for (Storage element : elements) {
            out.append(System.lineSeparator()).append(element.render(indent + "    "));
        }
        return out.toString();
    }

    /** The newest modification in the subtree — a maximum, not a sum. */
    @Override
    public Instant lastModified() {
        return elements.stream()
                .map(Storage::lastModified)
                .max(Comparator.naturalOrder())
                .filter(newest -> newest.isAfter(super.lastModified()))
                .orElseGet(super::lastModified);
    }

    /** This directory, plus everything beneath it. */
    @Override
    public int count() {
        return 1 + elements.stream().mapToInt(Storage::count).sum();
    }

    /**
     * The biggest leaf under here.
     *
     * <p>A reduction that returns an element rather than a number, and one a directory
     * cannot answer for itself — it has to ask, and take the best answer it gets back.</p>
     */
    @Override
    public Optional<Storage> largest() {
        return elements.stream()
                .map(Storage::largest)
                .flatMap(Optional::stream)
                .max(Comparator.comparingLong(Storage::size));
    }

    @Override
    public Optional<Storage> find(String wanted) {
        if (getName().equals(wanted)) {
            return Optional.of(this);
        }
        return elements.stream()
                .map(element -> element.find(wanted))
                .flatMap(Optional::stream)
                .findFirst();
    }

    @Override
    public List<Storage> findAll(Predicate<Storage> test) {
        List<Storage> found = new ArrayList<>();
        if (test.test(this)) {
            found.add(this);
        }
        for (Storage element : elements) {
            found.addAll(element.findAll(test));
        }
        return List.copyOf(found);
    }

    public void list() {
        System.out.println(render(""));
    }

    @Override
    public void add(Storage element) {
        if (element == this) {
            throw new IllegalArgumentException("a directory cannot contain itself");
        }
        if (!elements.contains(element)) {
            elements.add(element);
            if (element instanceof StorageElement child) {
                child.setParent(this);
            }
            invalidate();
        }
    }

    @Override
    public void remove(Storage element) {
        if (elements.remove(element)) {
            invalidate();
        }
    }

    @Override
    public List<Storage> elements() {
        return List.copyOf(elements);
    }

    @Override
    public StorageIterator iterator() {
        return new DirectoryIterator(this);
    }

    /** How many elements are in the whole subtree, this directory not counted. */
}
