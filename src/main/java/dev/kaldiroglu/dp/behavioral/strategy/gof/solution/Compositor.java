package dev.kaldiroglu.dp.behavioral.strategy.gof.solution;

import dev.kaldiroglu.dp.behavioral.strategy.gof.Component;
import dev.kaldiroglu.dp.behavioral.strategy.gof.Layout;

import java.util.List;

/**
 * The <b>Strategy</b>, in GoF's own words and their own name for it.
 * <p>
 * Design Patterns, p. 315: "We can avoid these problems by defining classes that encapsulate
 * different line breaking algorithms. An algorithm that's encapsulated in this way is called
 * a <i>strategy</i>."
 * <p>
 * One method. It is handed the components and the width and answers where the lines break —
 * it does not own the text, does not know what a document is, and cannot decide whether it
 * should be the algorithm in use.
 */
public interface Compositor {

    /** What this algorithm calls itself, for a test or a status bar to read back. */
    String name();

    /** Break the components into lines no wider than {@code lineWidth}. */
    Layout compose(List<Component> components, int lineWidth);
}
