package dev.kaldiroglu.dp.structural.bridge.hw.routeplanner;

import java.util.Map;

/** A ConcreteImplementor: the routing engine the company built itself. */
public final class InHouseMaps implements MapProvider {

    private static final Map<String, int[]> LEGS = Map.of(
            // from>to           seconds  toll  stepFree(1/0)
            "Kadikoy>Levent",  new int[]{2400, 1500, 0},
            "Kadikoy>Uskudar", new int[]{600,     0, 1},
            "Uskudar>Levent",  new int[]{1500,  900, 1},
            "Kadikoy>Sisli",   new int[]{1800,  700, 0},
            "Sisli>Levent",    new int[]{900,      0, 1});

    @Override
    public String name() {
        return "in-house";
    }

    @Override
    public int travelSeconds(String from, String to) {
        return leg(from, to)[0];
    }

    @Override
    public int tollMinor(String from, String to) {
        return leg(from, to)[1];
    }

    @Override
    public boolean stepFree(String from, String to) {
        return leg(from, to)[2] == 1;
    }

    private static int[] leg(String from, String to) {
        int[] found = LEGS.get(from + ">" + to);
        if (found == null) {
            throw new IllegalArgumentException("no leg " + from + " > " + to);
        }
        return found;
    }
}
