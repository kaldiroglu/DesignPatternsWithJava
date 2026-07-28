package dev.kaldiroglu.dp.structural.composite.hw.surveyform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** An operation that returns a collection, and the bill for transparency. */
class SurveyFormTest {

    private static FormElement form() {
        return new Section("Course feedback").with(
                new Section("About you").with(
                        new Question("Name", false).answer("Bora"),
                        new Question("Role", true).answer("engineer"),
                        new Question("Years of experience", true)),
                new Section("The session").with(
                        new Question("Which pattern was clearest?", true).answer("Composite"),
                        new Section("Exercises").with(
                                new Question("Was the homework the right length?", true))));
    }

    @Test
    @DisplayName("counts roll up through every level")
    void counts() {
        assertEquals(5, form().questionCount());
        assertEquals(3, form().answeredCount());
    }

    @Test
    @DisplayName("validation concatenates instead of summing, and reaches the deepest section")
    void validationGathers() {
        List<String> problems = form().validate();

        assertEquals(2, problems.size());
        assertTrue(problems.contains("required and unanswered: Years of experience"));
        assertTrue(problems.contains("required and unanswered: Was the homework the right length?"),
                "three levels down");
    }

    @Test
    @DisplayName("an answered form has nothing to report")
    void validFormIsSilent() {
        FormElement section = new Section("s").with(new Question("q", true).answer("yes"));
        assertEquals(List.of(), section.validate());
    }

    @Test
    @DisplayName("add is on the Component, so the client never tests a type")
    void theTransparentVariant() {
        assertTrue(java.util.Arrays.stream(FormElement.class.getDeclaredMethods())
                .anyMatch(m -> m.getName().equals("add")));

        // ... and this is what it costs: the call compiles and fails at run time.
        UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class,
                () -> new Question("q", false).add(new Question("nested", false)));
        assertTrue(thrown.getMessage().contains("no children"));
    }

    @Test
    @DisplayName("a section cannot contain itself")
    void noCycles() {
        Section section = new Section("s");
        assertThrows(IllegalArgumentException.class, () -> section.add(section));
    }

    @Test
    @DisplayName("rendering marks answered, unanswered and required alike, at any depth")
    void rendering() {
        String rendered = form().render("");
        assertTrue(rendered.contains("[x]"));
        assertTrue(rendered.contains("[ ] *"));
        assertTrue(rendered.contains("            "), "depth shows as indentation");
    }
}
