package dev.kaldiroglu.dp.structural.composite.bom.solution;

/**
 * The same three questions at four levels of the tree. The client never tests whether it
 * holds a part or an assembly. {@code problem.RightAnswersMain} reaches the same totals.
 */
public class RollUpsMain {

    public static void main(String[] args) {
        ProductCatalog.Bicycle catalog = ProductCatalog.cityBicycle();

        System.out.println("=== Roll-ups: one call each, at any level ===");
        report(catalog.bicycle());
        report(catalog.wheel());
        report(catalog.hub());
        report(catalog.spoke()); // a leaf answers the same three questions
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
}
