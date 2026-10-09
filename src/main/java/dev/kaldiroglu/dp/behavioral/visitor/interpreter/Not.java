package dev.kaldiroglu.dp.behavioral.visitor.interpreter;

/** A nonterminal expression: the rule does not hold. */
public record Not(Rule rule) implements Rule {

    @Override
    public boolean interpret(Product product) {
        return !rule.interpret(product);
    }

    @Override
    public String describe() {
        return "not " + rule.describe();
    }
}
