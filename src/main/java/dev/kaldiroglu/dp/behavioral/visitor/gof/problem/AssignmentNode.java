package dev.kaldiroglu.dp.behavioral.visitor.gof.problem;

import java.util.List;
import java.util.Set;

/** {@code variable = value} */
public record AssignmentNode(String variable, Node value) implements Node {

    @Override
    public void typeCheck(Set<String> assigned, List<String> errors) {
        value.typeCheck(assigned, errors);
        assigned.add(variable);
    }

    @Override
    public void generateCode(List<String> code) {
        value.generateCode(code);
        code.add("STORE " + variable);
    }

    @Override
    public String prettyPrint() {
        return variable + " = " + value.prettyPrint();
    }
}
