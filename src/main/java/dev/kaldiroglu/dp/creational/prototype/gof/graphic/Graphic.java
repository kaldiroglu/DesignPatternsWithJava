package dev.kaldiroglu.dp.creational.prototype.gof.graphic;

public abstract class Graphic {
    protected String name;

    public Graphic(String name) {
        this.name = name;
    }

    public abstract void draw(Position position);

    public abstract Graphic clone();
}
