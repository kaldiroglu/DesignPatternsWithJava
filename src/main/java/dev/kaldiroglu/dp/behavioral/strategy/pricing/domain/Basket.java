package dev.kaldiroglu.dp.behavioral.strategy.pricing.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * What the customer is holding at the till.
 * <p>
 * A basket knows what is in it and what that costs at shelf price. It knows nothing about
 * campaigns — which is the point, and the thing every naive design in {@code problem}
 * eventually breaks.
 */
public record Basket(List<Line> lines, Customer customer) {

    public Basket {
        lines = List.copyOf(lines);
    }

    public static Basket of(Customer customer, Line... lines) {
        return new Basket(List.of(lines), customer);
    }

    /** The shelf price of everything in the basket, before any campaign. */
    public Money listTotal() {
        Money total = Money.ZERO;
        for (Line line : lines) {
            total = total.plus(line.listTotal());
        }
        return total;
    }

    /** How many items are in the basket, counting quantities. */
    public int itemCount() {
        return lines.stream().mapToInt(Line::quantity).sum();
    }

    /** The lines in one category, cheapest first — what a "buy two, get one" rule needs. */
    public List<Line> inCategory(String category) {
        List<Line> found = new ArrayList<>();
        for (Line line : lines) {
            if (line.category().equals(category)) {
                found.add(line);
            }
        }
        found.sort((a, b) -> a.unitPrice().compareTo(b.unitPrice()));
        return List.copyOf(found);
    }
}
