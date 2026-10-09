package dev.kaldiroglu.dp.behavioral.visitor.gof.solution;

import java.util.List;

/**
 * Runs the same three statements through visitors. The nodes only accept a visitor; each
 * compiler job is one visitor class.
 */
public final class Main {

    public static void main(String[] args) {
        List<Node> program = List.of(
                new AssignmentNode("y", new ConstantNode(2)),
                new AssignmentNode("x", new AddNode(new VariableRefNode("y"), new ConstantNode(1))),
                new AssignmentNode("z", new AddNode(new VariableRefNode("w"), new VariableRefNode("x"))));
        TypeCheckingVisitor checker = new TypeCheckingVisitor();
        CodeGeneratingVisitor generator = new CodeGeneratingVisitor();
        for (Node statement : program) {
            PrettyPrintingVisitor printer = new PrettyPrintingVisitor();
            statement.accept(printer);
            System.out.println(printer.text());
            statement.accept(checker);
            statement.accept(generator);
        }
        System.out.println("Errors: " + checker.errors());
        System.out.println("Code:   " + generator.code());
    }
}
