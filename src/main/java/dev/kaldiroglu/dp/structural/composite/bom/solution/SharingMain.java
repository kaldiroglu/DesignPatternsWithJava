package dev.kaldiroglu.dp.structural.composite.bom.solution;

/** One wheel object used twice: the quantity is on the line, not on the wheel. */
public class SharingMain {

    public static void main(String[] args) {
        ProductCatalog.Bicycle catalog = ProductCatalog.cityBicycle();
        Assembly bicycle = catalog.bicycle();

        System.out.println("=== Sharing: two wheels, one object ===");
        BomComponent firstWheel = bicycle.lines().get(1).component();
        System.out.println("Line 2 of the bicycle requires "
                + bicycle.lines().get(1).quantity() + " x " + firstWheel.name());
        System.out.println("Is it the same object as the catalog's wheel? "
                + (firstWheel == catalog.wheel()));
    }
}
