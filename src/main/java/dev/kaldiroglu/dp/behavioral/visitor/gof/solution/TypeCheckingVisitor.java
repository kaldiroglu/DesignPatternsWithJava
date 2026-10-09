package dev.kaldiroglu.dp.behavioral.visitor.gof.solution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A <b>ConcreteVisitor</b>: reports a variable used before it is assigned.
 * <p>
 * The visitor walks the tree itself — it calls {@code accept} on the children. This is one
 * answer to GoF implementation issue 2 (who is responsible for traversing the object
 * structure?). The visitor also keeps state between nodes: the variables assigned so far.
 */
public final class TypeCheckingVisitor implements NodeVisitor {

    private final Set<String> assigned = new HashSet<>();
    private final List<String> errors = new ArrayList<>();

    @Override
    public void visitAssignment(AssignmentNode node) {
        node.value().accept(this);
        assigned.add(node.variable());
    }

    @Override
    public void visitVariableRef(VariableRefNode node) {
        if (!assigned.contains(node.name())) {
            errors.add(node.name() + " is used before it is assigned");
        }
    }

    @Override
    public void visitConstant(ConstantNode node) {
    }

    @Override
    public void visitAdd(AddNode node) {
        node.left().accept(this);
        node.right().accept(this);
    }

    public List<String> errors() {
        return List.copyOf(errors);
    }
}
