package dev.kaldiroglu.dp.behavioral.observer.price.problem;

import java.util.ArrayList;
import java.util.List;

/** A reader of the price: draws every price it is given. */
public final class Chart {

    private final List<Integer> points = new ArrayList<>();

    public void add(int price) {
        points.add(price);
    }

    public List<Integer> points() {
        return List.copyOf(points);
    }
}
