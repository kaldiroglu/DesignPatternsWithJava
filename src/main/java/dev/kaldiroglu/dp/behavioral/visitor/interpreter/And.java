package dev.kaldiroglu.dp.behavioral.visitor.interpreter;

/** A nonterminal expression: both rules hold. */
public record And(Rule left, Rule right) implements Rule {

    @Override
    public boolean interpret(Product product) {
        return left.interpret(product) && right.interpret(product);
    }

    @Override
    public String describe() {
        return "(" + left.describe() + " and " + right.describe() + ")";
    }
}
