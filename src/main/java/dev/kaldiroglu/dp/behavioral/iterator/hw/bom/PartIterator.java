package dev.kaldiroglu.dp.behavioral.iterator.hw.bom;

import dev.kaldiroglu.dp.structural.composite.bom.solution.BomComponent;
import dev.kaldiroglu.dp.structural.composite.bom.solution.BomLine;
import dev.kaldiroglu.dp.structural.composite.bom.solution.Part;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Homework 1: every part in a bill of materials, from the Composite deck's {@code bom}.
 * <p>
 * It walks the tree depth first and returns only {@link Part}s: assemblies are containers,
 * and services have nothing to pick from a shelf. Each part comes with the quantity the
 * whole product needs, multiplied along the path from the top.
 * <p>
 * The homework question was whether a part used in two places should come out twice. Here
 * it does: one {@link PartLine} for each place it is used. A purchasing report that wants
 * one line per part number adds them up afterwards. Both answers are defensible; the point
 * is to choose one and say so.
 */
public final class PartIterator implements Iterator<PartLine> {

    private record Step(BomComponent component, int quantity) { }

    private final Deque<Step> pending = new ArrayDeque<>();
    private PartLine next;

    public PartIterator(BomComponent root) {
        pending.push(new Step(root, 1));
        advance();
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public PartLine next() {
        if (next == null) {
            throw new NoSuchElementException("no more parts");
        }
        PartLine current = next;
        advance();
        return current;
    }

    /** Moves to the next part, opening assemblies on the way and skipping services. */
    private void advance() {
        next = null;
        while (next == null && !pending.isEmpty()) {
            Step step = pending.pop();
            if (step.component() instanceof Part part) {
                next = new PartLine(part, step.quantity());
            }
            var lines = step.component().lines();
            for (int i = lines.size() - 1; i >= 0; i--) {
                BomLine line = lines.get(i);
                pending.push(new Step(line.component(), step.quantity() * line.quantity()));
            }
        }
    }

    /** So that a bill of materials can be used in a for-each loop. */
    public static Iterable<PartLine> partsOf(BomComponent root) {
        return () -> new PartIterator(root);
    }
}
