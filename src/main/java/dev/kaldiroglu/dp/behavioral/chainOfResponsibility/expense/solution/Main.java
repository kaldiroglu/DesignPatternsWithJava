package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution;

import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem.ApprovalService;
import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem.LimitTable;
import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem.TeamLead;

import java.util.List;

/**
 * Runs the same six expenses through the three stages and through the chain.
 * <p>
 * The promise: every expense is approved by someone with enough authority, and never by
 * the person who spent it. Elif leads the team (1,000), Burak manages (10,000), Cem
 * directs (50,000), Deniz is the CFO (200,000). Emre is an engineer.
 */
public final class Main {

    public static void main(String[] args) {
        List<String[]> claims = List.of(
                new String[] {"Emre", "800", "books"},
                new String[] {"Emre", "5000", "a laptop"},
                new String[] {"Burak", "5000", "a conference"},
                new String[] {"Emre", "30000", "a server"},
                new String[] {"Cem", "30000", "a training course"},
                new String[] {"Emre", "500000", "a new office"});

        ApprovalService service = new ApprovalService();
        TeamLead lead = new TeamLead();
        LimitTable table = new LimitTable()
                .add("Elif", 1_000).add("Burak", 10_000).add("Cem", 50_000).add("Deniz", 200_000);

        AuditLog audit = new AuditLog();
        audit.then(new Approver("Elif", 1_000))
                .then(new Approver("Burak", 10_000))
                .then(new Approver("Cem", 50_000))
                .then(new Approver("Deniz", 200_000));

        for (String[] claim : claims) {
            String submitter = claim[0];
            int amount = Integer.parseInt(claim[1]);
            dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem.Expense before = new dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem.Expense(submitter, amount, claim[2]);
            Expense after = new Expense(submitter, amount, claim[2]);
            System.out.println(submitter + ", " + amount + ", " + claim[2]);
            System.out.println("  stage one:   " + service.approve(before));
            System.out.println("  stage two:   " + lead.approve(before));
            System.out.println("  stage three: " + table.approve(before));
            System.out.println("  chain:       " + audit.handle(after));
        }
        System.out.println("The audit log saw " + audit.seen().size() + " expenses.");
    }
}
