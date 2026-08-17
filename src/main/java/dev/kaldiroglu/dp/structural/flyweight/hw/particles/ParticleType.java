package dev.kaldiroglu.dp.structural.flyweight.hw.particles;

/**
 * <b>ConcreteFlyweight</b> — what a particle is: sprite, color, mass, lifetime.
 *
 * <p>This is the homework that shows the solution's limit as clearly as its benefit. Sharing
 * the <em>kind</em> of particle is easy. The position and velocity cannot be shared, cannot
 * be made immutable, and change sixty times a second — so they stay outside, in a place the
 * flyweight never sees.</p>
 */
public final class ParticleType {

    private final String name;
    private final String color;
    private final double mass;
    private final int lifetimeTicks;
    private final byte[] sprite;

    ParticleType(String name, String color, double mass, int lifetimeTicks, byte[] sprite) {
        this.name = name;
        this.color = color;
        this.mass = mass;
        this.lifetimeTicks = lifetimeTicks;
        this.sprite = sprite.clone();
    }

    public String name() {
        return name;
    }

    public String color() {
        return color;
    }

    public double mass() {
        return mass;
    }

    public int lifetimeTicks() {
        return lifetimeTicks;
    }

    public int spriteBytes() {
        return sprite.length;
    }

    /** Renders one particle. Everything that varies arrives as an argument. */
    public String render(double x, double y, int age) {
        return String.format("%s(%s) at (%.1f,%.1f) age %d/%d", name, color, x, y,
                age, lifetimeTicks);
    }
}
