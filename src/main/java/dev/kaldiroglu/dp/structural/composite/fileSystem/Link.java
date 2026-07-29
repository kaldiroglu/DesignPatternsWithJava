package dev.kaldiroglu.dp.structural.composite.fileSystem;

import java.util.Objects;

/**
 * A Leaf that points at another element.
 * <p>
 * A link names what it points at, which makes it a real element of the tree and raises a
 * good question: <strong>how big is it?</strong>
 * <p>
 * The answer taken here is that a link costs its own few bytes and <em>not</em> the size of
 * what it points at, because the target is counted where it actually lives. Any other answer
 * makes {@code size()} on a root double-count, and a tree that lies about its own size is
 * worse than one that cannot be asked.
 */
public abstract class Link extends StorageElement {

    private static final long LINK_BYTES = 64;

    private final Storage target;

    protected Link(String name, Directory parent, Storage target) {
        super(name, parent);
        this.target = Objects.requireNonNull(target, "a link must point at something");
    }

    public Storage getTarget() {
        return target;
    }

    @Override
    public final long size() {
        return LINK_BYTES;
    }

    @Override
    public String render(String indent) {
        return indent + getName() + "  -> " + target.getName();
    }
}
