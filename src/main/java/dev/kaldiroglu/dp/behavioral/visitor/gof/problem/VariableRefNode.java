package dev.kaldiroglu.dp.behavioral.visitor.gof.problem;

import java.util.List;
import java.util.Set;

/** A use of a variable. */
public record VariableRefNode(String name) implements Node {

    @Override
    public void typeCheck(Set<String> assigned, List<String> errors) {
        if (!assigned.contains(name)) {
            errors.add(name + " is used before it is assigned");
        }
    }

    @Override
    public void generateCode(List<String> code) {
        code.add("LOAD " + name);
    }

    @Override
    public String prettyPrint() {
        return name;
    }
}
