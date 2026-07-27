package dev.kaldiroglu.dp.structural.adapter.gof;

/**
 * GoF p. 146: a {@code Manipulator} knows how to animate a {@link Shape} in response to user
 * input (for example, dragging a handle). {@link Shape#createManipulator()} is a factory method
 * &ndash; each pluggable returns the manipulator that understands it.
 */
public abstract class Manipulator {

    public abstract void manipulate();
}
