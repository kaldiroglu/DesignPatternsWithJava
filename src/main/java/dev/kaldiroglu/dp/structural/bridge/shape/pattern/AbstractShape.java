package dev.kaldiroglu.dp.structural.bridge.shape.pattern;

import java.util.Objects;

/**
 * The Abstraction: a shape that holds a device and never asks which one it has.
 * <p>
 * Every subclass expresses itself purely in {@link ShapeDrawer} primitives. That is the
 * discipline the pattern asks for, and the payback is that {@link Triangle} was added later
 * without a single drawer being touched.
 */
public abstract class AbstractShape implements Shape {

    private final String name;
    protected ShapeDrawer drawer;

    protected AbstractShape(String name, ShapeDrawer drawer) {
        this.name = name;
        this.drawer = Objects.requireNonNull(drawer, "a shape must have something to draw on");
    }

    public String getName() {
        return name;
    }

    @Override
    public void setDrawer(ShapeDrawer drawer) {
        this.drawer = Objects.requireNonNull(drawer);
    }
}
