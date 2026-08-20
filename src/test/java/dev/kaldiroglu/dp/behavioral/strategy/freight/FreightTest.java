package dev.kaldiroglu.dp.behavioral.strategy.freight;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * Four carriers, four ways of arriving at a price, and one shipment they all disagree
 * about. Every figure the slides quote is measured here.
 */
class FreightTest {

    /** A large light parcel: light on the scale, expensive on the van. */
    private static final Shipment PILLOW =
            new Shipment("TR", "TR", 900, 50, 40, 30);

    /** A small heavy one: the opposite trade. */
    private static final Shipment BOOKS =
            new Shipment("TR", "TR", 4200, 25, 20, 10);

    private static RateCard desi() {
        return new ByDesi("Yurtici", Money.of("38.00"), 1);
    }

    private static RateCard bands() {
        return new ByWeightBand("Aras", List.of(
                new ByWeightBand.Band(1000, Money.of("45.00")),
                new ByWeightBand.Band(5000, Money.of("70.00")),
                new ByWeightBand.Band(10000, Money.of("110.00"))),
                Money.of("190.00"));
    }

    private static ByZone zones() {
        return new ByZone("UPS",
                Map.of("TR", Money.of("60.00"), "DE", Money.of("240.00")),
                Money.of("12.00"), 15);
    }

    private static RateCard flat() {
        return new FlatRate("Marketplace", Money.of("89.90"));
    }

    private static CarrierBoard board() {
        return new CarrierBoard(desi(), bands(), zones(), flat());
    }

    @Test
    @DisplayName("volume, not weight: a pillow is charged as twenty kilos")
    void desiIsAboutSpace() {
        // 50 x 40 x 30 is 60,000 cm3, which is 20 desi — so 20 kg chargeable against 0.9 kg
        // actual. That gap is the whole reason the domestic carriers rate this way.
        assertEquals(60_000, PILLOW.volumeCm3());
        assertEquals(20_000, PILLOW.desiGrams());
        assertEquals(20_000, PILLOW.chargeableGrams());
        assertEquals(Money.of("760.00"), desi().quote(PILLOW));   // 20 x 38.00
    }

    @Test
    @DisplayName("the same card charges the heavy parcel by its weight instead")
    void desiFallsBackToWeight() {
        // 25 x 20 x 10 is 5,000 cm3 — under two desi — so the 4.2 kg on the scale wins.
        assertEquals(1_666, BOOKS.desiGrams());
        assertEquals(4_200, BOOKS.chargeableGrams());
        assertEquals(Money.of("190.00"), desi().quote(BOOKS));    // 5 x 38.00, rounded up
    }

    @Test
    @DisplayName("a band table steps: one gram over an edge costs a whole band more")
    void bandsAreALookupNotARate() {
        assertEquals(Money.of("70.00"), bands().quote(BOOKS));    // 4.2 kg, the 5 kg band

        Shipment justOver = new Shipment("TR", "TR", 5_001, 10, 10, 10);
        assertEquals(Money.of("70.00"), bands().quote(new Shipment("TR", "TR", 5_000, 10, 10, 10)));
        assertEquals(Money.of("110.00"), bands().quote(justOver));

        // A rate per kilo cannot express that, which is why the interface is a method.
        assertNotEquals(bands().quote(justOver), bands().quote(BOOKS));
    }

    @Test
    @DisplayName("a zone card prices the route first and the parcel second")
    void zonesAddASurcharge() {
        // 60.00 base + 5 kg x 12.00 = 120.00, then 15% fuel = 138.00
        assertEquals(Money.of("138.00"), zones().quote(BOOKS));
    }

    @Test
    @DisplayName("the fuel feed moves one card, and nothing else")
    void theSurchargeIsOneCardsBusiness() {
        ByZone ups = zones();
        Money before = ups.quote(BOOKS);

        ups.setFuelSurchargePercent(30);

        assertEquals(Money.of("138.00"), before);
        assertEquals(Money.of("156.00"), ups.quote(BOOKS));       // 120.00 + 30%
        assertEquals(Money.of("190.00"), desi().quote(BOOKS));    // untouched
    }

    @Test
    @DisplayName("a card may ignore the shipment entirely and still be a card")
    void flatRateIgnoresEverything() {
        assertEquals(flat().quote(PILLOW), flat().quote(BOOKS));
        assertEquals(Money.of("89.90"), flat().quote(PILLOW));
    }

    @Test
    @DisplayName("one desk, four carriers, and the cheapest depends on the parcel")
    void theCheapestIsNotAlwaysTheSameCarrier() {
        List<Quote> forPillow = board().quoteAll(PILLOW);
        List<Quote> forBooks = board().quoteAll(BOOKS);

        assertEquals(4, forPillow.size());
        assertEquals(List.of("Yurtici", "Aras", "UPS", "Marketplace"),
                forPillow.stream().map(Quote::carrier).toList());

        // Both parcels priced by all four. Every figure here is on a slide.
        assertEquals(List.of(Money.of("760.00"), Money.of("190.00"),
                        Money.of("345.00"), Money.of("89.90")),
                forPillow.stream().map(Quote::price).toList());
        assertEquals(List.of(Money.of("190.00"), Money.of("70.00"),
                        Money.of("138.00"), Money.of("89.90")),
                forBooks.stream().map(Quote::price).toList());

        // The flat rate wins the big light parcel by a mile; the band table wins the small
        // heavy one. That is the point of holding four cards rather than picking one.
        assertEquals("Marketplace", board().cheapestFor(PILLOW).carrier());
        assertEquals("Aras", board().cheapestFor(BOOKS).carrier());
        assertNotEquals(board().cheapestFor(PILLOW).carrier(),
                board().cheapestFor(BOOKS).carrier());
    }

    @Test
    @DisplayName("a fifth carrier is one class, and the board is the only edit")
    void addingACarrier() {
        RateCard bike = new RateCard() {
            @Override
            public String carrier() {
                return "Kurye";
            }

            @Override
            public Money quote(Shipment shipment) {
                return shipment.isDomestic() && shipment.chargeableGrams() <= 5_000
                        ? Money.of("35.00")
                        : Money.of("999.00");
            }
        };

        CarrierBoard board = board().add(bike);

        assertEquals(5, board.size());
        assertEquals("Kurye", board.cheapestFor(BOOKS).carrier());
        assertEquals(Money.of("35.00"), board.cheapestFor(BOOKS).price());
    }

    @Test
    @DisplayName("a carrier that does not serve the destination says so")
    void unservedZonesAreRejected() {
        Shipment toJapan = new Shipment("TR", "JP", 2_000, 20, 20, 20);
        assertThrows(IllegalArgumentException.class, () -> zones().quote(toJapan));
    }

    @Test
    @DisplayName("the context holds a card and never asks what kind it is")
    void theDeskOnlyForwards() {
        ShippingDesk desk = new ShippingDesk(desi());
        assertEquals("Yurtici", desk.carrier());

        desk.setCard(flat());                                     // the same desk

        assertEquals("Marketplace", desk.carrier());
        assertEquals(Money.of("89.90"), desk.book(PILLOW).price());
        assertTrue(java.util.Arrays.stream(ShippingDesk.class.getDeclaredFields())
                .allMatch(f -> f.getType() == RateCard.class));
    }
}
