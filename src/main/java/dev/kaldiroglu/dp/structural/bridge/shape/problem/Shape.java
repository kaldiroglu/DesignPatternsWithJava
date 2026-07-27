package dev.kaldiroglu.dp.structural.bridge.shape.problem;

/**
 * What a shape can do.
 * <p>
 * The interface is not the problem. Everything that goes wrong in this package goes wrong in
 * the hierarchy underneath it, where the <em>device</em> is a superclass.
 */
public interface Shape {

    void draw();

    void erase();
}
