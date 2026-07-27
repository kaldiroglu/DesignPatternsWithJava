package dev.kaldiroglu.dp.structural.facade.gof;

import java.util.ArrayList;
import java.util.List;

/**
 * Subsystem class (GoF, p. 185): a node of the parse tree.
 *
 * <p>GoF gives {@code ProgramNode} subclasses such as {@code StatementNode} and
 * {@code ExpressionNode}. Each node knows how to {@link #traverse(CodeGenerator)}
 * itself — i.e., drive the {@link CodeGenerator} to emit code for it. The
 * concrete node types below are package-private: only the {@link ProgramNodeBuilder}
 * creates them, and only the code generator visits them, so clients of the
 * {@link Compiler} facade never see them.</p>
 */
public abstract class ProgramNode {

    /** Emits code for this node (and its children) via the given generator. */
    public abstract void traverse(CodeGenerator generator);
}

/** Root node: an ordered list of statements. */
class BlockNode extends ProgramNode {
    private final List<ProgramNode> statements = new ArrayList<>();

    void add(ProgramNode statement) {
        statements.add(statement);
    }

    @Override
    public void traverse(CodeGenerator generator) {
        for (ProgramNode statement : statements) {
            statement.traverse(generator);
        }
    }
}

/** Expression: an integer constant. */
class ConstantNode extends ProgramNode {
    private final int value;

    ConstantNode(int value) {
        this.value = value;
    }

    @Override
    public void traverse(CodeGenerator generator) {
        generator.emitPushConstant(value);
    }
}

/** Expression: a reference to a variable. */
class VariableRefNode extends ProgramNode {
    private final String name;

    VariableRefNode(String name) {
        this.name = name;
    }

    @Override
    public void traverse(CodeGenerator generator) {
        generator.emitLoad(name);
    }
}

/** Expression: a binary {@code +} or {@code -} operation. */
class BinaryOpNode extends ProgramNode {
    private final char operator;
    private final ProgramNode left;
    private final ProgramNode right;

    BinaryOpNode(char operator, ProgramNode left, ProgramNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public void traverse(CodeGenerator generator) {
        left.traverse(generator);
        right.traverse(generator);
        if (operator == '+') {
            generator.emitAdd();
        } else {
            generator.emitSubtract();
        }
    }
}

/** Statement: assign the value of an expression to a variable. */
class AssignmentNode extends ProgramNode {
    private final String variable;
    private final ProgramNode expression;

    AssignmentNode(String variable, ProgramNode expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public void traverse(CodeGenerator generator) {
        expression.traverse(generator);
        generator.emitStore(variable);
    }
}

/** Statement: return the value of an expression. */
class ReturnNode extends ProgramNode {
    private final ProgramNode expression;

    ReturnNode(ProgramNode expression) {
        this.expression = expression;
    }

    @Override
    public void traverse(CodeGenerator generator) {
        expression.traverse(generator);
        generator.emitReturn();
    }
}
