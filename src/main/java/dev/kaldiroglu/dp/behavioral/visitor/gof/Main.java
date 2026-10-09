package dev.kaldiroglu.dp.behavioral.visitor.gof;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Runs the same three statements through both designs. They print the same lines.
 * <pre>
 * y = 2
 * x = y + 1
 * z = w + x
 * </pre>
 */
public final class Main {

    public static void main(String[] args) {
        System.out.println("Before the pattern");
        List<dev.kaldiroglu.dp.behavioral.visitor.gof.problem.Node> before = List.of(
                new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.AssignmentNode("y", new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.ConstantNode(2)),
                new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.AssignmentNode("x", new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.AddNode(
                        new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.VariableRefNode("y"), new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.ConstantNode(1))),
                new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.AssignmentNode("z", new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.AddNode(
                        new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.VariableRefNode("w"), new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.VariableRefNode("x"))));
        Set<String> assigned = new HashSet<>();
        List<String> errors = new ArrayList<>();
        List<String> code = new ArrayList<>();
        for (dev.kaldiroglu.dp.behavioral.visitor.gof.problem.Node statement : before) {
            System.out.println("  " + statement.prettyPrint());
            statement.typeCheck(assigned, errors);
            statement.generateCode(code);
        }
        System.out.println("  errors: " + errors);
        System.out.println("  code:   " + code);

        System.out.println("With visitors");
        List<dev.kaldiroglu.dp.behavioral.visitor.gof.solution.Node> after = List.of(
                new dev.kaldiroglu.dp.behavioral.visitor.gof.solution.AssignmentNode("y", new dev.kaldiroglu.dp.behavioral.visitor.gof.solution.ConstantNode(2)),
                new dev.kaldiroglu.dp.behavioral.visitor.gof.solution.AssignmentNode("x", new dev.kaldiroglu.dp.behavioral.visitor.gof.solution.AddNode(
                        new dev.kaldiroglu.dp.behavioral.visitor.gof.solution.VariableRefNode("y"), new dev.kaldiroglu.dp.behavioral.visitor.gof.solution.ConstantNode(1))),
                new dev.kaldiroglu.dp.behavioral.visitor.gof.solution.AssignmentNode("z", new dev.kaldiroglu.dp.behavioral.visitor.gof.solution.AddNode(
                        new dev.kaldiroglu.dp.behavioral.visitor.gof.solution.VariableRefNode("w"), new dev.kaldiroglu.dp.behavioral.visitor.gof.solution.VariableRefNode("x"))));
        dev.kaldiroglu.dp.behavioral.visitor.gof.solution.TypeCheckingVisitor checker = new dev.kaldiroglu.dp.behavioral.visitor.gof.solution.TypeCheckingVisitor();
        dev.kaldiroglu.dp.behavioral.visitor.gof.solution.CodeGeneratingVisitor generator = new dev.kaldiroglu.dp.behavioral.visitor.gof.solution.CodeGeneratingVisitor();
        for (dev.kaldiroglu.dp.behavioral.visitor.gof.solution.Node statement : after) {
            dev.kaldiroglu.dp.behavioral.visitor.gof.solution.PrettyPrintingVisitor printer = new dev.kaldiroglu.dp.behavioral.visitor.gof.solution.PrettyPrintingVisitor();
            statement.accept(printer);
            System.out.println("  " + printer.text());
            statement.accept(checker);
            statement.accept(generator);
        }
        System.out.println("  errors: " + checker.errors());
        System.out.println("  code:   " + generator.code());
    }
}
