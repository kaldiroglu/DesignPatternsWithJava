package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.list;

import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution.Expense;

import java.util.ArrayList;
import java.util.List;

/**
 * The chain kept as a list. The order of the list is the order of the chain, and the loop
 * in {@link #handle} asks each handler in turn. No handler knows another one.
 * <p>
 * Each handler still decides by its own rule. That is the difference from
 * {@code problem.SkippingLimitTable}, whose loop walks names and makes every decision itself.
 */
public final class ApprovalChain {

    private final List<Link> links = new ArrayList<>();

    /** Adds a handler at the end of the chain and returns this chain. */
    public ApprovalChain then(Link link) {
        links.add(link);
        return this;
    }

    public String handle(Expense expense) {
        for (Link link : links) {
            String answer = link.tryToHandle(expense);
            if (answer != null) {
                return answer;
            }
        }
        return "no one may approve it";
    }
}
