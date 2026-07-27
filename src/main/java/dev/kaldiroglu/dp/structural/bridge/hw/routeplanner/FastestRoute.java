package dev.kaldiroglu.dp.structural.bridge.hw.routeplanner;

/** A RefinedAbstraction: time is all that matters. */
public final class FastestRoute extends RoutePlanner {

    public FastestRoute(MapProvider maps) {
        super(maps);
    }

    @Override
    protected long score(Route route) {
        return route.seconds();
    }
}
