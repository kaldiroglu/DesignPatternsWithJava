package dev.kaldiroglu.dp.structural.flyweight.forest.solution;

/**
 * <b>ConcreteFlyweight</b> — everything that is true of a kind of tree rather than of one
 * tree: its name, its color, and its texture.
 *
 * <p>Immutable, and created only by {@link TreeFactory}. The position is not here, which is
 * the point: {@link #draw} receives it as a parameter, so one instance can be drawn at ten
 * thousand different places without holding any of them.</p>
 */
public final class TreeType {

    private final String name;
    private final String color;
    private final byte[] texture;

    TreeType(String name, String color, byte[] texture) {
        this.name = name;
        this.color = color;
        this.texture = texture.clone();   // loaded once, for every tree of this kind
    }

    /** Extrinsic state arrives as arguments. GoF, p. 198: "acts on extrinsic state". */
    public void draw(int x, int y) {
        System.out.println("Drawing " + color + " " + name + " at (" + x + "," + y
                + ") with " + texture.length + " texture bytes");
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int textureBytes() {
        return texture.length;
    }
}
