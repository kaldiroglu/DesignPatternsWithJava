package dev.kaldiroglu.dp.structural.composite.bom.domain;

/**
 * The engineering reference data for the sample product — the numbers, and
 * nothing else.
 *
 * <p>Both the naive design in {@code ..bom.problem} and the Composite design in
 * {@code ..bom.solution} build their bicycle from these constants. That is
 * deliberate: it makes the two designs provably comparable. If the naive
 * bicycle and the Composite bicycle ever disagreed about a total, the difference
 * would be in the <em>design</em>, not in the data.</p>
 *
 * <p>Notice that this class contains no structure at all — only figures. How the
 * parts nest, and how the nesting is traversed, is exactly what the two packages
 * disagree about.</p>
 */
public final class Catalog {

    private Catalog() {
    }

    /**
     * A purchased part: something bought from a supplier and not broken down
     * further.
     *
     * @param partNumber  the catalog identifier
     * @param name        the human-readable name
     * @param unitCost    the supplier's price for one
     * @param weightGrams the mass of one, in grams
     */
    public record PartSpec(String partNumber, String name, Money unitCost, int weightGrams) {
    }

    /**
     * An assembly: something the factory builds from other items.
     *
     * @param partNumber          the catalog identifier
     * @param name                the human-readable name
     * @param assemblyCost        labor, fasteners and paint for this level alone
     * @param assemblyWeightGrams the mass this level adds itself, e.g. weld
     */
    public record AssemblySpec(String partNumber, String name,
                               Money assemblyCost, int assemblyWeightGrams) {
    }

    /**
     * A subcontracted operation: it costs money, but it adds no mass and it is
     * not a part anyone can put on a shelf.
     *
     * <p>This is the "new kind of component" both packages are asked to absorb.
     * See {@code solution.Service} and {@code problem.Service}.</p>
     *
     * @param partNumber the catalog identifier
     * @param name       the human-readable name
     * @param fee        what the subcontractor charges
     */
    public record ServiceSpec(String partNumber, String name, Money fee) {
    }

    // --- Purchased parts ----------------------------------------------------

    public static final PartSpec RIM =
            new PartSpec("RIM-700C", "700c Rim", Money.of(24.00), 850);
    public static final PartSpec SPOKE =
            new PartSpec("SPOKE-14G", "14g Spoke", Money.of(0.40), 5);
    public static final PartSpec AXLE =
            new PartSpec("AXLE-QR", "Quick-release Axle", Money.of(6.50), 120);
    public static final PartSpec BEARING =
            new PartSpec("BEARING-6001", "6001 Sealed Bearing", Money.of(2.10), 15);
    public static final PartSpec TIRE =
            new PartSpec("TIRE-700x25", "700x25 Tire", Money.of(18.00), 260);
    public static final PartSpec TUBE =
            new PartSpec("TUBE-700", "700c Inner Tube", Money.of(4.50), 95);
    public static final PartSpec SADDLE =
            new PartSpec("SADDLE-CR", "Cromoly Saddle", Money.of(18.00), 310);
    public static final PartSpec TUBESET =
            new PartSpec("TUBESET-CR", "Cromoly Tubeset", Money.of(95.00), 1800);
    public static final PartSpec FORK =
            new PartSpec("FORK-CR", "Cromoly Fork", Money.of(42.00), 700);
    public static final PartSpec PAINT =
            new PartSpec("PAINT-KIT", "Paint & Decals", Money.of(6.00), 40);

    // --- Assemblies ---------------------------------------------------------

    public static final AssemblySpec HUB =
            new AssemblySpec("HUB-ASM", "Wheel Hub", Money.of(3.00), 20);
    public static final AssemblySpec WHEEL =
            new AssemblySpec("WHEEL-ASM", "700c Wheel", Money.of(12.00), 0);
    public static final AssemblySpec FRAME =
            new AssemblySpec("FRAME-ASM", "Frame Assembly", Money.of(25.00), 30);
    public static final AssemblySpec BICYCLE =
            new AssemblySpec("BIKE-CITY", "City Bicycle", Money.of(40.00), 0);

    // --- Subcontracted operations -------------------------------------------

    public static final ServiceSpec POWDER_COATING =
            new ServiceSpec("SVC-COAT", "Powder Coating", Money.of(14.00));

    // --- Quantities ---------------------------------------------------------

    public static final int SPOKES_PER_WHEEL = 32;
    public static final int BEARINGS_PER_HUB = 2;
    public static final int WHEELS_PER_BICYCLE = 2;
}
