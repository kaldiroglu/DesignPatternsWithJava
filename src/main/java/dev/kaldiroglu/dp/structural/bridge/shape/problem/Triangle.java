package dev.kaldiroglu.dp.structural.bridge.shape.problem;

/** One shape kind. It cannot draw itself: only its per-device subclasses can. */
public abstract class Triangle extends AbstractShape {

    protected Triangle(String name) {
        super(name);
    }
}
