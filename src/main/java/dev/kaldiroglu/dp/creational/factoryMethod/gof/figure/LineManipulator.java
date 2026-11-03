package dev.kaldiroglu.dp.creational.factoryMethod.gof.figure;

public class LineManipulator implements Manipulator{

    @Override
    public void downClick() {

    }

    @Override
    public void drag() {
        System.out.println("LineManipulator stretches the line");
    }

    @Override
    public void upClick() {

    }
}
