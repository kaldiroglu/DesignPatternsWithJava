package dev.kaldiroglu.dp.behavioral.strategy.gof.solution;

import dev.kaldiroglu.dp.behavioral.strategy.gof.Component;
import dev.kaldiroglu.dp.behavioral.strategy.gof.Layout;

import java.util.ArrayList;
import java.util.List;

/**
 * A <b>ConcreteStrategy</b>: GoF's {@code ArrayCompositor}, which "breaks the components
 * into lines at regular intervals... This is useful for breaking a collection of icons
 * into rows, for example" (p. 316).
 * <p>
 * This is the one worth stopping on. It ignores the line width entirely — it counts
 * components, not columns — and it is still a {@link Compositor}. An interface that had
 * been designed around "fit text to a width" could not have held it, and the fact that it
 * fits is the evidence that the interface describes an <em>algorithm</em> rather than a
 * variation on one.
 */
public final class ArrayCompositor implements Compositor {

    private final int perLine;

    public ArrayCompositor(int perLine) {
        if (perLine < 1) {
            throw new IllegalArgumentException("a row needs at least one component");
        }
        this.perLine = perLine;
    }

    @Override
    public String name() {
        return "ArrayCompositor(" + perLine + ")";
    }

    @Override
    public Layout compose(List<Component> components, int lineWidth) {
        List<List<Component>> lines = new ArrayList<>();
        List<Component> line = new ArrayList<>();
        for (Component component : components) {
            line.add(component);
            if (line.size() == perLine) {
                lines.add(List.copyOf(line));
                line.clear();
            }
        }
        if (!line.isEmpty()) {
            lines.add(List.copyOf(line));
        }
        return new Layout(lines, lineWidth);
    }
}
