package dev.kaldiroglu.dp.structural.composite.hw.surveyform;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** The Composite: a titled group of questions and other sections. */
public final class Section implements FormElement {

    private final String title;
    private final List<FormElement> children = new ArrayList<>();

    public Section(String title) {
        this.title = Objects.requireNonNull(title);
    }

    @Override
    public String getTitle() {
        return title;
    }

    /** Concatenates rather than sums — the shape most real composite operations take. */
    @Override
    public List<String> validate() {
        List<String> problems = new ArrayList<>();
        for (FormElement child : children) {
            problems.addAll(child.validate());
        }
        return List.copyOf(problems);
    }

    @Override
    public int questionCount() {
        return children.stream().mapToInt(FormElement::questionCount).sum();
    }

    @Override
    public int answeredCount() {
        return children.stream().mapToInt(FormElement::answeredCount).sum();
    }

    @Override
    public String render(String indent) {
        StringBuilder out = new StringBuilder(indent + title);
        for (FormElement child : children) {
            out.append(System.lineSeparator()).append(child.render(indent + "    "));
        }
        return out.toString();
    }

    @Override
    public void add(FormElement element) {
        if (element == this) {
            throw new IllegalArgumentException("a section cannot contain itself");
        }
        children.add(element);
    }

    public Section with(FormElement... elements) {
        for (FormElement element : elements) {
            add(element);
        }
        return this;
    }
}
