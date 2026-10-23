package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution;

/**
 * Sends the six expenses into the chain, with the audit log at the front. The audit log sees
 * every one, including the one nobody may approve.
 */
public final class AuditLogMain {

    public static void main(String[] args) {
        AuditLog audit = SampleChain.build();
        for (String[] claim : SampleChain.claims()) {
            audit.handle(new Expense(claim[0], Integer.parseInt(claim[1]), claim[2]));
        }
        System.out.println("The audit log saw " + audit.seen().size() + " expenses.");
    }
}
