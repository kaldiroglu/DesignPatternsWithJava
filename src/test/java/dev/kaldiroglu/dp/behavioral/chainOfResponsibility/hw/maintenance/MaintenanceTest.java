package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.maintenance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Homework 1: the maintenance queue knows only the first developer. */
class MaintenanceTest {

    @Test
    @DisplayName("each request goes to the developer suited for it, and a 30-day improvement reaches the project team")
    void eachRequestFindsItsDeveloper() {
        Developer first = new BugFixer("Bora");
        first.then(new UiDesigner("Ece"))
                .then(new FeatureDeveloper("Fatih"))
                .then(new ProjectTeam("the project team"));
        RequestQueue queue = new RequestQueue(first);
        queue.add(new Request(Request.Kind.BUG, "login fails", 1));
        queue.add(new Request(Request.Kind.UI_CHANGE, "new logo", 2));
        queue.add(new Request(Request.Kind.IMPROVEMENT, "export to PDF", 10));
        queue.add(new Request(Request.Kind.IMPROVEMENT, "new reports", 30));
        queue.add(new Request(Request.Kind.PROJECT, "mobile app", 120));

        assertEquals(List.of(
                "login fails -> Bora",
                "new logo -> Ece",
                "export to PDF -> Fatih",
                "new reports -> the project team",
                "mobile app -> the project team"), queue.processAll());
    }

    @Test
    @DisplayName("a request nobody takes goes back to the team lead")
    void nobodyTakesIt() {
        Developer first = new BugFixer("Bora");
        assertEquals("new logo -> nobody: back to the team lead",
                first.take(new Request(Request.Kind.UI_CHANGE, "new logo", 2)));
    }
}
