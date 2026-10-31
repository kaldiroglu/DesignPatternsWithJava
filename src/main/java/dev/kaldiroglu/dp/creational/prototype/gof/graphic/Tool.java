package dev.kaldiroglu.dp.creational.prototype.gof.graphic;

public abstract class Tool {
    protected Graphic prototype;

    public Tool(Graphic prototype){
        this.prototype = prototype;
    }

    public abstract void manipluate();
}
