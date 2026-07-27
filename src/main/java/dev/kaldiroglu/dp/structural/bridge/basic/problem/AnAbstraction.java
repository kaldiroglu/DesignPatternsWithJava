package dev.kaldiroglu.dp.structural.bridge.basic.problem;

/**
 * The Abstraction's interface — and the last thing in this package that is not a product.
 * <p>
 * Below it, the implementation is chosen by <em>which class you instantiate</em>. So every
 * combination of refinement and implementation needs a class of its own, and the count is
 * m x n rather than m + n.
 */
public interface AnAbstraction {

    void doIt();
}
