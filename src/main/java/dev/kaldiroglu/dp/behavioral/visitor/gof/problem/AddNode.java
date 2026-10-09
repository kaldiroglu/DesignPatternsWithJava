package dev.kaldiroglu.dp.behavioral.visitor.gof.problem;

import java.util.List;
import java.util.Set;

/** {@code left + right} */
public record AddNode(Node left, Node right) implements Node {

    @Override
    public void typeCheck(Set<String> assigned, List<String> errors) {
        left.typeCheck(assigned, errors);
        right.typeCheck(assigned, errors);
    }

    @Override
    public void generateCode(List<String> code) {
        left.generateCode(code);
        right.generateCode(code);
        code.add("ADD");
    }

    @Override
    public String prettyPrint() {
        return left.prettyPrint() + " + " + right.prettyPrint();
    }
}
