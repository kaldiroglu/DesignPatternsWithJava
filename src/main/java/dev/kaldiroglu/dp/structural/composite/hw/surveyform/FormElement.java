package dev.kaldiroglu.dp.structural.composite.hw.surveyform;

import java.util.List;

/**
 * The Component: a part of a form, whether it is one question or a whole section.
 * <p>
 * The operation to notice is {@link #validate()}. It returns a <em>collection</em> rather
 * than a number, which is the more common shape in real systems and slightly harder: a
 * composite has to concatenate what its children return instead of adding it up.
 * <p>
 * This package takes the <strong>transparent</strong> side of GoF's implementation issue 4 —
 * {@link #add} is declared here, on the Component, so every element looks alike and a client
 * never asks what it is holding. The price is on the next line down.
 */
public interface FormElement {

    String getTitle();

    /** Everything wrong with this element and anything inside it. Empty means valid. */
    List<String> validate();

    int questionCount();

    int answeredCount();

    String render(String indent);

    /**
     * Adds a child.
     *
     * @throws UnsupportedOperationException on a {@link Question}, which has no children.
     *         That is the cost of transparency: the mistake compiles and fails at run time.
     *         {@code composite.graphic} and {@code composite.fileSystem} make the other
     *         choice, so the three can be compared.
     */
    void add(FormElement element);
}
