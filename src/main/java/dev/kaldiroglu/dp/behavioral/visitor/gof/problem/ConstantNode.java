package dev.kaldiroglu.dp.behavioral.visitor.gof.problem;

import java.util.List;
import java.util.Set;

/** A number written in the program. */
public record ConstantNode(int value) implements Node {

    @Override
    public void typeCheck(Set<String> assigned, List<String> errors) {
    }

    @Override
    public void generateCode(List<String> code) {
        code.add("PUSH " + value);
    }

    @Override
    public String prettyPrint() {
        return String.valueOf(value);
    }
}
