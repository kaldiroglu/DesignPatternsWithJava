package dev.kaldiroglu.dp.structural.flyweight.hw.particles;

import java.util.ArrayList;
import java.util.List;

/**
 * The extrinsic state of every particle, in parallel arrays.
 *
 * <p>Worth a moment. The obvious design gives each particle an object holding x, y, dx, dy,
 * age and a type reference — and a million of those is a million objects for the collector
 * to walk. Keeping the varying state in {@code double[]} arrays instead means the particles
 * are not objects at all; only their four <em>kinds</em> are.</p>
 *
 * <p>That is the pattern taken to its conclusion: Flyweight removes objects, and the state
 * that could not be shared did not have to become objects either.</p>
 */
public class ParticleSystem {

    private final List<ParticleType> types = new ArrayList<>();
    private double[] x;
    private double[] y;
    private double[] dx;
    private double[] dy;
    private int[] age;
    private int count;

    public ParticleSystem(int capacity) {
        this.x = new double[capacity];
        this.y = new double[capacity];
        this.dx = new double[capacity];
        this.dy = new double[capacity];
        this.age = new int[capacity];
    }

    public void spawn(ParticleType type, double px, double py, double vx, double vy) {
        if (count == x.length) {
            grow();
        }
        types.add(type);
        x[count] = px;
        y[count] = py;
        dx[count] = vx;
        dy[count] = vy;
        age[count] = 0;
        count++;
    }

    /** One frame. Gravity scales with the shared type's mass — read, never copied. */
    public void tick() {
        for (int i = 0; i < count; i++) {
            dy[i] -= 9.81 * types.get(i).mass() * 0.016;
            x[i] += dx[i];
            y[i] += dy[i];
            age[i]++;
        }
    }

    /** Particles whose age has passed their type's lifetime. */
    public int expiredCount() {
        int expired = 0;
        for (int i = 0; i < count; i++) {
            if (age[i] >= types.get(i).lifetimeTicks()) {
                expired++;
            }
        }
        return expired;
    }

    public String render(int index) {
        return types.get(index).render(x[index], y[index], age[index]);
    }

    public int count() {
        return count;
    }

    public double yOf(int index) {
        return y[index];
    }

    public ParticleType typeOf(int index) {
        return types.get(index);
    }

    private void grow() {
        int size = x.length * 2;
        x = java.util.Arrays.copyOf(x, size);
        y = java.util.Arrays.copyOf(y, size);
        dx = java.util.Arrays.copyOf(dx, size);
        dy = java.util.Arrays.copyOf(dy, size);
        age = java.util.Arrays.copyOf(age, size);
    }
}
