package dev.kaldiroglu.dp.behavioral.iterator.hw.bom;

import dev.kaldiroglu.dp.structural.composite.bom.solution.ProductCatalog;

/**
 * Walks a city bicycle's bill of materials and lists the parts only, with quantities
 * multiplied down the tree.
 */
public class Main {

    public static void main(String[] args) {
        ProductCatalog.Bicycle bike = ProductCatalog.cityBicycle();
        bike.wheel().changeQuantity(bike.spoke(), 36);

        System.out.println("Parts of a city bicycle, with 36 spokes in each of its two wheels:");
        for (PartLine line : PartIterator.partsOf(bike.bicycle())) {
            System.out.println("  " + line);
        }
        System.out.println("Assemblies such as the wheel are walked through, not listed.");
    }
}
