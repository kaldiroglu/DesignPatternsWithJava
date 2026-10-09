package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.Printed.by;
import static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.Printed.codeOf;
import static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.Printed.countOf;
import static org.junit.jupiter.api.Assertions.*;

/** The same six expenses through the chain. Every figure on the Part 3 slides is asserted here. */
class SolutionTest {

    private static final List<Expense> SIX = List.of(
            new Expense("Emre", 800, "books"),
            new Expense("Emre", 5_000, "a laptop"),
            new Expense("Burak", 5_000, "a conference"),
            new Expense("Emre", 30_000, "a server"),
            new Expense("Cem", 30_000, "a training course"),
            new Expense("Emre", 500_000, "a new office"));

    /** The chain of expense.solution.Main: the audit log first, then the four approvers. */
    private static AuditLog chain() {
        AuditLog audit = new AuditLog();
        audit.then(new Approver("Elif", 1_000))
                .then(new Approver("Burak", 10_000))
                .then(new Approver("Cem", 50_000))
                .then(new Approver("Deniz", 200_000));
        return audit;
    }

    @Test
    @DisplayName("the chain gives the six answers the rule asks for")
    void theSixAnswers() {
        AuditLog chain = chain();
        assertEquals(List.of(
                        "approved by Elif",
                        "approved by Burak",
                        "approved by Cem",        // past Burak, who refuses his own
                        "approved by Cem",
                        "approved by Deniz",      // past Cem, who refuses his own
                        "no one may approve it"),
                SIX.stream().map(chain::handle).toList());
    }

    @Test
    @DisplayName("the audit log saw 6 expenses, including the one nobody could approve")
    void theAuditLogSawSix() {
        AuditLog chain = chain();
        SIX.forEach(chain::handle);
        assertEquals(6, chain.seen().size());
        assertEquals(SIX, chain.seen());
    }

    @Test
    @DisplayName("an expense of 500,000 passes every approver, and the end of the chain still answers")
    void theEndOfTheChainAnswers() {
        assertEquals("no one may approve it", chain().handle(new Expense("Emre", 500_000, "a new office")));
        assertEquals("no one may approve it", new Approver("Elif", 1_000).handle(new Expense("Emre", 2_000, "a desk")));
    }

    @Test
    @DisplayName("an approver checks both its limit and that the expense is not its own")
    void anApproverHasBothRules() {
        Approver burak = new Approver("Burak", 10_000);
        assertEquals("approved by Burak", burak.handle(new Expense("Emre", 10_000, "a laptop")));
        assertEquals("no one may approve it", burak.handle(new Expense("Emre", 10_001, "a laptop")));
        assertEquals("no one may approve it", burak.handle(new Expense("Burak", 5_000, "a conference")));
    }

    @Test
    @DisplayName("today the chain gives Burak's 800 to Elif: within her limit, and not her own")
    void buraksEightHundred() {
        assertEquals("approved by Elif", chain().handle(new Expense("Burak", 800, "a book")));
    }

    @Test
    @DisplayName("a finance check between Burak and Cem is one more link, and no approver changes")
    void aFinanceCheckIsOneMoreLink() {
        List<Expense> checked = new ArrayList<>();
        ExpenseHandler finance = new ExpenseHandler() {
            @Override
            protected String tryToHandle(Expense expense) {
                if (expense.amount() > 20_000) {
                    checked.add(expense);
                }
                return null;
            }
        };
        AuditLog audit = new AuditLog();
        audit.then(new Approver("Elif", 1_000))
                .then(new Approver("Burak", 10_000))
                .then(finance)
                .then(new Approver("Cem", 50_000))
                .then(new Approver("Deniz", 200_000));

        assertEquals(List.of("approved by Elif", "approved by Burak", "approved by Cem",
                        "approved by Cem", "approved by Deniz", "no one may approve it"),
                SIX.stream().map(audit::handle).toList());
        assertEquals(3, checked.size(), "the 30,000 server, the 30,000 course and the 500,000 office");
    }

    @Test
    @DisplayName("the submitter knows only the first link: no approver class names another")
    void noLinkNamesAnother() {
        String approver = codeOf("chainOfResponsibility/expense/solution/Approver.java");
        String handler = codeOf("chainOfResponsibility/expense/solution/ExpenseHandler.java");
        for (String name : List.of("Elif", "Burak", "Cem", "Deniz")) {
            assertEquals(0, countOf(approver, name), name);
            assertEquals(0, countOf(handler, name), name);
        }
        assertEquals(0, countOf(approver, "new "));
    }

    @Test
    @DisplayName("Main prints each design's answer for the six expenses, then that the audit log saw 6")
    void mainOutput() {
        List<String> lines = by(() -> Main.main(new String[0]));
        assertEquals(List.of(
                "Emre, 800, books",
                "  stage one:   approved by Elif",
                "  stage two:   approved by Elif",
                "  stage three: approved by Elif",
                "  chain:       approved by Elif",
                "Emre, 5000, a laptop",
                "  stage one:   approved by Burak",
                "  stage two:   approved by Burak",
                "  stage three: approved by Burak",
                "  chain:       approved by Burak",
                "Burak, 5000, a conference",
                "  stage one:   approved by Cem",
                "  stage two:   approved by Cem",
                "  stage three: approved by Burak",
                "  chain:       approved by Cem",
                "Emre, 30000, a server",
                "  stage one:   approved by Cem",
                "  stage two:   approved by Cem",
                "  stage three: approved by Cem",
                "  chain:       approved by Cem",
                "Cem, 30000, a training course",
                "  stage one:   approved by Deniz",
                "  stage two:   approved by Deniz",
                "  stage three: approved by Cem",
                "  chain:       approved by Deniz",
                "Emre, 500000, a new office",
                "  stage one:   no one may approve it",
                "  stage two:   no one may approve it",
                "  stage three: no one may approve it",
                "  chain:       no one may approve it",
                "The audit log saw 6 expenses."), lines);
    }
}
