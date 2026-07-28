package dev.kaldiroglu.dp.structural.composite.hw.orgchart;

/**
 * Homework 1 — the org chart.
 * <p>
 * The roll-up is the easy half. The interesting half is what happens when somebody reports to
 * two managers, which is common in real companies and fatal to a tree.
 */
public class Main {

    public static void main(String[] args) {
        IndividualContributor ayse = new IndividualContributor("Ayse", "engineer", 90_000);
        IndividualContributor bora = new IndividualContributor("Bora", "engineer", 85_000);
        IndividualContributor cem = new IndividualContributor("Cem", "designer", 80_000);

        Manager engineering = new Manager("Deniz", "eng manager", 120_000).add(ayse).add(bora);
        Manager design = new Manager("Ece", "design manager", 115_000).add(cem);
        Manager chief = new Manager("Fatma", "CTO", 180_000).add(engineering).add(design);

        System.out.println(chief.render(""));
        System.out.println();
        System.out.printf("headcount  %d%n", chief.headcount());
        System.out.printf("total cost %d%n", chief.totalCost());
        System.out.printf("engineering only: %d people, %d%n",
                engineering.headcount(), engineering.totalCost());

        System.out.println("""

                Now the part a tree cannot express. Cem is a designer who also
                works permanently inside the engineering team, so he reports to
                both Ece and Deniz.""");

        engineering.add(cem);   // the same object, in two places

        System.out.printf("%nheadcount now          %d  <- Cem counted twice%n", chief.headcount());
        System.out.printf("distinct headcount     %d  <- the true number%n", chief.distinctHeadcount());
        System.out.printf("total cost now         %d  <- and his salary paid twice%n", chief.totalCost());

        System.out.println("""

                Nothing threw. Nothing warned. The structure is now a graph and
                every roll-up over it silently over-counts.

                A cycle is caught, because that one hangs the program:""");
        try {
            engineering.add(chief);
        } catch (IllegalArgumentException e) {
            System.out.println("  rejected: " + e.getMessage());
        }
    }
}
