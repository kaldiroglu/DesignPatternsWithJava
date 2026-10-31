package dev.kaldiroglu.dp.structural.composite.bom.problem;

/**
 * The totals of the naive design. They are <b>correct</b>: this design is correct today and
 * expensive to keep correct. Compare them with {@code solution.RollUpsMain}.
 */
public class RightAnswersMain {

    public static void main(String[] args) {
        Assembly bicycle = NaiveProductCatalog.cityBicycle().bicycle();

        System.out.println("=== The naive design gets the right answers ===");
        System.out.println("Bicycle cost:   " + NaiveCosting.totalCost(bicycle));
        System.out.println("Bicycle weight: " + NaiveShipping.totalWeightGrams(bicycle) + " g");
        System.out.println("Bicycle parts:  " + NaiveCosting.partCount(bicycle));
    }
}
