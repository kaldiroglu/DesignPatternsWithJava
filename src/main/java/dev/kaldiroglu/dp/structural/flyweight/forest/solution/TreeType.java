package dev.kaldiroglu.dp.structural.flyweight.forest.solution;

public final class TreeType {

    private final String name;
    private final String color;
    private final byte[] texture;

    public TreeType(String name, String color, byte[] texture) {
        this.name = name;
        this.color = color;
        this.texture = texture;
    }

    public void draw(int x, int y) {
        System.out.println("Drawing " + color + " " + name + " at (" + x + "," + y
            + ") with " + texture.length + " texture bytes");
    }

    public String getName() { return name; }
    public String getColor() { return color; }
}
