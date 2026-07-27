package dev.kaldiroglu.dp.structural.bridge.hw.routeplanner;

/** A RefinedAbstraction: tolls first, time only as a tie-break. */
public final class CheapestRoute extends RoutePlanner {

    public CheapestRoute(MapProvider maps) {
        super(maps);
    }

    @Override
    protected long score(Route route) {
        return route.tollMinor() * 100_000L + route.seconds();
    }
}
