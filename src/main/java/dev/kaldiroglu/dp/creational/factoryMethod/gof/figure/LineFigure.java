package dev.kaldiroglu.dp.creational.factoryMethod.gof.figure;

public class LineFigure implements Figure{

    @Override
    public Manipulator createManipulator() {
        return new LineManipulator();
    }
}
