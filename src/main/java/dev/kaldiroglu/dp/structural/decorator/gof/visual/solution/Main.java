package dev.kaldiroglu.dp.structural.decorator.gof.visual.solution;

/** The Motivation example of GoF p. 175, by decorating: two classes, every combination. */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        System.out.println("--- solution: two decorators, composed ----------------------------------");
        VisualComponent text = new TextView(5, 2, "hello there");
        show("new BorderDecorator(new ScrollDecorator(text))",
                VisualComponent.render(new BorderDecorator(new ScrollDecorator(text))));
        show("new ScrollDecorator(new BorderDecorator(text))",
                VisualComponent.render(new ScrollDecorator(new BorderDecorator(text))));
        show("new BorderDecorator(new BorderDecorator(text))",
                VisualComponent.render(new BorderDecorator(new BorderDecorator(text))));
        System.out.println("""

                Two decorator classes, three chains, three pictures.""");
    }

    private static void show(String caption, String picture) {
        System.out.println("\n" + caption);
        System.out.println(picture);
    }
}
