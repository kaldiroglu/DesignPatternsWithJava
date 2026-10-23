package dev.kaldiroglu.dp.behavioral.visitor.gof;

import dev.kaldiroglu.dp.behavioral.visitor.Printed;
import dev.kaldiroglu.dp.behavioral.visitor.gof.solution.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GoF's compiler (Design Patterns, pp. 331-344), before and after the pattern, on the
 * program y = 2, x = y + 1, z = w + x.
 */
class CompilerTest {

    private static final List<String> CODE = List.of("PUSH 2", "STORE y", "LOAD y", "PUSH 1",
            "ADD", "STORE x", "LOAD w", "LOAD x", "ADD", "STORE z");

    private static List<Node> program() {
        return List.of(
                new AssignmentNode("y", new ConstantNode(2)),
                new AssignmentNode("x", new AddNode(new VariableRefNode("y"), new ConstantNode(1))),
                new AssignmentNode("z", new AddNode(new VariableRefNode("w"), new VariableRefNode("x"))));
    }

    private static List<dev.kaldiroglu.dp.behavioral.visitor.gof.problem.Node> programBefore() {
        return List.of(
                new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.AssignmentNode("y",
                        new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.ConstantNode(2)),
                new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.AssignmentNode("x",
                        new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.AddNode(
                                new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.VariableRefNode("y"),
                                new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.ConstantNode(1))),
                new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.AssignmentNode("z",
                        new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.AddNode(
                                new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.VariableRefNode("w"),
                                new dev.kaldiroglu.dp.behavioral.visitor.gof.problem.VariableRefNode("x"))));
    }

    @Test
    @DisplayName("the type checker reports that w is used before it is assigned")
    void typeChecking() {
        TypeCheckingVisitor checker = new TypeCheckingVisitor();
        program().forEach(statement -> statement.accept(checker));

        assertEquals(List.of("w is used before it is assigned"), checker.errors());
    }

    @Test
    @DisplayName("the code generator handles the children first, and the code ends LOAD w, LOAD x, ADD, STORE z")
    void codeGeneration() {
        CodeGeneratingVisitor generator = new CodeGeneratingVisitor();
        program().forEach(statement -> statement.accept(generator));

        assertEquals(CODE, generator.code());
        assertEquals(List.of("LOAD w", "LOAD x", "ADD", "STORE z"), CODE.subList(6, 10));
    }

    @Test
    @DisplayName("the pretty printer prints the program back")
    void prettyPrinting() {
        List<String> lines = new ArrayList<>();
        for (Node statement : program()) {
            PrettyPrintingVisitor printer = new PrettyPrintingVisitor();
            statement.accept(printer);
            lines.add(printer.text());
        }
        assertEquals(List.of("y = 2", "x = y + 1", "z = w + x"), lines);
    }

    @Test
    @DisplayName("before the pattern, the nodes give the same errors, code and text")
    void beforeThePatternTheSameResults() {
        Set<String> assigned = new HashSet<>();
        List<String> errors = new ArrayList<>();
        List<String> code = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        for (var statement : programBefore()) {
            lines.add(statement.prettyPrint());
            statement.typeCheck(assigned, errors);
            statement.generateCode(code);
        }

        assertEquals(List.of("y = 2", "x = y + 1", "z = w + x"), lines);
        assertEquals(List.of("w is used before it is assigned"), errors);
        assertEquals(CODE, code);
    }

    @Test
    @DisplayName("before the pattern, four node classes each carry all three jobs")
    void fourNodesThreeJobs() {
        List<Class<?>> nodes = List.of(
                dev.kaldiroglu.dp.behavioral.visitor.gof.problem.AssignmentNode.class,
                dev.kaldiroglu.dp.behavioral.visitor.gof.problem.VariableRefNode.class,
                dev.kaldiroglu.dp.behavioral.visitor.gof.problem.ConstantNode.class,
                dev.kaldiroglu.dp.behavioral.visitor.gof.problem.AddNode.class);
        assertEquals(3, dev.kaldiroglu.dp.behavioral.visitor.gof.problem.Node.class.getDeclaredMethods().length);
        for (Class<?> node : nodes) {
            List<String> jobs = Arrays.stream(node.getDeclaredMethods()).map(Method::getName)
                    .filter(List.of("typeCheck", "generateCode", "prettyPrint")::contains).toList();
            assertEquals(3, jobs.size(), node.getSimpleName());
        }
    }

    @Test
    @DisplayName("with the pattern, a node only accepts, and the visitor has one method per node class")
    void nodesOnlyAccept() {
        assertEquals(List.of("accept"),
                Arrays.stream(Node.class.getDeclaredMethods()).map(Method::getName).toList());
        assertEquals(List.of("visitAdd", "visitAssignment", "visitConstant", "visitVariableRef"),
                Arrays.stream(NodeVisitor.class.getDeclaredMethods()).map(Method::getName).sorted().toList());
    }

    @Test
    @DisplayName("gof.problem.Main prints the program, the error and the code, then the cost")
    void problemMainOutput() {
        List<String> lines = Printed.by(() ->
                dev.kaldiroglu.dp.behavioral.visitor.gof.problem.Main.main(new String[0]));

        List<String> expected = new ArrayList<>(BODY);
        expected.add("Each of the three jobs is written in all four node classes.");
        assertEquals(expected, lines);
    }

    @Test
    @DisplayName("gof.solution.Main prints the same program, error and code with visitors")
    void solutionMainOutput() {
        List<String> lines = Printed.by(() ->
                dev.kaldiroglu.dp.behavioral.visitor.gof.solution.Main.main(new String[0]));

        assertEquals(BODY, lines);
    }

    /** What both designs print for y = 2, x = y + 1, z = w + x. */
    private static final List<String> BODY = List.of("y = 2", "x = y + 1", "z = w + x",
            "Errors: [w is used before it is assigned]",
            "Code:   [PUSH 2, STORE y, LOAD y, PUSH 1, ADD, STORE x, LOAD w, LOAD x, ADD, STORE z]");
}
