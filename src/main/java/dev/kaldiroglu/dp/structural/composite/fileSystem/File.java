package dev.kaldiroglu.dp.structural.composite.fileSystem;

/** A Leaf: a file has bytes and no children. */
public class File extends StorageElement {

    private final long bytes;

    public File(String name, Directory parent) {
        this(name, parent, 1024);
    }

    public File(String name, Directory parent, long bytes) {
        super(name, parent);
        this.bytes = bytes;
        attach();
    }

    /** A leaf answers for itself. That is the whole of a leaf's job. */
    @Override
    public long size() {
        return bytes;
    }

    @Override
    public Storage copy() {
        return new File(getName(), null, bytes);
    }

    @Override
    public String render(String indent) {
        return indent + getName() + "  (" + bytes + " bytes)";
    }
}
