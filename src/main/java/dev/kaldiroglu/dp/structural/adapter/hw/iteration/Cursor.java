package dev.kaldiroglu.dp.structural.adapter.hw.iteration;

/**
 * The Adaptee: a cursor from an in-house collection library, written before {@code Iterator}
 * existed and still used by code nobody wants to touch.
 * <p>
 * The operation model is the old one — ask whether more remain, then fetch. Nothing here is
 * wrong; it is simply not the shape the rest of the world settled on.
 */
public interface Cursor<T> {

    boolean hasMoreElements();

    T nextElement();
}
