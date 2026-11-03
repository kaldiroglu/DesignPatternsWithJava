package dev.kaldiroglu.dp.creational.factoryMethod.gof.figure;

public class TextFigure implements Figure{

    @Override
    public Manipulator createManipulator() {
        return new TextManipulator();
    }
}
