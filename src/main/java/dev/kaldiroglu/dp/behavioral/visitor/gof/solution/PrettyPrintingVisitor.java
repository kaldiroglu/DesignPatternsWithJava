package dev.kaldiroglu.dp.behavioral.visitor.gof.solution;

/** A <b>ConcreteVisitor</b>: prints the program back as text. */
public final class PrettyPrintingVisitor implements NodeVisitor {

    private final StringBuilder text = new StringBuilder();

    @Override
    public void visitAssignment(AssignmentNode node) {
        text.append(node.variable()).append(" = ");
        node.value().accept(this);
    }

    @Override
    public void visitVariableRef(VariableRefNode node) {
        text.append(node.name());
    }

    @Override
    public void visitConstant(ConstantNode node) {
        text.append(node.value());
    }

    @Override
    public void visitAdd(AddNode node) {
        node.left().accept(this);
        text.append(" + ");
        node.right().accept(this);
    }

    public String text() {
        return text.toString();
    }
}
