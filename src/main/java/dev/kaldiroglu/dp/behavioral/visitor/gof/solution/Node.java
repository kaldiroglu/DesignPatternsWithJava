package dev.kaldiroglu.dp.behavioral.visitor.gof.solution;

/** The <b>Element</b>: a node only accepts a visitor. It no longer knows any compiler job. */
public interface Node {

    void accept(NodeVisitor visitor);
}
