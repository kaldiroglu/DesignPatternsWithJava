package dev.kaldiroglu.dp.structural.bridge.hw.routeplanner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * The Abstraction: how to choose between journeys.
 * <p>
 * It builds the candidates and asks each subclass to score them. The candidate-building is
 * shared, the preference is not — which is the split that makes three route kinds three small
 * classes rather than three copies of this method.
 */
public abstract class RoutePlanner {

    protected final MapProvider maps;

    protected RoutePlanner(MapProvider maps) {
        this.maps = Objects.requireNonNull(maps);
    }

    public final Route plan(String from, String to, List<String> hubs) {
        List<Route> candidates = new ArrayList<>();
        candidates.add(measure(List.of(from, to)));
        for (String hub : hubs) {
            candidates.add(measure(List.of(from, hub, to)));
        }
        return candidates.stream()
                .min(Comparator.comparingLong(this::score))
                .orElseThrow();
    }

    /** Lower is better. */
    protected abstract long score(Route route);

    private Route measure(List<String> stops) {
        int seconds = 0;
        int toll = 0;
        boolean stepFree = true;
        for (int i = 0; i < stops.size() - 1; i++) {
            String a = stops.get(i);
            String b = stops.get(i + 1);
            seconds += maps.travelSeconds(a, b);
            toll += maps.tollMinor(a, b);
            stepFree &= maps.stepFree(a, b);
        }
        return new Route(stops, seconds, toll, stepFree);
    }
}
