package dev.kaldiroglu.dp.behavioral.visitor.gof.solution;

import java.util.ArrayList;
import java.util.List;

/** A <b>ConcreteVisitor</b>: code for a stack machine. Children first, then the node. */
public final class CodeGeneratingVisitor implements NodeVisitor {

    private final List<String> code = new ArrayList<>();

    @Override
    public void visitAssignment(AssignmentNode node) {
        node.value().accept(this);
        code.add("STORE " + node.variable());
    }

    @Override
    public void visitVariableRef(VariableRefNode node) {
        code.add("LOAD " + node.name());
    }

    @Override
    public void visitConstant(ConstantNode node) {
        code.add("PUSH " + node.value());
    }

    @Override
    public void visitAdd(AddNode node) {
        node.left().accept(this);
        node.right().accept(this);
        code.add("ADD");
    }

    public List<String> code() {
        return List.copyOf(code);
    }
}
