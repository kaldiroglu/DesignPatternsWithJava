package dev.kaldiroglu.dp.structural.composite.bom.problem;

/** The three copies of the same walk, by name. */
public class RepeatedWalksMain {

    public static void main(String[] args) {
        System.out.println("=== ...but every client re-implements the same walk ===");
        System.out.println("NaiveCosting.totalCost   — walk #1");
        System.out.println("NaiveCosting.partCount   — walk #2");
        System.out.println("NaiveShipping.totalWeightGrams — walk #3");
        System.out.println("Each one branches on instanceof and iterates BOTH child lists.");
    }
}
