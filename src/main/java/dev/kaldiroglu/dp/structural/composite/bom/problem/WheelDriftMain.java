package dev.kaldiroglu.dp.structural.composite.bom.problem;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;
import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

/** One wheel design stored twice: a change to one copy leaves the other as it was. */
public class WheelDriftMain {

    public static void main(String[] args) {
        NaiveProductCatalog.Bicycle catalog = NaiveProductCatalog.cityBicycle();
        Assembly bicycle = catalog.bicycle();

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
    }
}
