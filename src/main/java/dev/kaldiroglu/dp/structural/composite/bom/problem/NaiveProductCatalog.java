package dev.kaldiroglu.dp.structural.composite.bom.problem;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;

/**
 * Builds the same city bicycle as {@code solution.ProductCatalog}, from the same
 * figures in {@link Catalog} — but in the naive design.
 *
 * <p>Two things are worth watching while reading {@link #cityBicycle()}:</p>
 *
 * <ol>
 *   <li><b>The wheel is built twice.</b> There is no way to say "two of these",
 *       so {@link #wheel()} is called once per wheel and the bicycle ends up
 *       holding two separate objects that merely happen to match. Change one and
 *       the other does not follow.</li>
 *   <li><b>Thirty-two spokes are thirty-two list entries.</b> Every traversal, in
 *       every client, walks all of them — 85 list entries for this bicycle, where
 *       the Composite version walks 13.</li>
 * </ol>
 */
public final class NaiveProductCatalog {

    private NaiveProductCatalog() {
    }

    /**
     * The bicycle, and the interior objects the tests need to reach.
     *
     * <p>Note that this record has to expose <b>two</b> wheels and <b>two</b>
     * hubs, where the Composite version exposes one of each. The duplication is
     * not an accident of this class; it is forced by the design.</p>
     */
    public record Bicycle(Assembly bicycle, Assembly frame,
                          Assembly wheel1, Assembly wheel2,
                          Assembly hub1, Assembly hub2) {
    }

    /** Assembles the sample bicycle. */
    public static Bicycle cityBicycle() {
        Assembly frame = new Assembly(Catalog.FRAME);
        frame.addPart(new Part(Catalog.TUBESET));
        frame.addPart(new Part(Catalog.FORK));
        frame.addPart(new Part(Catalog.PAINT));

        // The wheel has to be built once per wheel, because a quantity cannot be
        // expressed. These two objects are equal in every field and identical in
        // no way that the code can rely on.
        Assembly wheel1 = wheel();
        Assembly wheel2 = wheel();

        Assembly bicycle = new Assembly(Catalog.BICYCLE);
        bicycle.addSubAssembly(frame);
        bicycle.addSubAssembly(wheel1);
        bicycle.addSubAssembly(wheel2);
        bicycle.addPart(new Part(Catalog.SADDLE));

        return new Bicycle(bicycle, frame, wheel1, wheel2,
                wheel1.subAssemblies().get(0), wheel2.subAssemblies().get(0));
    }

    /** Builds one wheel, hub and all. Called once per wheel on the product. */
    private static Assembly wheel() {
        Assembly hub = new Assembly(Catalog.HUB);
        hub.addPart(new Part(Catalog.AXLE));
        hub.addPart(new Part(Catalog.BEARING), Catalog.BEARINGS_PER_HUB);

        Assembly wheel = new Assembly(Catalog.WHEEL);
        wheel.addPart(new Part(Catalog.RIM));
        // 32 entries in the list, walked by every client on every query.
        wheel.addPart(new Part(Catalog.SPOKE), Catalog.SPOKES_PER_WHEEL);
        wheel.addSubAssembly(hub);
        wheel.addPart(new Part(Catalog.TIRE));
        wheel.addPart(new Part(Catalog.TUBE));
        return wheel;
    }
}
