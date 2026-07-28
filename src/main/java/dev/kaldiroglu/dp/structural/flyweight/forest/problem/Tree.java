package dev.kaldiroglu.dp.structural.flyweight.forest.problem;

/**
 * A tree that owns everything about itself.
 *
 * <p>Five fields, of which three — name, color and texture — are the same for every tree of
 * the same kind. Plant ten thousand oaks and you have ten thousand copies of the word "Oak",
 * ten thousand copies of "Green", and ten thousand textures.</p>
 *
 * <p>The texture is copied on purpose. An earlier version of this class stored the caller's
 * array directly and the comment claimed each tree kept "its own copy of the bytes" — which
 * was not true, because Java arrays are references and every tree pointed at the same one.
 * The example measured a saving it was not making. {@link #Tree} now copies, which is what a
 * loader reading a texture per tree would really do, and {@link TextureLoader} counts it.</p>
 */
public class Tree {

    private final int x;
    private final int y;
    private final String name;
    private final String color;
    private final byte[] texture;

    public Tree(int x, int y, String name, String color, byte[] texture) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.color = color;
        this.texture = TextureLoader.load(texture);   // this tree's own bytes
    }

    public void draw() {
        System.out.println("Drawing " + color + " " + name + " at (" + x + "," + y
                + ") with " + texture.length + " texture bytes");
    }

    /** Bytes retained by this one tree. */
    public int textureBytes() {
        return texture.length;
    }
}
