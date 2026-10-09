package dev.kaldiroglu.dp.behavioral.visitor.gof.problem;

import java.util.List;
import java.util.Set;

/**
 * GoF's motivation, before the pattern: a compiler's syntax tree where every node class
 * carries every operation the compiler runs on it.
 * <p>
 * Type checking, code generation and pretty-printing are three unrelated jobs, and each one
 * is spread over four classes. A fourth job — say, a metrics count — is an edit to every
 * node class.
 */
public interface Node {

    void typeCheck(Set<String> assigned, List<String> errors);

    void generateCode(List<String> code);

    String prettyPrint();
}
