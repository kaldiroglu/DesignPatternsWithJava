package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.Printed.codeOf;
import static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.Printed.countOf;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The three attempts of Part 1, on the six expenses the slides use. Elif leads the team
 * (1,000), Burak manages (10,000), Cem directs (50,000), Deniz is the CFO (200,000).
 */
class ProblemTest {

    private static final String SOURCE = "chainOfResponsibility/expense/problem/";

    /** The six expenses of expense.solution.Main, in its order. */
    static final List<Expense> SIX = List.of(
            new Expense("Emre", 800, "books"),
            new Expense("Emre", 5_000, "a laptop"),
            new Expense("Burak", 5_000, "a conference"),
            new Expense("Emre", 30_000, "a server"),
            new Expense("Cem", 30_000, "a training course"),
            new Expense("Emre", 500_000, "a new office"));

    /** What the rule says for the six expenses: never your own, never above your limit. */
    static final List<String> THE_RULE_SAYS = List.of(
            "approved by Elif",
            "approved by Burak",
            "approved by Cem",
            "approved by Cem",
            "approved by Deniz",
            "no one may approve it");

    private static LimitTable table() {
        return new LimitTable()
                .add("Elif", 1_000).add("Burak", 10_000).add("Cem", 50_000).add("Deniz", 200_000);
    }

    @Test
    @DisplayName("stage one keeps the rule for all six expenses")
    void stageOneIsCorrect() {
        ApprovalService service = new ApprovalService();
        assertEquals(THE_RULE_SAYS, SIX.stream().map(service::approve).toList());
    }

    @Test
    @DisplayName("stage one writes every approver's name in one method")
    void stageOneNamesEveryone() {
        String code = codeOf(SOURCE + "ApprovalService.java");
        for (String name : List.of("Elif", "Burak", "Cem", "Deniz")) {
            assertEquals(2, countOf(code, name), name + " is in the condition and in the answer");
        }
    }

    @Test
    @DisplayName("stage two keeps the rule for all six expenses")
    void stageTwoIsCorrect() {
        TeamLead lead = new TeamLead();
        assertEquals(THE_RULE_SAYS, SIX.stream().map(lead::approve).toList());
    }

    @Test
    @DisplayName("in stage two each approver creates the next one by class, so the order is fixed in code")
    void stageTwoFixesTheOrder() {
        assertEquals(1, countOf(codeOf(SOURCE + "TeamLead.java"), "new Manager()"));
        assertEquals(1, countOf(codeOf(SOURCE + "Manager.java"), "new Director()"));
        assertEquals(1, countOf(codeOf(SOURCE + "Director.java"), "new Cfo()"));
        assertEquals(0, countOf(codeOf(SOURCE + "Cfo.java"), "new "));
    }

    @Test
    @DisplayName("stage three has no names in its code: the names are rows added at startup")
    void stageThreeHasNoNames() {
        String code = codeOf(SOURCE + "LimitTable.java");
        for (String name : List.of("Elif", "Burak", "Cem", "Deniz")) {
            assertEquals(0, countOf(code, name), name);
        }
    }

    @Test
    @DisplayName("stage three chooses by amount alone: Burak approves his own 5,000 and Cem his own 30,000")
    void stageThreeBreaksTheRule() {
        LimitTable table = table();
        assertEquals(List.of(
                        "approved by Elif",
                        "approved by Burak",
                        "approved by Burak",      // Burak's own conference
                        "approved by Cem",
                        "approved by Cem",        // Cem's own training course
                        "no one may approve it"),
                SIX.stream().map(table::approve).toList());
    }

    @Test
    @DisplayName("5,000 is within the manager's limit of 10,000, and the manager is Burak")
    void buraksConference() {
        Expense conference = new Expense("Burak", 5_000, "a conference");
        assertEquals("approved by Burak", table().approve(conference));
        assertEquals("approved by Cem", new ApprovalService().approve(conference));
        assertEquals("approved by Cem", new TeamLead().approve(conference));
    }

    @Test
    @DisplayName("exactly two of the six expenses are approved by the person who spent the money in stage three")
    void twoPeopleApprovedTheirOwn() {
        LimitTable table = table();
        long own = SIX.stream()
                .filter(e -> table.approve(e).equals("approved by " + e.submitter()))
                .count();
        assertEquals(2, own);
    }
}
