package dev.kaldiroglu.dp.behavioral.visitor.gof.solution;

/** A <b>ConcreteElement</b>: A number written in the program. */
public record ConstantNode(int value) implements Node {

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visitConstant(this);
    }
}
