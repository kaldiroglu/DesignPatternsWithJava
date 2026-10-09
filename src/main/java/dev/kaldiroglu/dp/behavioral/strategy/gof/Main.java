package dev.kaldiroglu.dp.behavioral.strategy.gof;

import java.util.List;

/**
 * Shows the shared types on their own: words as components, and a layout that measures its
 * lines.
 */
public class Main {

    public static void main(String[] args) {
        List<Component> first = List.of(Component.word("A"), Component.word("document"),
                Component.word("editor"));
        List<Component> second = List.of(Component.word("breaks"), Component.word("lines"));
        Layout layout = new Layout(List.of(first, second), 20);

        System.out.println("A layout of " + layout.lineCount() + " lines in a 20-column measure:");
        for (int i = 0; i < layout.lineCount(); i++) {
            System.out.println("  '" + layout.render().get(i) + "' width " + layout.widthOf(i)
                    + ", slack " + layout.slackOn(i));
        }
        System.out.println("Worst slack, last line not counted: " + layout.worstSlack());
    }
}
