package dev.kaldiroglu.dp.structural.composite.bom.solution;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;

/**
 * Builds the sample product structure used by the demo and the tests, from the
 * same figures in {@link Catalog} that {@code problem.NaiveProductCatalog} uses.
 *
 * <p>A city bicycle: a welded frame assembly, two identical wheel assemblies —
 * each with its own hub sub-assembly — and a saddle bought as a single part. The
 * structure is four levels deep and reuses one wheel object twice, which is what
 * makes it worth modelling with Composite rather than with a flat list.</p>
 *
 * <p>Read this class beside {@code problem.NaiveProductCatalog}. That one has to
 * build the wheel twice and hand back two wheels and two hubs; this one builds
 * the wheel once and says {@code .add(wheel, 2)}.</p>
 */
public final class ProductCatalog {

    private ProductCatalog() {
    }

    /**
     * The bicycle, and the shared sub-assemblies it is built from.
     *
     * @param bicycle the finished product, the root of the structure
     * @param frame   the frame sub-assembly
     * @param wheel   the wheel sub-assembly — <b>one</b> object used twice
     * @param hub     the hub sub-assembly, nested inside the wheel
     * @param spoke   a purchased part, exposed so tests can change its quantity
     */
    public record Bicycle(Assembly bicycle, Assembly frame, Assembly wheel,
                          Assembly hub, Part spoke) {
    }

    /** Assembles the sample bicycle from the ground up. */
    public static Bicycle cityBicycle() {
        // --- Purchased parts (leaves) ---------------------------------------
        Part rim = new Part(Catalog.RIM);
        Part spoke = new Part(Catalog.SPOKE);
        Part axle = new Part(Catalog.AXLE);
        Part bearing = new Part(Catalog.BEARING);
        Part tire = new Part(Catalog.TIRE);
        Part tube = new Part(Catalog.TUBE);
        Part saddle = new Part(Catalog.SADDLE);
        Part tubeset = new Part(Catalog.TUBESET);
        Part fork = new Part(Catalog.FORK);
        Part paint = new Part(Catalog.PAINT);

        // --- Sub-assemblies (composites) ------------------------------------
        Assembly hub = new Assembly(Catalog.HUB)
                .add(axle)
                .add(bearing, Catalog.BEARINGS_PER_HUB);

        Assembly wheel = new Assembly(Catalog.WHEEL)
                .add(rim)
                .add(spoke, Catalog.SPOKES_PER_WHEEL)  // one Part, one line, quantity 32
                .add(hub)                              // an assembly inside an assembly
                .add(tire)
                .add(tube);

        Assembly frame = new Assembly(Catalog.FRAME)
                .add(tubeset)
                .add(fork)
                .add(paint);

        // --- The finished product -------------------------------------------
        Assembly bicycle = new Assembly(Catalog.BICYCLE)
                .add(frame)
                .add(wheel, Catalog.WHEELS_PER_BICYCLE)  // ONE wheel object, required twice
                .add(saddle);

        return new Bicycle(bicycle, frame, wheel, hub, spoke);
    }
}
