package dev.kaldiroglu.dp.behavioral.strategy.freight;

/**
 * One parcel, described the way every carrier's rate card needs it described.
 * <p>
 * The awkward part of freight is that no two carriers rate on the same thing. One charges
 * by weight, one by the volume the parcel occupies on the van, one by which zone it is
 * going to. So the shipment carries all of it, and each rating algorithm reads the part it
 * cares about.
 *
 * @param fromZone  where it is collected
 * @param toZone    where it is going
 * @param grams     actual weight
 * @param lengthCm  longest side
 * @param widthCm   second side
 * @param heightCm  third side
 */
public record Shipment(String fromZone, String toZone,
                       int grams, int lengthCm, int widthCm, int heightCm) {

    public Shipment {
        if (grams < 1) {
            throw new IllegalArgumentException("a shipment has to weigh something");
        }
    }

    /** Volume in cubic centimetres. */
    public int volumeCm3() {
        return lengthCm * widthCm * heightCm;
    }

    /**
     * Volumetric weight, in grams, on the divisor Turkish carriers call <i>desi</i>.
     * <p>
     * One desi is 3000 cubic centimetres, and it counts as one kilogram. A large light
     * parcel is charged as though it were heavy, because what it costs the carrier is the
     * space on the van rather than the load on the axle.
     */
    public int desiGrams() {
        return volumeCm3() * 1000 / 3000;
    }

    /** What a carrier that charges for whichever is greater will use. */
    public int chargeableGrams() {
        return Math.max(grams, desiGrams());
    }

    public boolean isDomestic() {
        return fromZone.equals(toZone);
    }
}
