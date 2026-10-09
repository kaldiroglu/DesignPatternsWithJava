package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem;

/**
 * Stage one: one method decides who approves.
 * <p>
 * It is correct: nobody approves above their limit, and nobody approves their own expense.
 * But every approver's name and limit is written here, and every rule is one more
 * condition in this method. A new level, or a person on leave, is an edit to this class.
 */
public final class ApprovalService {

    public String approve(Expense expense) {
        int amount = expense.amount();
        String submitter = expense.submitter();
        if (amount <= 1_000 && !submitter.equals("Elif")) {
            return "approved by Elif";
        } else if (amount <= 10_000 && !submitter.equals("Burak")) {
            return "approved by Burak";
        } else if (amount <= 50_000 && !submitter.equals("Cem")) {
            return "approved by Cem";
        } else if (amount <= 200_000 && !submitter.equals("Deniz")) {
            return "approved by Deniz";
        } else {
            return "no one may approve it";
        }
    }
}
