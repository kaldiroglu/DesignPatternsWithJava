package dev.kaldiroglu.dp.structural.bridge.hw.statementrun;

/**
 * The Implementor: everything a document is allowed to ask of the thing it is rendered onto.
 * <p>
 * <strong>Every name here is deliberate.</strong> The obvious first draft of this interface
 * has {@code drawBox}, {@code setFont}, {@code newPage} and {@code margin} on it — and every
 * one of those is a question about <em>paper</em>. Hand that interface to a screen reader and
 * it cannot answer: a voice has no page, no font and no margin.
 * <p>
 * What survives the screen reader is the set of primitives that describe <em>meaning</em>
 * rather than ink. A heading is still a heading when it is spoken; a label-and-value pair is
 * still a label and a value. That is the test for whether something belongs on an Implementor,
 * and accessibility is the honest way to apply it, because the third medium is a real user
 * rather than a hypothetical platform.
 */
public interface Medium {

    void heading(int level, String text);

    void field(String label, String value);

    void row(String... cells);

    void total(String label, String amount);

    /** Everything rendered so far. */
    String output();
}
