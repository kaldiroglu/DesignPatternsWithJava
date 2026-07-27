package dev.kaldiroglu.dp.structural.composite.bom.problem;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;
import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

/**
 * Runs the naive design and shows what it costs.
 *
 * <p>The totals it prints are <b>correct</b> — this is not a demonstration of a
 * broken program. It is a demonstration of a program that is correct today and
 * expensive to keep correct. Run {@code solution.Demo} straight afterwards and
 * compare.</p>
 */
public class Main {


    public static void main(String[] args) {
        NaiveProductCatalog.Bicycle catalog = NaiveProductCatalog.cityBicycle();
        Assembly bicycle = catalog.bicycle();

        System.out.println("=== The naive design gets the right answers ===");
        System.out.println("Bicycle cost:   " + NaiveCosting.totalCost(bicycle));
        System.out.println("Bicycle weight: " + NaiveShipping.totalWeightGrams(bicycle) + " g");
        System.out.println("Bicycle parts:  " + NaiveCosting.partCount(bicycle));

        System.out.println();
        System.out.println("=== ...but every client re-implements the same walk ===");
        System.out.println("NaiveCosting.totalCost   — walk #1");
        System.out.println("NaiveCosting.partCount   — walk #2");
        System.out.println("NaiveShipping.totalWeightGrams — walk #3");
        System.out.println("Each one branches on instanceof and iterates BOTH child lists.");

        System.out.println();
        System.out.println("=== ...and pays for every duplicated list entry ===");
        System.out.printf("Child-list entries walked per query: %d%n", listEntries(bicycle));
        System.out.println("(the Composite version walks 13)");

        System.out.println();
        System.out.println("=== ...and cannot share a sub-assembly ===");
        System.out.println("Are the two wheels the same object? "
                + (catalog.wheel1() == catalog.wheel2()));
        Money wheel1Before = NaiveCosting.totalCost(catalog.wheel1());
        Money wheel2Before = NaiveCosting.totalCost(catalog.wheel2());
        System.out.println("Wheel 1 costs " + wheel1Before + ", wheel 2 costs " + wheel2Before);

        // An engineering change applied the way a developer naturally would:
        // to "the wheel" — of which there are secretly two.
        catalog.wheel1().addPart(new Part(Catalog.SPOKE), 4); // 32 -> 36 spokes
        System.out.println("Engineering change: 4 more spokes on the wheel...");
        System.out.println("Wheel 1 now costs " + NaiveCosting.totalCost(catalog.wheel1())
                + ", wheel 2 still costs " + NaiveCosting.totalCost(catalog.wheel2()));
        System.out.println("The two wheels have silently drifted apart, and the bicycle now "
                + "costs " + NaiveCosting.totalCost(bicycle)
                + " instead of the $399.20 it should.");

        System.out.println();
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

    /** Counts the child-list entries any full traversal has to walk. */
    private static int listEntries(Assembly assembly) {
        int entries = assembly.parts().size() + assembly.subAssemblies().size();
        for (Assembly sub : assembly.subAssemblies()) {
            entries += listEntries(sub);
        }
        return entries;
    }
}
