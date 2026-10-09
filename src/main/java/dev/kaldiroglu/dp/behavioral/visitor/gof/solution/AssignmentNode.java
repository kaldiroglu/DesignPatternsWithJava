package dev.kaldiroglu.dp.behavioral.visitor.gof.solution;

/** A <b>ConcreteElement</b>: {@code variable = value} */
public record AssignmentNode(String variable, Node value) implements Node {

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visitAssignment(this);
    }
}
