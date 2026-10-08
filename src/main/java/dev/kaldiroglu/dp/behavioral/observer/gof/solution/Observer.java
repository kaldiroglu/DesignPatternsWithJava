package dev.kaldiroglu.dp.behavioral.observer.gof.solution;

/**
 * The <b>Observer</b>: GoF's {@code Observer}, with one operation.
 * <p>
 * The subject passes itself, so an observer that watches several subjects knows which one
 * changed. That is GoF implementation issue 2 (observing more than one subject).
 */
public interface Observer {

    void update(Subject changed);
}
