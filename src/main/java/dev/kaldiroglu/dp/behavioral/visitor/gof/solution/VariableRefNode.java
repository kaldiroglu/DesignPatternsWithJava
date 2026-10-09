package dev.kaldiroglu.dp.behavioral.visitor.gof.solution;

/** A <b>ConcreteElement</b>: A use of a variable. */
public record VariableRefNode(String name) implements Node {

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visitVariableRef(this);
    }
}
