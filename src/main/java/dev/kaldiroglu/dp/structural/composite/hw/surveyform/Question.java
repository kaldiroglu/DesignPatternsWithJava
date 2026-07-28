package dev.kaldiroglu.dp.structural.composite.hw.surveyform;

import java.util.List;
import java.util.Objects;

/** A Leaf: one question, which may or may not have been answered. */
public final class Question implements FormElement {

    private final String title;
    private final boolean required;
    private String answer;

    public Question(String title, boolean required) {
        this.title = Objects.requireNonNull(title);
        this.required = required;
    }

    public Question answer(String answer) {
        this.answer = answer;
        return this;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<String> validate() {
        boolean missing = required && (answer == null || answer.isBlank());
        return missing ? List.of("required and unanswered: " + title) : List.of();
    }

    @Override
    public int questionCount() {
        return 1;
    }

    @Override
    public int answeredCount() {
        return answer == null || answer.isBlank() ? 0 : 1;
    }

    @Override
    public String render(String indent) {
        String mark = answer == null || answer.isBlank() ? (required ? "[ ] *" : "[ ]  ") : "[x]  ";
        return indent + mark + " " + title;
    }

    /**
     * Refuses, because a question has no children.
     * <p>
     * GoF's answer to this, on p. 167: a default that fails is the price of declaring child
     * management on the Component, and the compensation is that the client never has to test
     * a type. Note that this refusal is a <em>run-time</em> failure — the call compiles.
     */
    @Override
    public void add(FormElement element) {
        throw new UnsupportedOperationException(
                "a question has no children: " + title);
    }
}
