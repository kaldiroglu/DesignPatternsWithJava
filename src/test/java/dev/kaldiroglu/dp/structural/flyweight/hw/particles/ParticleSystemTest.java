package dev.kaldiroglu.dp.structural.flyweight.hw.particles;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Homework 3 — the particle system.
 * <p>
 * This one is chosen for what it cannot share. Position and velocity change sixty times a
 * second, so they can be neither shared nor made immutable — and the answer is not to give
 * up on the pattern but to notice that they need not be objects at all.
 */
class ParticleSystemTest {

    private static final int SPRITE = 16 * 1024;

    private static ParticleType spark(ParticleFactory factory) {
        return factory.get("Spark", "Orange", 0.1, 30);
    }

    @Test
    @DisplayName("a hundred thousand particles need four type objects")
    void typesAreShared() {
        ParticleFactory factory = new ParticleFactory(SPRITE);
        ParticleSystem system = new ParticleSystem(1_000);

        for (int i = 0; i < 100_000; i++) {
            ParticleType type = switch (i % 4) {
                case 0 -> factory.get("Spark", "Orange", 0.1, 30);
                case 1 -> factory.get("Smoke", "Gray", 0.02, 120);
                case 2 -> factory.get("Ember", "Red", 0.15, 60);
                default -> factory.get("Ash", "DarkGray", 0.05, 200);
            };
            system.spawn(type, i % 800, i % 600, 1, 1);
        }

        assertEquals(100_000, system.count());
        assertEquals(100_000, factory.requestCount());
        assertEquals(4, factory.distinctTypes());
        assertEquals(4L * SPRITE, (long) factory.distinctTypes() * SPRITE);
    }

    @Test
    @DisplayName("two particles of a kind hold the identical type object")
    void particlesOfAKindShareOneType() {
        ParticleFactory factory = new ParticleFactory(SPRITE);
        ParticleSystem system = new ParticleSystem(10);

        system.spawn(spark(factory), 0, 0, 1, 1);
        system.spawn(spark(factory), 500, 500, -1, 2);

        assertSame(system.typeOf(0), system.typeOf(1));
        assertEquals(1, factory.distinctTypes());
    }

    @Test
    @DisplayName("the varying state is arrays, so a million particles are not a million objects")
    void extrinsicStateIsNotObjects() {
        long objectFields = java.util.Arrays.stream(ParticleSystem.class.getDeclaredFields())
                .filter(f -> !f.isSynthetic())
                .filter(f -> f.getType().isArray() || f.getType() == java.util.List.class)
                .count();

        assertEquals(6, objectFields, "five parallel arrays and the list of type references");
        assertTrue(java.util.Arrays.stream(ParticleSystem.class.getDeclaredFields())
                        .anyMatch(f -> f.getType() == double[].class),
                "positions live in a double[], not in an object per particle");
    }

    @Test
    @DisplayName("gravity reads mass from the shared type without copying it")
    void physicsReadsTheFlyweight() {
        ParticleFactory factory = new ParticleFactory(SPRITE);
        ParticleSystem system = new ParticleSystem(10);

        ParticleType heavy = factory.get("Ember", "Red", 1.0, 60);
        ParticleType light = factory.get("Smoke", "Gray", 0.01, 120);
        system.spawn(heavy, 0, 100, 0, 0);
        system.spawn(light, 0, 100, 0, 0);

        for (int frame = 0; frame < 10; frame++) {
            system.tick();
        }

        assertTrue(system.yOf(0) < system.yOf(1), "the heavy one fell faster");
        assertNotEquals(system.yOf(0), system.yOf(1));
    }

    @Test
    @DisplayName("lifetime comes from the type, so expiry needs no per-particle copy")
    void expiryReadsTheSharedLifetime() {
        ParticleFactory factory = new ParticleFactory(SPRITE);
        ParticleSystem system = new ParticleSystem(10);

        system.spawn(factory.get("Spark", "Orange", 0.1, 5), 0, 0, 0, 0);
        system.spawn(factory.get("Ash", "DarkGray", 0.05, 200), 0, 0, 0, 0);

        for (int frame = 0; frame < 10; frame++) {
            system.tick();
        }

        assertEquals(1, system.expiredCount(), "the spark is gone, the ash is not");
    }
}
