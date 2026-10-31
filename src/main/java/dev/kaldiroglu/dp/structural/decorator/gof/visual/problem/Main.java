package dev.kaldiroglu.dp.structural.decorator.gof.visual.problem;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.SampleText;

/** The Motivation example of GoF p. 175, by subclassing: one class per combination. */
public final class Main {

    private static final String TEXT = SampleText.TEXT;

    private Main() {
    }

    public static void main(String[] args) {
        System.out.println("--- problem: one subclass per combination -------------------------------");
        show("new BorderedTextView(...)", TextView.render(new BorderedTextView(30, 4, TEXT)));
        show("new ScrolledTextView(...)", TextView.render(new ScrolledTextView(30, 4, TEXT)));
        show("new BorderedScrolledTextView(...)",
                TextView.render(new BorderedScrolledTextView(30, 4, TEXT)));
        show("new ScrolledBorderedTextView(...)",
                TextView.render(new ScrolledBorderedTextView(30, 4, TEXT)));
        System.out.println("""
                Four subclasses for two embellishments, and the scrollbar is written out
                three times. A third embellishment would need eleven more classes.""");
    }

    private static void show(String caption, String picture) {
        System.out.println("\n" + caption);
        System.out.println(picture);
    }
}
