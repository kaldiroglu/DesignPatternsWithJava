package dev.kaldiroglu.dp.structural.composite.bom.problem;

/** What the missing quantity costs: one list entry per spoke, and two copies of the wheel. */
public class ListEntriesMain {

    public static void main(String[] args) {
        Assembly bicycle = NaiveProductCatalog.cityBicycle().bicycle();

        System.out.println("=== ...and pays for every duplicated list entry ===");
        System.out.printf("Child-list entries walked per query: %d%n", listEntries(bicycle));
        System.out.println("(the Composite version walks 13)");
    }

    /** Counts the child-list entries any full traversal has to walk. */
    private static int listEntries(Assembly assembly) {
        int entries = assembly.parts().size() + assembly.subAssemblies().size();
        for (Assembly sub : assembly.subAssemblies()) {
            entries += listEntries(sub);
        }
        return entries;
    }
}
