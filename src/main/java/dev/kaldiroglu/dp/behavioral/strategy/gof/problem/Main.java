package dev.kaldiroglu.dp.behavioral.strategy.gof.problem;

import dev.kaldiroglu.dp.behavioral.strategy.gof.Component;

import java.util.ArrayList;
import java.util.List;

/** Breaks one paragraph with both algorithms that live inside the composition, chosen by a flag. */
public class Main {

    public static void main(String[] args) {
        String text = "A document editor breaks a stream of text into lines "
                + "and there are many algorithms for it";
        List<Component> words = new ArrayList<>();
        for (String word : text.split(" ")) {
            words.add(Component.word(word));
        }

        for (boolean quality : new boolean[] {false, true}) {
            var layout = new Composition(words, 26, quality).repair();
            System.out.println("quality = " + quality + ", worst slack " + layout.worstSlack() + ":");
            layout.render().forEach(line -> System.out.println("  |" + line));
        }
        System.out.println("Both algorithms are private methods of the composition.");
        System.out.println("A boolean set in the constructor chooses one.");
        System.out.println("A third algorithm does not fit in a boolean.");
    }
}
