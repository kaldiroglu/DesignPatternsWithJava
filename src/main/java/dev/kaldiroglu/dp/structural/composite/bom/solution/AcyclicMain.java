package dev.kaldiroglu.dp.structural.composite.bom.solution;

/** A wheel that contains the whole bicycle is refused when it is added. */
public class AcyclicMain {

    public static void main(String[] args) {
        ProductCatalog.Bicycle catalog = ProductCatalog.cityBicycle();

        System.out.println("=== The structure is kept acyclic ===");
        try {
            catalog.wheel().add(catalog.bicycle()); // a wheel that contains the whole bicycle
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected as expected: " + e.getMessage());
        }
    }
}
