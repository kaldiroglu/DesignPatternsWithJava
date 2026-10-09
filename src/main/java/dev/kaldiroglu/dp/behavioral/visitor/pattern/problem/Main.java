package dev.kaldiroglu.dp.behavioral.visitor.pattern.problem;

import java.util.List;

/**
 * Shows the shape of the earlier syntax tree example. Its methods are empty, so running
 * them prints nothing; the full version is in {@code visitor.gof.problem}.
 */
public final class Main {

    public static void main(String[] args) {
        List<Node> nodes = List.of(new Assignment(), new VariableReference());
        for (Node node : nodes) {
            node.typeCheck();
            node.generateCode();
            node.prettyPrint();
            System.out.println(node.getClass().getSimpleName()
                    + " has typeCheck, generateCode and prettyPrint");
        }
        System.out.println("The methods of this version are empty: it shows only where the operations live.");
    }
}
