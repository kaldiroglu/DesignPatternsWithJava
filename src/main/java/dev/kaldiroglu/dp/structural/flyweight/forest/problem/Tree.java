package dev.kaldiroglu.dp.structural.flyweight.forest.problem;

public class Tree {
    private final int x;
    private final int y;
    private final String name;
    private final String color;
    private final byte[] texture; // could be megabytes

    public Tree(int x, int y, String name, String color, byte[] texture) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.color = color;
        this.texture = texture; // each Tree keeps its own copy of the bytes
    }

    public void draw() {
        System.out.println("Drawing " + color + " " + name + " at (" + x + "," + y
            + ") with " + texture.length + " texture bytes");
    }
}
