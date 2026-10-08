package dev.kaldiroglu.dp.behavioral.iterator.hw.bom;

import dev.kaldiroglu.dp.structural.composite.bom.solution.Part;

/**
 * One part, and how many of it the whole product needs.
 * <p>
 * The quantity is multiplied down the tree: two wheels, each with thirty-six spokes, gives
 * one line of seventy-two spokes from the bicycle's point of view.
 */
public record PartLine(Part part, int quantity) {

    @Override
    public String toString() {
        return quantity + " x " + part.name();
    }
}
