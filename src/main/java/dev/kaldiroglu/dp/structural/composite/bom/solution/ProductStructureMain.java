package dev.kaldiroglu.dp.structural.composite.bom.solution;

/** The product structure as a tree. The client holds one type and asks for it once. */
public class ProductStructureMain {

    public static void main(String[] args) {
        Assembly bicycle = ProductCatalog.cityBicycle().bicycle();

        System.out.println("=== The product structure ===");
        System.out.print(bicycle.toTree());
    }
}
