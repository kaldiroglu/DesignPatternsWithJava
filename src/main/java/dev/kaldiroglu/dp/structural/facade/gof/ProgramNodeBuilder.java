package dev.kaldiroglu.dp.structural.facade.gof;

/**
 * Subsystem class (GoF, p. 185): builds the parse tree.
 *
 * <p>The {@link Parser} calls the {@code newXxx} factory methods to create
 * {@link ProgramNode}s without knowing their concrete classes, and accumulates
 * top-level statements into a root block returned by {@link #getRootNode()}.</p>
 */
public class ProgramNodeBuilder {

    private final BlockNode root = new BlockNode();

    public ProgramNode newConstant(int value) {
        return new ConstantNode(value);
    }

    public ProgramNode newVariable(String name) {
        return new VariableRefNode(name);
    }

    public ProgramNode newBinaryOperator(char operator, ProgramNode left, ProgramNode right) {
        return new BinaryOpNode(operator, left, right);
    }

    public ProgramNode newAssignment(String variable, ProgramNode expression) {
        return new AssignmentNode(variable, expression);
    }

    public ProgramNode newReturn(ProgramNode expression) {
        return new ReturnNode(expression);
    }

    /** Adds a top-level statement to the program. */
    public void addStatement(ProgramNode statement) {
        root.add(statement);
    }

    public ProgramNode getRootNode() {
        return root;
    }
}
