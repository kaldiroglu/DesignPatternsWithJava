package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution;

/**
 * A <b>ConcreteHandler</b>: a person with a limit.
 * <p>
 * The approver decides by its own rule: the amount is within the limit, and the expense
 * is not its own. Both conditions are here, in the link, so the chain needs no table and
 * the client needs no names.
 */
public final class Approver extends ExpenseHandler {

    private final String name;
    private final int limit;

    public Approver(String name, int limit) {
        this.name = name;
        this.limit = limit;
    }

    @Override
    protected String tryToHandle(Expense expense) {
        if (expense.amount() <= limit && !expense.submitter().equals(name)) {
            return "approved by " + name;
        }
        return null;
    }
}
