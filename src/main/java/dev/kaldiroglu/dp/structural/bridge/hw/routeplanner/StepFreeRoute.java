package dev.kaldiroglu.dp.structural.bridge.hw.routeplanner;

/**
 * A RefinedAbstraction: fastest, but only among journeys a wheelchair can make.
 * <p>
 * Routes with steps are not filtered out, they are scored beyond reach — so if every
 * candidate has steps the planner still returns the best of a bad set rather than throwing.
 * Whether that is the right call is a good thing to argue about; it is a decision of the
 * abstraction, and no map provider has an opinion on it.
 */
public final class StepFreeRoute extends RoutePlanner {

    public StepFreeRoute(MapProvider maps) {
        super(maps);
    }

    @Override
    protected long score(Route route) {
        return route.stepFree() ? route.seconds() : Integer.MAX_VALUE + (long) route.seconds();
    }
}
