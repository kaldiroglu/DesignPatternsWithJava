package dev.kaldiroglu.dp.structural.bridge.hw.routeplanner;

/**
 * The Implementor: what the routing rules are allowed to ask of a map.
 * <p>
 * Every primitive here is a measurement of one leg. None of them is a routing decision, and
 * none of them mentions a vendor. That is the property the exercise is checking: if a single
 * vendor type name reaches the abstraction, swapping vendors will touch the routing rules,
 * and the whole point of the pattern is lost.
 */
public interface MapProvider {

    String name();

    int travelSeconds(String from, String to);

    /** Tolls in minor units, so there is no rounding argument. */
    int tollMinor(String from, String to);

    boolean stepFree(String from, String to);
}
