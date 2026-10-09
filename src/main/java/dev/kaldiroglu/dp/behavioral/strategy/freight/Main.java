package dev.kaldiroglu.dp.behavioral.strategy.freight;

import java.util.List;
import java.util.Map;

/**
 * Quotes a pillow and a box of books with four carriers whose rate cards have nothing in
 * common.
 */
public class Main {

    public static void main(String[] args) {
        Shipment pillow = new Shipment("TR", "TR", 900, 50, 40, 30);
        Shipment books = new Shipment("TR", "TR", 4200, 25, 20, 10);

        CarrierBoard board = new CarrierBoard(
                new ByDesi("Yurtici", Money.of("38.00"), 1),
                new ByWeightBand("Aras", List.of(
                        new ByWeightBand.Band(1000, Money.of("45.00")),
                        new ByWeightBand.Band(5000, Money.of("70.00")),
                        new ByWeightBand.Band(10000, Money.of("110.00"))),
                        Money.of("190.00")),
                new ByZone("UPS", Map.of("TR", Money.of("60.00"), "DE", Money.of("240.00")),
                        Money.of("12.00"), 15),
                new FlatRate("Marketplace", Money.of("89.90")));

        System.out.println("The pillow: 900 g, but charged as " + pillow.chargeableGrams()
                + " g by volume");
        board.quoteAll(pillow).forEach(quote -> System.out.println("  " + quote));
        System.out.println("  Cheapest: " + board.cheapestFor(pillow).carrier());

        System.out.println("The books: " + books.chargeableGrams() + " g");
        board.quoteAll(books).forEach(quote -> System.out.println("  " + quote));
        System.out.println("  Cheapest: " + board.cheapestFor(books).carrier());
    }
}
