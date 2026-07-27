package dev.kaldiroglu.dp.structural.bridge.shape.pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared state for the concrete drawers, and the measuring instrument for the tests.
 * <p>
 * Every device call is recorded, so a test can assert what a shape actually asked for rather
 * than describing it. Note that {@link #calls()} is <em>not</em> on {@link ShapeDrawer}: the
 * implementor interface stays primitives-only, and this is a detail of how the examples are
 * observed.
 */
public abstract class AbstractShapeDrawer implements ShapeDrawer {

    private final String name;
    private final List<String> calls = new ArrayList<>();

    protected AbstractShapeDrawer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /** What this device was asked to do, in order. */
    public List<String> calls() {
        return List.copyOf(calls);
    }

    public void resetCalls() {
        calls.clear();
    }

    protected void record(String call) {
        calls.add(call);
        System.out.println("  " + name + ": " + call);
    }
}
