package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution;

import java.util.List;

/**
 * The six claims and the chain that the solution's runnable classes use. Elif leads the
 * team (1,000), Burak manages (10,000), Cem directs (50,000), Deniz is the CFO (200,000).
 * Emre is an engineer.
 */
final class SampleChain {

    private SampleChain() {
    }

    static List<String[]> claims() {
        return List.of(
                new String[] {"Emre", "800", "books"},
                new String[] {"Emre", "5000", "a laptop"},
                new String[] {"Burak", "5000", "a conference"},
                new String[] {"Emre", "30000", "a server"},
                new String[] {"Cem", "30000", "a training course"},
                new String[] {"Emre", "500000", "a new office"});
    }

    /** The audit log first, then the four approvers. */
    static AuditLog build() {
        AuditLog audit = new AuditLog();
        audit.then(new Approver("Elif", 1_000))
                .then(new Approver("Burak", 10_000))
                .then(new Approver("Cem", 50_000))
                .then(new Approver("Deniz", 200_000));
        return audit;
    }
}
