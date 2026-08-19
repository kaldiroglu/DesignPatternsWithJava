package dev.kaldiroglu.dp.behavioral.strategy.gof.problem;

import dev.kaldiroglu.dp.behavioral.strategy.gof.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * GoF's example before the pattern: the text object breaks its own lines.
 * <p>
 * Design Patterns, p. 315: "There are many algorithms for breaking a stream of text into
 * lines. Hard-wiring all such algorithms into the classes that require them isn't
 * desirable for several reasons." This class is that hard-wiring, and the three reasons
 * GoF give are visible in it.
 *
 * <h2>The three, in GoF's words and in this file</h2>
 * <ul>
 *   <li><b>"Clients get more complex if they include the line breaking code."</b> The
 *       method below is a text object that also happens to be a typesetter. Read how much
 *       of it is about laying out lines and how little is about being a document.</li>
 *   <li><b>"Different algorithms will be appropriate at different times."</b> The
 *       {@code quality} flag is that sentence made into a parameter, and it is a branch
 *       that every future algorithm has to be threaded through.</li>
 *   <li><b>"It's difficult to add new algorithms or vary existing ones when line breaking
 *       is an integral part of a Composition."</b> A third algorithm is a third branch in
 *       a method that already works for two.</li>
 * </ul>
 */
public final class Composition {

    private final List<Component> components;
    private final int lineWidth;
    private final boolean quality;

    public Composition(List<Component> components, int lineWidth, boolean quality) {
        this.components = List.copyOf(components);
        this.lineWidth = lineWidth;
        this.quality = quality;
    }

    /**
     * Break the components into lines.
     * <p>
     * Two algorithms, one method, one boolean. The flag is the whole problem: a caller who
     * wants a third algorithm has nothing to pass, and the class cannot be extended to
     * accept one without editing this method.
     */
    public dev.kaldiroglu.dp.behavioral.strategy.gof.Layout repair() {
        return quality ? bestFit() : firstFit();
    }

    /** Fill each line until the next component will not fit. Fast, and leaves ragged gaps. */
    private dev.kaldiroglu.dp.behavioral.strategy.gof.Layout firstFit() {
        List<List<Component>> lines = new ArrayList<>();
        List<Component> line = new ArrayList<>();
        int used = 0;
        for (Component component : components) {
            int needed = line.isEmpty() ? component.width() : component.width() + 1;
            if (used + needed > lineWidth && !line.isEmpty() && line.getLast().breakable()) {
                lines.add(List.copyOf(line));
                line.clear();
                used = 0;
                needed = component.width();
            }
            line.add(component);
            used += needed;
        }
        lines.add(List.copyOf(line));
        return new dev.kaldiroglu.dp.behavioral.strategy.gof.Layout(lines, lineWidth);
    }

    /**
     * Look at the whole paragraph and even the lines out, which is TeX's idea.
     * <p>
     * Written out here in the same class as the fast one, which is exactly what GoF object
     * to: two unrelated algorithms sharing a file because one object needs both. Note that
     * the greedy pass below is the <em>same</em> code as {@link #firstFit()}, copied,
     * because this algorithm needs it as a subroutine and there is nowhere to put it.
     */
    private dev.kaldiroglu.dp.behavioral.strategy.gof.Layout bestFit() {
        int lines = greedy(lineWidth).size();
        int widest = components.stream().mapToInt(Component::width).max().orElse(1);
        int best = lineWidth;
        for (int width = widest; width <= lineWidth; width++) {
            if (greedy(width).size() <= lines) {
                best = width;
                break;
            }
        }
        return new dev.kaldiroglu.dp.behavioral.strategy.gof.Layout(greedy(best), lineWidth);
    }

    private List<List<Component>> greedy(int width) {
        List<List<Component>> lines = new ArrayList<>();
        List<Component> line = new ArrayList<>();
        int used = 0;
        for (Component component : components) {
            int needed = line.isEmpty() ? component.width() : component.width() + 1;
            if (used + needed > width && !line.isEmpty() && line.getLast().breakable()) {
                lines.add(List.copyOf(line));
                line.clear();
                used = 0;
                needed = component.width();
            }
            line.add(component);
            used += needed;
        }
        lines.add(List.copyOf(line));
        return lines;
    }
}
