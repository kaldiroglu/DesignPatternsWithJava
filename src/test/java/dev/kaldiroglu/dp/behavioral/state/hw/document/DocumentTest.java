package dev.kaldiroglu.dp.behavioral.state.hw.document;

import dev.kaldiroglu.dp.behavioral.state.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/** Homework 2: the document workflow, with the transitions in one central table. */
class DocumentTest {

    @Test
    @DisplayName("a draft cannot be approved: the table has no such line")
    void aDraftCannotBeApproved() {
        Document document = new Document(new Workflow());
        IllegalStateException refused = assertThrows(IllegalStateException.class,
                () -> document.apply(Action.APPROVE));
        assertEquals("cannot approve a draft document", refused.getMessage());
        assertEquals(Status.DRAFT, document.status());
    }

    @Test
    @DisplayName("the table holds four transitions and nothing else")
    void theTableHasFourLines() {
        Workflow workflow = new Workflow();
        int allowed = 0;
        for (Status from : Status.values()) {
            for (Action action : Action.values()) {
                if (workflow.after(from, action).isPresent()) {
                    allowed++;
                }
            }
        }
        assertEquals(4, allowed);
        assertEquals(Optional.of(Status.DRAFT), workflow.after(Status.IN_REVIEW, Action.REJECT));
    }

    @Test
    @DisplayName("the statuses hold no rules: Status declares no methods of its own")
    void theStatusesAreOnlyNames() {
        assertTrue(java.util.Arrays.stream(Status.class.getDeclaredMethods())
                .allMatch(m -> m.getName().equals("values") || m.getName().equals("valueOf")
                        || m.getName().startsWith("$")));
    }

    @Test
    @DisplayName("Main prints the refusal, then the document's way to ARCHIVED")
    void mainOutput() {
        assertEquals(List.of(
                "APPROVE -> refused: cannot approve a draft document",
                "SUBMIT -> IN_REVIEW",
                "REJECT -> DRAFT",
                "SUBMIT -> IN_REVIEW",
                "APPROVE -> PUBLISHED",
                "ARCHIVE -> ARCHIVED"),
                Printed.by(() -> Main.main(new String[0])));
    }
}
