package dev.kaldiroglu.dp.behavioral.strategy.gof.solution;

import dev.kaldiroglu.dp.behavioral.strategy.gof.Component;

/** Breaks one paragraph with three compositors, changed on the same composition while it runs. */
public class Main {

    public static void main(String[] args) {
        String text = "A document editor breaks a stream of text into lines "
                + "and there are many algorithms for it";
        Composition document = new Composition(26, new SimpleCompositor());
        for (String word : text.split(" ")) {
            document.insert(Component.word(word));
        }

        Compositor[] compositors = {new SimpleCompositor(), new TeXCompositor(), new ArrayCompositor(6)};
        for (Compositor compositor : compositors) {
            document.setCompositor(compositor);
            var layout = document.repair();
            System.out.println(document.compositorName() + ", " + layout.lineCount() + " lines:");
            layout.render().forEach(line -> System.out.println("  |" + line));
        }
        System.out.println("One composition, three algorithms, and the composition names none of them.");
    }
}
