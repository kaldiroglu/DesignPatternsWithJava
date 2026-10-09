package dev.kaldiroglu.dp.behavioral.visitor.gof.solution;

/** A <b>ConcreteElement</b>: {@code left + right} */
public record AddNode(Node left, Node right) implements Node {

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visitAdd(this);
    }
}
