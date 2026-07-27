package dev.kaldiroglu.dp.structural.decorator.gof.visual;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.BorderedScrolledTextView;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.BorderedTextView;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.ScrolledBorderedTextView;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.ScrolledTextView;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.BorderDecorator;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.ScrollDecorator;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.TextView;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.VisualComponent;

/**
 * The Motivation example of GoF p. 175, run both ways: first by subclassing, then by
 * decorating. The pictures are identical; the designs are not.
 */
public final class Demo {

    private static final String TEXT = "A decorator conforms to the interface of the component it decorates.";

    private Demo() {
    }

    public static void run() {
        System.out.println("=".repeat(72));
        System.out.println("GoF Motivation (p. 175) — a TextView with a border and a scrollbar");
        System.out.println("=".repeat(72));

        System.out.println("\n--- problem: one subclass per combination -------------------------------");
        show("new BorderedTextView(...)",
                dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.TextView.render(
                        new BorderedTextView(30, 4, TEXT)));
        show("new ScrolledTextView(...)",
                dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.TextView.render(
                        new ScrolledTextView(30, 4, TEXT)));
        show("new BorderedScrolledTextView(...)",
                dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.TextView.render(
                        new BorderedScrolledTextView(30, 4, TEXT)));
        show("new ScrolledBorderedTextView(...)",
                dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.TextView.render(
                        new ScrolledBorderedTextView(30, 4, TEXT)));
        System.out.println("""
                Four subclasses for two embellishments, and the scrollbar is written out
                three times. A third embellishment would need eleven more classes.""");

        System.out.println("\n--- solution: two decorators, composed ----------------------------------");
        VisualComponent text = new TextView(30, 4, TEXT);
        show("new BorderDecorator(text)",
                VisualComponent.render(new BorderDecorator(text)));
        show("new ScrollDecorator(text)",
                VisualComponent.render(new ScrollDecorator(text)));
        show("new BorderDecorator(new ScrollDecorator(text))",
                VisualComponent.render(new BorderDecorator(new ScrollDecorator(text))));
        show("new ScrollDecorator(new BorderDecorator(text))",
                VisualComponent.render(new ScrollDecorator(new BorderDecorator(text))));
        show("new BorderDecorator(new BorderDecorator(text))  — GoF: a property, twice",
                VisualComponent.render(new BorderDecorator(new BorderDecorator(text))));
        System.out.println("""
                One border class, one scrollbar class, and every combination — including
                combinations nobody anticipated — is available at run time.""");
    }

    private static void show(String caption, String picture) {
        System.out.println("\n" + caption);
        System.out.println(picture);
    }

    public static void main(String[] args) {
        run();
    }
}
