package dev.kaldiroglu.dp.structural.bridge.basic.pattern;

/**
 * The Abstraction's interface — the pattern reduced to its bones.
 * <p>
 * Two abstractions and two implementations are <strong>four</strong> classes here. In
 * {@code basic.problem} the same four combinations take six. Add a third implementation and
 * this package grows by one; the other grows by two.
 */
public interface AnAbstraction {

    void doIt();
}
