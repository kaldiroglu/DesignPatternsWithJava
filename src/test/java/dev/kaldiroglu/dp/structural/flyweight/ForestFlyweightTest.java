package dev.kaldiroglu.dp.structural.flyweight;

import dev.kaldiroglu.dp.structural.flyweight.forest.solution.Forest;
import dev.kaldiroglu.dp.structural.flyweight.forest.solution.TextureLoader;
import dev.kaldiroglu.dp.structural.flyweight.forest.solution.TreeFactory;
import dev.kaldiroglu.dp.structural.flyweight.forest.solution.TreeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * The forest example's original claim — that each tree kept its own copy of a megabyte —
 * was not true of the code: Java arrays are references, and every tree pointed at the same
 * one. The example measured a saving it was not making. These tests measure the real one.
 */
class ForestFlyweightTest {

    private static final int TEXTURE = 4_096;
    private static final int PER_KIND = 5_000;

    // ------------------------------------------------------------------ the problem

    @Test
    @DisplayName("without sharing, every tree loads its own texture")
    void problemLoadsOnceForEveryTree() {
        dev.kaldiroglu.dp.structural.flyweight.forest.problem.TextureLoader.reset();

        var forest = new dev.kaldiroglu.dp.structural.flyweight.forest.problem.Forest();
        byte[] oak = new byte[TEXTURE];
        byte[] pine = new byte[TEXTURE];
        for (int i = 0; i < PER_KIND; i++) {
            forest.plant(i, i, "Oak", "Green", oak);
            forest.plant(i, -i, "Pine", "DarkGreen", pine);
        }

        assertEquals(10_000, forest.size());
        assertEquals(10_000,
                dev.kaldiroglu.dp.structural.flyweight.forest.problem.TextureLoader.loadCount(),
                "two kinds of tree, ten thousand loads");
        assertEquals(10_000L * TEXTURE, forest.textureBytes(), "40 MB for two textures");
    }

    // ------------------------------------------------------------------ the solution

    @Test
    @DisplayName("with sharing, a texture is loaded once per kind")
    void solutionLoadsOncePerKind() {
        TextureLoader loader = new TextureLoader(TEXTURE);
        Forest forest = new Forest(new TreeFactory(loader));

        for (int i = 0; i < PER_KIND; i++) {
            forest.plant(i, i, "Oak", "Green");
            forest.plant(i, -i, "Pine", "DarkGreen");
        }

        assertEquals(10_000, forest.size(), "the same forest is on screen");
        assertEquals(2, forest.distinctTypes());
        assertEquals(2, loader.loadCount(), "ten thousand trees, two loads");
        assertEquals(2L * TEXTURE, forest.textureBytes(), "8 KB, not 40 MB");
    }

    @Test
    @DisplayName("every tree of a kind points at the same TreeType")
    void treesOfAKindShareOneType() {
        TreeFactory factory = new TreeFactory(new TextureLoader(TEXTURE));

        TreeType first = factory.getType("Oak", "Green");
        TreeType second = factory.getType("Oak", "Green");

        assertSame(first, second);
        assertEquals(2, factory.requestCount());
        assertEquals(1, factory.distinctTypes());
    }

    @Test
    @DisplayName("color is part of the key, so a red oak is a different kind")
    void theKeyCoversAllOfTheIntrinsicState() {
        TreeFactory factory = new TreeFactory(new TextureLoader(TEXTURE));

        TreeType green = factory.getType("Oak", "Green");
        TreeType autumn = factory.getType("Oak", "Red");

        assertEquals(2, factory.distinctTypes(), "same species, different look, different type");
        assertEquals("Green", green.getColor());
        assertEquals("Red", autumn.getColor());
    }

    @Test
    @DisplayName("the factory loads the texture, so a caller cannot supply a conflicting one")
    void theFactoryOwnsTheIntrinsicState() {
        // The earlier signature took a texture argument and ignored it whenever the type
        // already existed, so a second caller's texture vanished without a word. Loading
        // inside the factory removes the argument, and with it the whole class of bug.
        boolean takesATexture = java.util.Arrays.stream(TreeFactory.class.getMethods())
                .filter(m -> m.getName().equals("getType"))
                .anyMatch(m -> java.util.Arrays.stream(m.getParameterTypes())
                        .anyMatch(t -> t == byte[].class));

        assertTrue(!takesATexture, "getType names only the intrinsic state it keys on");
    }

    @Test
    @DisplayName("the texture is loaded per kind, named by the kind that needed it")
    void loadsAreAttributable() {
        TextureLoader loader = new TextureLoader(TEXTURE);
        Forest forest = new Forest(new TreeFactory(loader));

        forest.plant(0, 0, "Oak", "Green");
        forest.plant(1, 1, "Oak", "Green");
        forest.plant(2, 2, "Pine", "DarkGreen");

        assertEquals(java.util.List.of("Oak", "Pine"), loader.loadedKinds());
    }
}
