package dev.kaldiroglu.dp.structural.composite.bom.solution;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;
import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

/** A subcontracted operation: one new class, and no client edited. */
public class NewItemMain {

    public static void main(String[] args) {
        ProductCatalog.Bicycle catalog = ProductCatalog.cityBicycle();
        Assembly bicycle = catalog.bicycle();
        // The figures are for the bicycle after the engineering change: 36 spokes per wheel.
        catalog.wheel().changeQuantity(catalog.spoke(), 36);

        System.out.println("=== A new kind of item costs one class and no client edits ===");
        Service coating = new Service(Catalog.POWDER_COATING);
        Money costBeforeCoating = bicycle.totalCost();
        catalog.frame().add(coating);
        System.out.println("Added " + coating.name() + " (" + coating.totalCost()
                + ") to the frame.");
        System.out.println("Bicycle cost " + costBeforeCoating + " -> " + bicycle.totalCost()
                + ", weight unchanged at " + bicycle.totalWeightGrams()
                + " g, part count unchanged at " + bicycle.partCount() + ".");
        System.out.println("The tree printer and ShippingEstimate were never "
                + "told this class exists:");
        System.out.println("Shipping estimate is still " + ShippingEstimate.of(bicycle) + ".");
    }
}
