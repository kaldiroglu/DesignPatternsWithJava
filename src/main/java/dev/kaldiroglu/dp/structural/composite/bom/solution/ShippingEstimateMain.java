package dev.kaldiroglu.dp.structural.composite.bom.solution;

/** A client that works for a bicycle and for a spoke, and never asks which it holds. */
public class ShippingEstimateMain {

    public static void main(String[] args) {
        ProductCatalog.Bicycle catalog = ProductCatalog.cityBicycle();
        // The estimate is for the bicycle after the engineering change: 36 spokes per wheel.
        catalog.wheel().changeQuantity(catalog.spoke(), 36);

        System.out.println("=== A client that does not know or care about the type ===");
        System.out.println("Shipping estimate for the bicycle: "
                + ShippingEstimate.of(catalog.bicycle()));
        System.out.println("Shipping estimate for a single spoke: "
                + ShippingEstimate.of(catalog.spoke()));
    }
}
