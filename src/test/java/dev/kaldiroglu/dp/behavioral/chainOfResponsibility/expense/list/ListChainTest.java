package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.list;

import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution.Expense;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.Printed.by;
import static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.Printed.codeOf;
import static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.Printed.countOf;
import static org.junit.jupiter.api.Assertions.*;

/** The chain kept as a list: the same answers as the chain with a next field, and no handler knows another. */
class ListChainTest {

    private static final List<Expense> SIX = List.of(
            new Expense("Emre", 800, "books"),
            new Expense("Emre", 5_000, "a laptop"),
            new Expense("Burak", 5_000, "a conference"),
            new Expense("Emre", 30_000, "a server"),
            new Expense("Cem", 30_000, "a training course"),
            new Expense("Emre", 500_000, "a new office"));

    private static ApprovalChain chain(AuditLog audit) {
        return new ApprovalChain()
                .then(audit)
                .then(new Approver("Elif", 1_000))
                .then(new Approver("Burak", 10_000))
                .then(new Approver("Cem", 50_000))
                .then(new Approver("Deniz", 200_000));
    }

    private static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution.AuditLog linkedChain() {
        var audit = new dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution.AuditLog();
        audit.then(new dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution.Approver("Elif", 1_000))
                .then(new dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution.Approver("Burak", 10_000))
                .then(new dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution.Approver("Cem", 50_000))
                .then(new dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution.Approver("Deniz", 200_000));
        return audit;
    }

    @Test
    @DisplayName("the list gives the same six answers as the chain with a next field")
    void sameAnswersAsTheLinkedChain() {
        ApprovalChain list = chain(new AuditLog());
        var linked = linkedChain();
        assertEquals(SIX.stream().map(linked::handle).toList(), SIX.stream().map(list::handle).toList());
        assertEquals("approved by Cem", list.handle(SIX.get(2)));
    }

    @Test
    @DisplayName("no handler has a field that holds another handler")
    void noHandlerKnowsAnother() {
        for (Class<?> handler : List.of(Approver.class, AuditLog.class)) {
            Field[] fields = handler.getDeclaredFields();
            assertTrue(Arrays.stream(fields).noneMatch(f -> Link.class.isAssignableFrom(f.getType())),
                    handler.getSimpleName());
        }
    }

    @Test
    @DisplayName("the passing is one loop in ApprovalChain")
    void theLoopDoesThePassing() {
        String code = codeOf("chainOfResponsibility/expense/list/ApprovalChain.java");
        assertEquals(1, countOf(code, "for (Link link : links)"));
        assertEquals(1, countOf(code, "link.tryToHandle(expense)"));
    }

    @Test
    @DisplayName("the order of the list is the order of the chain: with Cem first, Cem approves Emre's 800")
    void theListOrderIsTheChainOrder() {
        ApprovalChain cemFirst = new ApprovalChain()
                .then(new Approver("Cem", 50_000))
                .then(new Approver("Elif", 1_000));
        assertEquals("approved by Cem", cemFirst.handle(SIX.get(0)));
    }

    @Test
    @DisplayName("the audit log sees all six expenses")
    void theAuditLogSawSix() {
        AuditLog audit = new AuditLog();
        ApprovalChain chain = chain(audit);
        SIX.forEach(chain::handle);
        assertEquals(SIX, audit.seen());
    }

    @Test
    @DisplayName("ListChainMain prints the six answers")
    void mainOutput() {
        assertEquals(List.of(
                "Emre, 800, books: approved by Elif",
                "Emre, 5000, a laptop: approved by Burak",
                "Burak, 5000, a conference: approved by Cem",
                "Emre, 30000, a server: approved by Cem",
                "Cem, 30000, a training course: approved by Deniz",
                "Emre, 500000, a new office: no one may approve it"), by(() -> ListChainMain.main(new String[0])));
    }
}
