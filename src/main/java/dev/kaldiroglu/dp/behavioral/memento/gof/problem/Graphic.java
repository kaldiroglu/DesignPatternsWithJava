package dev.kaldiroglu.dp.behavioral.memento.gof.problem;

/** A box on the canvas. Only its x position matters here. */
public final class Graphic {

    private final String name;
    private int x;

    public Graphic(String name, int x) {
        this.name = name;
        this.x = x;
    }

    public void move(int dx) {
        x += dx;
    }

    public String name() {
        return name;
    }

    public int x() {
        return x;
    }
}
