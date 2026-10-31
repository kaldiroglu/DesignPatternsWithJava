package dev.kaldiroglu.dp.structural.decorator.gof.visual.solution;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.SampleText;

/** The Motivation example of GoF p. 175, by decorating: two classes, every combination. */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        System.out.println("--- solution: two decorators, composed ----------------------------------");
        VisualComponent text = new TextView(30, 4, SampleText.TEXT);
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
}
