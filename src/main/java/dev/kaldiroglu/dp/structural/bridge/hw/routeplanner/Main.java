package dev.kaldiroglu.dp.structural.bridge.hw.routeplanner;

import java.util.List;

/**
 * Homework 3 — the map switch.
 * <p>
 * Three route kinds, two providers, and the deliverable is a diff: after the swap, not one
 * file on the abstraction side was touched.
 */
public class Main {

    private static final List<String> HUBS = List.of("Uskudar", "Sisli");

    public static void main(String[] args) {
        for (MapProvider maps : List.of(new InHouseMaps(), new VendorMaps())) {
            System.out.println("--- " + maps.name() + " ---");
            System.out.println("  fastest    " + new FastestRoute(maps).plan("Kadikoy", "Levent", HUBS).describe());
            System.out.println("  cheapest   " + new CheapestRoute(maps).plan("Kadikoy", "Levent", HUBS).describe());
            System.out.println("  step-free  " + new StepFreeRoute(maps).plan("Kadikoy", "Levent", HUBS).describe());
        }
        System.out.println();
        System.out.println("Different answers, identical routing code. The only line that changed");
        System.out.println("between the two blocks above is the one that chose a provider.");
    }
}
