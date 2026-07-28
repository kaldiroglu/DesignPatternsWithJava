package dev.kaldiroglu.dp.structural.composite.fileSystem;

import dev.kaldiroglu.dp.structural.composite.fileSystem.iterator.DirectoryIterator;
import dev.kaldiroglu.dp.structural.composite.fileSystem.iterator.StorageIterator;

import java.util.ArrayList;
import java.util.List;

/**
 * The Composite: a directory is storage, and it holds storage.
 * <p>
 * Every operation here is the same two lines — do this directory's part, then ask the
 * children to do theirs. The recursion is the pattern; there is nothing else to it.
 */
public class Directory extends StorageElement implements StorageContainer {

    private static final long DIRECTORY_BYTES = 256;

    private final List<Storage> elements = new ArrayList<>();

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
        return DIRECTORY_BYTES + elements.stream().mapToLong(Storage::size).sum();
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
     * The first version asked {@code isDirectory()} and branched on the answer — a type test
     * in the one pattern whose entire purpose is to remove them. Each element now renders
     * itself, and this method neither knows nor asks what kind it is.
     */
    @Override
    public String render(String indent) {
        StringBuilder out = new StringBuilder(indent + getName() + "/");
        for (Storage element : elements) {
            out.append(System.lineSeparator()).append(element.render(indent + "    "));
        }
        return out.toString();
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
        }
    }

    @Override
    public void remove(Storage element) {
        elements.remove(element);
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
    public int count() {
        int total = 0;
        StorageIterator it = iterator();
        while (it.hasNext()) {
            it.next();
            total++;
        }
        return total;
    }
}
