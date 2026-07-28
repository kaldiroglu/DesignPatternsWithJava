package dev.kaldiroglu.dp.structural.composite.fileSystem;

/** A Leaf: the macOS name for a link that survives its target being moved. */
public class Alias extends Link {

    public Alias(String name, Directory parent, Storage target) {
        super(name, parent, target);
        attach();
    }

    @Override
    public Storage copy() {
        return new Alias(getName(), null, getTarget());
    }
}
