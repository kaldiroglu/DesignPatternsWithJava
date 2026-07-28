package dev.kaldiroglu.dp.structural.composite.fileSystem;

/** A Leaf: the Windows name for the same idea, resolved by path rather than by identity. */
public class ShortCut extends Link {

    public ShortCut(String name, Directory parent, Storage target) {
        super(name, parent, target);
        attach();
    }

    @Override
    public Storage copy() {
        return new ShortCut(getName(), null, getTarget());
    }
}
