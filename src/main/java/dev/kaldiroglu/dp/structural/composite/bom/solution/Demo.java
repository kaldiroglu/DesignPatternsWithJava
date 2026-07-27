package dev.kaldiroglu.dp.structural.composite.bom.solution;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;
import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

/**
 * Client of the bill-of-materials Composite.
 *
 * <p>Every section below is a question the business asks, answered by one call
 * against {@link BomComponent}. Nowhere does this client test whether it is
 * holding a part or an assembly.</p>
 *
 * <p>Run {@code problem.Demo} first. It builds the same bicycle from the same
 * figures and reaches the same totals — the difference this demo is meant to show
 * is not in the answers, it is in what the code had to do to get them.</p>
 */
public final class Demo {

    private Demo() {
    }

    public static void main(String[] args) {
        ProductCatalog.Bicycle catalog = ProductCatalog.cityBicycle();
        Assembly bicycle = catalog.bicycle();
        Assembly wheel = catalog.wheel();

        System.out.println("=== The product structure ===");
        System.out.print(bicycle.toTree());

        System.out.println();
        System.out.println("=== Roll-ups: one call each, at any level ===");
        report(bicycle);
        report(wheel);
        report(catalog.hub());
        report(catalog.spoke()); // a leaf answers the same three questions

        System.out.println();
        System.out.println("=== Sharing: two wheels, one object ===");
        BomComponent firstWheel = bicycle.lines().get(1).component();
        System.out.println("Line 2 of the bicycle requires "
                + bicycle.lines().get(1).quantity() + " x " + firstWheel.name());
        System.out.println("Is it the same object as the catalog's wheel? "
                + (firstWheel == wheel));

        System.out.println();
        System.out.println("=== An engineering change deep in the tree ===");
        System.out.println("Before: bicycle costs " + bicycle.totalCost()
                + " and weighs " + bicycle.totalWeightGrams() + " g");
        wheel.changeQuantity(catalog.spoke(), 36); // 32 -> 36 spokes per wheel
        System.out.println("Change: 36 spokes per wheel instead of 32");
        System.out.println("After:  bicycle costs " + bicycle.totalCost()
                + " and weighs " + bicycle.totalWeightGrams() + " g");
        System.out.println("Both wheels changed, and the roll-up above them "
                + "was recomputed automatically.");

        System.out.println();
        System.out.println("=== A client that does not know or care about the type ===");
        System.out.println("Shipping estimate for the bicycle: "
                + shippingEstimate(bicycle));
        System.out.println("Shipping estimate for a single spoke: "
                + shippingEstimate(catalog.spoke()));

        System.out.println();
        System.out.println("=== A new kind of item costs one class and no client edits ===");
        Service coating = new Service(Catalog.POWDER_COATING);
        Money costBeforeCoating = bicycle.totalCost();
        catalog.frame().add(coating);
        System.out.println("Added " + coating.name() + " (" + coating.totalCost()
                + ") to the frame.");
        System.out.println("Bicycle cost " + costBeforeCoating + " -> " + bicycle.totalCost()
                + ", weight unchanged at " + bicycle.totalWeightGrams()
                + " g, part count unchanged at " + bicycle.partCount() + ".");
        System.out.println("The tree printer and the shipping estimator below were never "
                + "told this class exists:");
        System.out.println("Shipping estimate is still " + shippingEstimate(bicycle) + ".");

        System.out.println();
        System.out.println("=== The structure is kept acyclic ===");
        try {
            wheel.add(bicycle); // a wheel that contains the whole bicycle
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected as expected: " + e.getMessage());
        }
    }

    /** Prints the three roll-ups for any component whatsoever. */
    private static void report(BomComponent component) {
        System.out.printf("%-22s %-18s cost %9s   %6d g   %3d part(s)%n",
                component.name(),
                component.isAssembly() ? "(assembly)" : "(purchased part)",
                component.totalCost(),
                component.totalWeightGrams(),
                component.partCount());
    }

    /**
     * A second, unrelated client. It was written against {@link BomComponent}
     * and therefore works for a whole bicycle and for a single spoke, with no
     * changes and no type tests.
     */
    private static Money shippingEstimate(BomComponent component) {
        int kilos = Math.max(1, (component.totalWeightGrams() + 999) / 1000);
        return Money.of(4.90).times(kilos);
    }
}
