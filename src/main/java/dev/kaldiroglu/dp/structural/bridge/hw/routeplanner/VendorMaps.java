package dev.kaldiroglu.dp.structural.bridge.hw.routeplanner;

import java.util.Map;

/**
 * A ConcreteImplementor: the vendor the company switched to.
 * <p>
 * Its numbers disagree with the in-house engine's — better traffic data, different toll
 * records, and a more careful survey of which stations have lifts. That disagreement is the
 * point: the routing rules above must produce a <em>different answer</em> without a
 * <em>different line of code</em>.
 */
public final class VendorMaps implements MapProvider {

    private static final Map<String, int[]> LEGS = Map.of(
            "Kadikoy>Levent",  new int[]{2100, 1500, 0},
            "Kadikoy>Uskudar", new int[]{540,     0, 1},
            "Uskudar>Levent",  new int[]{1080,  900, 0},   // the vendor knows about the steps
            "Kadikoy>Sisli",   new int[]{1500,  700, 1},
            "Sisli>Levent",    new int[]{1020,     0, 1});

    @Override
    public String name() {
        return "vendor";
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
