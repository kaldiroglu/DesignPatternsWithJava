package dev.kaldiroglu.dp.behavioral.visitor.gof.problem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Runs three statements through a syntax tree whose node classes carry every compiler job:
 * pretty-printing, type checking and code generation.
 */
public final class Main {

    public static void main(String[] args) {
        List<Node> program = List.of(
                new AssignmentNode("y", new ConstantNode(2)),
                new AssignmentNode("x", new AddNode(new VariableRefNode("y"), new ConstantNode(1))),
                new AssignmentNode("z", new AddNode(new VariableRefNode("w"), new VariableRefNode("x"))));
        Set<String> assigned = new HashSet<>();
        List<String> errors = new ArrayList<>();
        List<String> code = new ArrayList<>();
        for (Node statement : program) {
            System.out.println(statement.prettyPrint());
            statement.typeCheck(assigned, errors);
            statement.generateCode(code);
        }
        System.out.println("Errors: " + errors);
        System.out.println("Code:   " + code);
        System.out.println("Each of the three jobs is written in all four node classes.");
    }
}
