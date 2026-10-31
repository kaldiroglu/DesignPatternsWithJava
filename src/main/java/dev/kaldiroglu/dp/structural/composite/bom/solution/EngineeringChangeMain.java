package dev.kaldiroglu.dp.structural.composite.bom.solution;

/** A change deep in the tree: one quantity, and every total above it follows. */
public class EngineeringChangeMain {

    public static void main(String[] args) {
        ProductCatalog.Bicycle catalog = ProductCatalog.cityBicycle();
        Assembly bicycle = catalog.bicycle();

        System.out.println("=== An engineering change deep in the tree ===");
        System.out.println("Before: bicycle costs " + bicycle.totalCost()
                + " and weighs " + bicycle.totalWeightGrams() + " g");
        catalog.wheel().changeQuantity(catalog.spoke(), 36); // 32 -> 36 spokes per wheel
        System.out.println("Change: 36 spokes per wheel instead of 32");
        System.out.println("After:  bicycle costs " + bicycle.totalCost()
                + " and weighs " + bicycle.totalWeightGrams() + " g");
        System.out.println("Both wheels changed, and the roll-up above them "
                + "was recomputed automatically.");
    }
}
