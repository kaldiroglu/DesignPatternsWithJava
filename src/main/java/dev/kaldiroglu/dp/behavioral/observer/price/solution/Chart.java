package dev.kaldiroglu.dp.behavioral.observer.price.solution;

import java.util.ArrayList;
import java.util.List;

/** A <b>ConcreteObserver</b>: draws every new price. */
public final class Chart implements PriceListener {

    private final List<Integer> points = new ArrayList<>();

    @Override
    public void priceChanged(PriceChange change) {
        points.add(change.newPrice());
    }

    public List<Integer> points() {
        return List.copyOf(points);
    }
}
