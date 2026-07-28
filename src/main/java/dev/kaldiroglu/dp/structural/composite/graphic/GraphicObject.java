package dev.kaldiroglu.dp.structural.composite.graphic;

/** Shared state for anything drawable: what it is called, and what color it is. */
public abstract class GraphicObject implements Graphic {

    protected final String name;
    protected final String color;

    protected GraphicObject(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    /** A leaf is one shape. {@link Canvas} overrides this to add up its children. */
    @Override
    public int shapeCount() {
        return 1;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " \"" + name + "\", " + color;
    }
}
