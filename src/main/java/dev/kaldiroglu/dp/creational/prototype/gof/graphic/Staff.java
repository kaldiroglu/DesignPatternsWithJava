package dev.kaldiroglu.dp.creational.prototype.gof.graphic;

public class Staff extends Graphic{

    private final String property ;

    public Staff(String name, String property) {
        super(name);
        this.property = property;
    }


    @Override
    public void draw(Position position) {

    }

    @Override
    public Graphic clone() {
        return new Staff(name, property);
    }
}
