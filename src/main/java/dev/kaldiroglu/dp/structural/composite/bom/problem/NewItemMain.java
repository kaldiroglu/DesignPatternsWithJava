package dev.kaldiroglu.dp.structural.composite.bom.problem;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;

/** A subcontracted operation: the first new kind of item, and the walks throw. */
public class NewItemMain {

    public static void main(String[] args) {
        System.out.println("=== ...and breaks when a new kind of item arrives ===");
        Service coating = new Service(Catalog.POWDER_COATING);
        try {
            NaiveCosting.totalCost(coating);
        } catch (IllegalArgumentException e) {
            System.out.println("Adding a subcontracted operation: " + e.getMessage());
        }
        System.out.println("Every one of the three walks needs a new instanceof branch, "
                + "and Assembly needs a third collection.");
    }
}
