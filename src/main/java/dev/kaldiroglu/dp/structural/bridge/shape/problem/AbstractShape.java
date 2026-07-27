package dev.kaldiroglu.dp.structural.bridge.shape.problem;

/**
 * The root of the shape hierarchy.
 * <p>
 * Every shape the editor can draw is a class, and so is every combination of shape and
 * device — because the device is decided by which class you instantiate.
 */
public abstract class AbstractShape implements Shape {

    private final String name;

    protected AbstractShape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (" + getClass().getSimpleName() + ")";
    }
}
