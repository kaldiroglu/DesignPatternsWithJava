package dev.kaldiroglu.dp.structural.flyweight;

import dev.kaldiroglu.dp.structural.flyweight.book.correct.CharacterFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * The pool package documented itself as an implementation of Flyweight. It is an object
 * pool, which is the commonest thing mistaken for this pattern. Rather than delete the code,
 * these tests state the difference in the only terms that settle it — what the two factories
 * actually return.
 */
class PoolIsNotFlyweightTest {

    @Test
    @DisplayName("a flyweight factory returns the same object twice")
    void aFlyweightIsShared() throws Exception {
        CharacterFactory flyweights = new CharacterFactory();

        var first = flyweights.createCharacter('e', false);
        var second = flyweights.createCharacter('e', false);

        assertSame(first, second, "both holders have it, at the same time");
        assertEquals(1, flyweights.createdCount());
    }

    @Test
    @DisplayName("a pool returns a different object, because the first is still in use")
    void aPooledObjectIsExclusive() throws Exception {
        Object pool = pool(2);

        Object first = borrow(pool, 100);
        Object second = borrow(pool, 100);

        assertNotSame(first, second, "handing one connection to two callers would be the bug");
    }

    @Test
    @DisplayName("a pool runs out; a flyweight factory cannot")
    void aPoolIsFinite() throws Exception {
        Object pool = pool(1);

        Object only = borrow(pool, 100);
        Object none = borrow(pool, 50);

        assertNotSame(null, only);
        assertNull(none, "the second caller waits, and then gets nothing");

        CharacterFactory flyweights = new CharacterFactory();
        for (int i = 0; i < 1_000; i++) {
            assertSame(flyweights.createCharacter('e', false),
                    flyweights.createCharacter('e', false),
                    "a thousand callers, all served, all with the same object");
        }
    }

    @Test
    @DisplayName("a pooled object must be given back; a flyweight has no such protocol")
    void onlyOneOfThemHasARelease() {
        boolean poolHasRelease = java.util.Arrays.stream(poolClass().getDeclaredMethods())
                .anyMatch(m -> m.getName().equals("release"));
        boolean factoryHasRelease = java.util.Arrays.stream(
                        CharacterFactory.class.getDeclaredMethods())
                .anyMatch(m -> m.getName().equals("release") || m.getName().equals("returnTo"));

        assertTrue(poolHasRelease, "forget it and the pool leaks until it blocks");
        assertFalse(factoryHasRelease, "there is nothing to give back");
    }

    @Test
    @DisplayName("a pooled object is mutable; a flyweight is not")
    void mutabilityIsTheDecidingDifference() {
        boolean pooledIsMutable = java.util.Arrays.stream(pooledConnectionClass()
                        .getDeclaredMethods())
                .anyMatch(m -> m.getName().startsWith("mark"));
        boolean flyweightIsMutable = java.util.Arrays.stream(
                        dev.kaldiroglu.dp.structural.flyweight.book.correct.Character.class
                                .getDeclaredMethods())
                .anyMatch(m -> m.getName().startsWith("set"));

        assertTrue(pooledIsMutable, "it carries a transaction, a cursor, a state");
        assertFalse(flyweightIsMutable, "which is why concurrent sharing is safe");
    }

    // ------------------------------------------------------------------ reflection helpers
    // ConnectionPool and PooledConnection are package-private, so the test reaches them the
    // only way it can without moving the demo's classes into the public API for its sake.

    private static Class<?> poolClass() {
        try {
            return Class.forName(
                    "dev.kaldiroglu.dp.structural.flyweight.pool.ConnectionPool");
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }

    private static Class<?> pooledConnectionClass() {
        try {
            return Class.forName(
                    "dev.kaldiroglu.dp.structural.flyweight.pool.PooledConnection");
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }

    private static Object pool(int size) throws Exception {
        Constructor<?> constructor = poolClass().getDeclaredConstructor(int.class);
        constructor.setAccessible(true);
        return constructor.newInstance(size);
    }

    private static Object borrow(Object pool, long timeoutMs) throws Exception {
        Method borrow = poolClass().getDeclaredMethod("borrow", long.class);
        borrow.setAccessible(true);
        return borrow.invoke(pool, timeoutMs);
    }
}
