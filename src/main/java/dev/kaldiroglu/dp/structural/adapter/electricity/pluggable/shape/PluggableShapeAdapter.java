package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.shape;

import dev.kaldiroglu.dp.structural.adapter.gof.*;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * <b>Pluggable adapter</b>, parameterized form &ndash; GoF technique (c), p. 143.
 *
 * <p>A single adapter class turns <i>any</i> object into a {@link Shape}: instead of writing a new
 * adapter subclass per adaptee, the adaptation for the <b>narrow interface</b>
 * ({@code boundingBox} + {@code isEmpty}) is <b>injected as lambdas</b>. GoF's Smalltalk "blocks"
 * are Java's functional interfaces ({@link Supplier}, {@link BooleanSupplier}).</p>
 *
 * <p>See {@code PluggableDemo} for the same class adapting two completely unrelated adaptees
 * (a {@code TextView} and a {@link Circle}) with no new subclasses.</p>
 */
public final class PluggableShapeAdapter implements Shape {

    private final String description;
    private final Supplier<BoundingBox> boundingBoxFn;
    private final BooleanSupplier isEmptyFn;

    public PluggableShapeAdapter(String description,
                                 Supplier<BoundingBox> boundingBoxFn,
                                 BooleanSupplier isEmptyFn) {
        this.description = description;
        this.boundingBoxFn = boundingBoxFn;
        this.isEmptyFn = isEmptyFn;
    }

    @Override
    public BoundingBox boundingBox() {
        return boundingBoxFn.get();
    }

    @Override
    public boolean isEmpty() {
        return isEmptyFn.getAsBoolean();
    }

    /**
     * Manipulation is not the operation that varies between adaptees, so it is not part of the
     * narrow interface. We reuse {@link TextManipulator}; {@code this} is available at call time,
     * so there is no construction-time cycle.
     */
    @Override
    public Manipulator createManipulator() {
        return new TextManipulator(this);
    }

    @Override
    public String toString() {
        return description;
    }
}
