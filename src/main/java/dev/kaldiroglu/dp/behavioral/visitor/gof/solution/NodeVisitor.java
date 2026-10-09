package dev.kaldiroglu.dp.behavioral.visitor.gof.solution;

/**
 * The <b>Visitor</b>: one operation for each kind of node. GoF name the methods after the
 * class — {@code visitAssignment}, {@code visitVariableRef} — because not every language
 * has overloading.
 */
public interface NodeVisitor {

    void visitAssignment(AssignmentNode node);

    void visitVariableRef(VariableRefNode node);

    void visitConstant(ConstantNode node);

    void visitAdd(AddNode node);
}
